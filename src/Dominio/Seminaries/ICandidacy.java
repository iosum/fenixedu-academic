/*
 * Created on 29/Jul/2003, 11:47:10
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package Dominio.Seminaries;
import java.util.List;

import Dominio.IDomainObject;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 29/Jul/2003, 11:47:10
 * 
 */
public interface ICandidacy extends IDomainObject
{
	/**
	 * @return
	 */
	public abstract List getCaseStudyChoices();
	/**
	 * @return
	 */
	public abstract Integer getIdInternal();
	/**
	 * @param list
	 */
	public abstract void setCaseStudyChoices(List list);
	/**
	 * @param integer
	 */
	public abstract void setIdInternal(Integer integer);

	public abstract Integer getModalityIdInternal();
	/**
	 * @return
	 */
	public abstract Integer getSeminaryIdInternal();
	/**
	 * @return
	 */
	public abstract Integer getThemeIdInternal();
	/**
	 * @param integer
	 */

	public abstract void setThemeIdInternal(Integer integer);
    
    public void setCurricularCourseIdInternal(Integer integer);

    public void setStudentIdInternal(Integer integer);
    public Integer getStudentIdInternal();
    public Integer getCurricularCourseIdInternal();
        
    public abstract void setModalityIdInternal(Integer modality);
    
    public void setSeminaryIdInternal(Integer integer);
    
    public abstract String getMotivation();
    public abstract void setMotivation(String motivation); 

    

}