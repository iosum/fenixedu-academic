/*
 * Created on 2/Abr/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExportGrouping;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGrouping;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author T�nia Pous�o
 * 
 */
public class DeleteGrouping implements IService {

    public Boolean run(Integer executionCourseId, Integer groupPropertiesId)
            throws FenixServiceException, ExcepcaoPersistencia {

        if (groupPropertiesId == null) {
            return Boolean.FALSE;
        }

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentGrouping persistentGrouping = sp.getIPersistentGrouping();
        IPersistentExportGrouping persistentExportGrouping = sp.getIPersistentExportGrouping();

        IGrouping groupProperties = (IGrouping) persistentGrouping.readByOID(Grouping.class,
                groupPropertiesId);

        if (groupProperties == null) {
            throw new ExistingServiceException();
        }

        groupProperties.delete();
        
        persistentGrouping.deleteByOID(Grouping.class, groupPropertiesId);

        return Boolean.TRUE;
    }

}
