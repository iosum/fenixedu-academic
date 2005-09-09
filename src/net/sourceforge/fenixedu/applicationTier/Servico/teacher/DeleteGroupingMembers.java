/*
 * Created on 24/Ago/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGrouping;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author joaosa & rmalo
 * 
 */

public class DeleteGroupingMembers implements IService {

    public boolean run(Integer executionCourseCode, Integer groupingCode, List studentUsernames)
            throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentGrouping persistentGrouping = persistentSupport.getIPersistentGrouping();
        final IGrouping grouping = (IGrouping) persistentGrouping
                .readByOID(Grouping.class, groupingCode);
        if (grouping == null) {
            throw new ExistingServiceException();
        }

        final IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
                .getInstance();
        IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
                .getGroupEnrolmentStrategyInstance(grouping);

        if (!strategy.checkStudentsUserNamesInGrouping(studentUsernames, grouping)) {
            throw new InvalidSituationServiceException();
        }

        for (final String studentUsername : (List<String>) studentUsernames) {
            IAttends attend = grouping.getStudentAttend(studentUsername);
            removeAttendFromStudentGroups(grouping, attend);
            grouping.removeAttends(attend);
        }        
        return true;
    }

    private void removeAttendFromStudentGroups(final IGrouping grouping, IAttends attend) {
        if (attend != null) {
            for (final IStudentGroup studentGroup : grouping.getStudentGroups()) {
                studentGroup.removeAttends(attend);
            }
        }
    }
}