package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;

/**
 * @author David Santos in Jun 23, 2004
 */

public class PreviousYearsCurricularCourseEnrollmentRuleIgnoringLastYears implements IEnrollmentRule {

    private IExecutionPeriod executionPeriod;

    private IBranch studentBranch;

    private int degreeDuration;

    private int firstOfTheLastYears;

    public PreviousYearsCurricularCourseEnrollmentRuleIgnoringLastYears(
            IStudentCurricularPlan studentCurricularPlan, IExecutionPeriod executionPeriod,
            int firstOfTheLastYears) {
        this.executionPeriod = executionPeriod;
        this.studentBranch = studentCurricularPlan.getBranch();
        this.degreeDuration = studentCurricularPlan.getDegreeCurricularPlan().getDegreeDuration()
                .intValue();
        this.firstOfTheLastYears = firstOfTheLastYears;
    }

    public List apply(List curricularCoursesToBeEnrolledIn) {

        Map map = buildHashMapForCurricularCoursesList(curricularCoursesToBeEnrolledIn);
        boolean canPassToNextYear = true;

        for (int i = 1; (i <= this.degreeDuration) && canPassToNextYear; i++) {

            List curricularCourses = (List) map.get(new Integer(i));

            if ((curricularCourses != null) && (!curricularCourses.isEmpty())) {

                int size = curricularCourses.size();
                int counter = 0;

                for (int j = 0; j < size; j++) {

                    CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) curricularCourses
                            .get(j);
                    if (curricularCourse2Enroll.getEnrollmentType().equals(
                            CurricularCourseEnrollmentType.TEMPORARY)) {
                        counter++;
                    }
                }

                if (counter == size || i >= firstOfTheLastYears) {
                    canPassToNextYear = true;
                } else {

                    canPassToNextYear = false;

                    for (int j = (i + 1); j <= this.degreeDuration; j++) {
                        List curricularCoursesList = (List) map.get(new Integer(j));

                        if ((curricularCoursesList != null) && (!curricularCoursesList.isEmpty())) {
                            curricularCoursesToBeEnrolledIn.removeAll(curricularCoursesList);
                        }
                    }
                }
            } else {
                canPassToNextYear = true;
            }
        }

        return curricularCoursesToBeEnrolledIn;
    }

    private Map buildHashMapForCurricularCoursesList(List curricularCourses2Enroll) {

        Map map = new HashMap();

        List copy = new ArrayList();
        copy.addAll(curricularCourses2Enroll);

        for (int i = 1; i <= this.degreeDuration; i++) {
            int size = copy.size();

            List curricularCoursesToRemove = new ArrayList();

            for (int j = 0; j < size; j++) {
                CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) copy.get(j);

                if (isCurricularCourseFromYear(i, curricularCourse2Enroll.getCurricularCourse())) {
                    putCurricularCourseInHashMap(map, i, curricularCourse2Enroll);
                    curricularCoursesToRemove.add(curricularCourse2Enroll);
                }
            }

            copy.removeAll(curricularCoursesToRemove);
        }

        return map;
    }

    private boolean isCurricularCourseFromYear(int i, ICurricularCourse curricularCourse) {
        return (curricularCourse.getCurricularYearByBranchAndSemester(this.studentBranch,
                this.executionPeriod.getSemester()).getYear().intValue() == i);
    }

    private void putCurricularCourseInHashMap(Map map, int i,
            CurricularCourse2Enroll curricularCourse2Enroll) {

        Integer key = new Integer(i);

        List curricularCoursesList = (List) map.get(key);

        if (curricularCoursesList == null) {
            curricularCoursesList = new ArrayList();
            curricularCoursesList.add(curricularCourse2Enroll);
        } else {
            if (!curricularCoursesList.contains(curricularCourse2Enroll)) {
                curricularCoursesList.add(curricularCourse2Enroll);
            }
        }

        map.put(key, curricularCoursesList);
    }
}