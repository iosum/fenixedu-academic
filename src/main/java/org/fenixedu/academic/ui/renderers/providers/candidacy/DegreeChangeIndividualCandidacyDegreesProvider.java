/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.renderers.providers.candidacy;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.candidacyProcess.CandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean;
import org.fenixedu.academic.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPersonProcess;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.ui.struts.action.candidacy.CandidacyProcessDA.ChooseDegreeBean;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeChangeIndividualCandidacyDegreesProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        final Set<AcademicProgram> programs =
                AcademicAccessRule.getProgramsAccessibleToFunction(AcademicOperationType.MANAGE_INDIVIDUAL_CANDIDACIES,
                        Authenticate.getUser()).collect(Collectors.toSet());

        return getDegrees(source).stream().filter(degree -> programs.contains(degree)).collect(Collectors.toList());
    }

    private Collection<Degree> getDegrees(Object source) {
        if (source instanceof IndividualCandidacyProcessWithPrecedentDegreeInformationBean) {
            IndividualCandidacyProcessWithPrecedentDegreeInformationBean bean =
                    (IndividualCandidacyProcessWithPrecedentDegreeInformationBean) source;

            if (bean.getCandidacyProcess() != null) {
                return bean.getCandidacyProcess().getDegreeSet();
            } else {
                return bean.getIndividualCandidacyProcess().getCandidacyProcess().getDegreeSet();
            }
        }
        if (source instanceof ChooseDegreeBean) {
            final ChooseDegreeBean bean = (ChooseDegreeBean) source;
            final CandidacyProcess candidacyProcess = bean.getCandidacyProcess();
            if (candidacyProcess instanceof DegreeCandidacyForGraduatedPersonProcess) {
                final DegreeCandidacyForGraduatedPersonProcess candidacyForGraduatedPersonProcess =
                        (DegreeCandidacyForGraduatedPersonProcess) candidacyProcess;
                return candidacyForGraduatedPersonProcess.getAvailableDegrees();
            }
        }
        return Degree.readAllMatching(DegreeType.oneOf(DegreeType::isBolonhaMasterDegree, DegreeType::isIntegratedMasterDegree));
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
