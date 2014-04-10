package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.Collection;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.collect.Sets;

public class UpdateDepartmentsCompetenceCourseManagementGroup {

    @Atomic
    public static void run(Department department, String[] add, String[] remove) {
        check(RolePredicates.DEPARTMENT_ADMINISTRATIVE_OFFICE_PREDICATE);

        Group original = department.getCompetenceCourseMembersGroup();

        Group changed = original;
        if (add != null) {
            for (String personID : add) {
                Person person = FenixFramework.getDomainObject(personID);
                changed = changed.grant(person.getUser());
            }
        }
        if (remove != null) {
            for (String personID : remove) {
                Person person = FenixFramework.getDomainObject(personID);
                changed = changed.revoke(person.getUser());
            }
        }

        updateBolonhaManagerRoleToGroupDelta(department, original, changed);
        department.setCompetenceCourseMembersGroup(changed);
    }

    private static void updateBolonhaManagerRoleToGroupDelta(Department department, Group original, Group changed) {
        Set<User> originalMembers = original.getMembers();
        Set<User> newMembers = changed.getMembers();
        for (User user : Sets.difference(originalMembers, newMembers)) {
            Person person = user.getPerson();
            if (person.hasRole(RoleType.BOLONHA_MANAGER) && !belongsToOtherGroupsWithSameRole(department, person)) {
                person.removeRoleByType(RoleType.BOLONHA_MANAGER);
            }
        }
        Role bolonhaRole = Role.getRoleByRoleType(RoleType.BOLONHA_MANAGER);
        for (User user : Sets.difference(newMembers, originalMembers)) {
            Person person = user.getPerson();
            if (!person.hasRole(RoleType.BOLONHA_MANAGER)) {
                person.addPersonRoles(bolonhaRole);
            }
        }
    }

    private static boolean belongsToOtherGroupsWithSameRole(Department departmentWhoAsks, Person person) {
        Collection<Department> departments = Bennu.getInstance().getDepartmentsSet();
        for (Department department : departments) {
            if (department != departmentWhoAsks) {
                Group group = department.getCompetenceCourseMembersGroup();
                if (group != null && group.isMember(person.getUser())) {
                    return true;
                }
            }
        }

        for (Degree degree : Degree.readBolonhaDegrees()) {
            for (DegreeCurricularPlan dcp : degree.getDegreeCurricularPlans()) {
                if (dcp.getCurricularPlanMembersGroup().isMember(person.getUser())) {
                    return true;
                }
            }
        }

        return false;
    }

}