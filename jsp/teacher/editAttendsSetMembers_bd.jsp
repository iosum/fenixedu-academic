<%@ page language="java" %>

<%@ page import="java.lang.String" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="title.editAttendsSetMembers"/></h2>


<logic:present name="siteView" property="component"> 
<bean:define id="component" name="siteView" property="component" />
<bean:define id="infoGrouping" name="component" property="infoGrouping" />

<br/>

	<table width="100%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop">
				<bean:message key="label.teacher.EditAttendsSetMembers.description" />
			</td>
		</tr>
	</table>
	<br/>
	
<span class="error"><html:errors/></span>
<br/>
<br/>

<html:link page="<%="/viewAttendsSet.do?method=viewAttendsSet&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") %>">
    	<bean:message key="link.backToAttendsSet"/></html:link><br/>
  


<logic:empty name="infoGrouping" property="infoAttends">
<h2><bean:message key="message.infoAttendsSet.not.available" /></h2>
</logic:empty> 	

<logic:notEmpty name="infoGrouping" property="infoAttends">
	<html:form action="/deleteAttendsSetMembers" method="get">
	<html:hidden property="page" value="1"/>
	<bean:message key="message.editAttendsSetMembers.RemoveMembers"/>

	<br>
	<br>		 
	<table width="50%" cellpadding="0" border="0">
		<tr>
			<td class="listClasses-header"></td>
			<td class="listClasses-header"><bean:message key="label.teacher.StudentNumber" /></td>
			<td class="listClasses-header"><bean:message key="label.teacher.StudentName" /></td>
			<td class="listClasses-header"><bean:message key="label.teacher.StudentEmail" /></td>		
		</tr>	
		<logic:iterate id="infoAttend" name="infoGrouping" property="infoAttends">
			<bean:define id="infoStudent" name="infoAttend" property="aluno" />
			<bean:define id="infoPerson" name="infoStudent" property="infoPerson" />	
			<tr>	
				<td class="listClasses">
					<html:multibox property="studentsToRemove">
					<bean:write name="infoPerson" property="username" />
					</html:multibox>
				</td>	
				<td class="listClasses"><bean:write name="infoStudent" property="number" /></td>	
				<td class="listClasses"><bean:write name="infoPerson" property="nome" /></td>	
				<td class="listClasses"><bean:write name="infoPerson" property="email" /></td>		
		 	</tr>	
		</logic:iterate>
	</table>
	<br>
	<html:submit styleClass="inputbutton"><bean:message key="button.removeAluno"/>               		         	
	</html:submit>
	<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
	</html:reset>
	<br>
	<br>
	<html:hidden property="method" value="deleteAttendsSetMembers"/>
	<html:hidden property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
	<html:hidden property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
	</html:form>
</logic:notEmpty> 	

</logic:present>

<logic:notPresent name="siteView" property="component">
<h2>
<bean:message key="message.infoAttendsSet.not.available" />
</h2>
</logic:notPresent>


<logic:present name="infoStudentList"> 
		
<html:form action="/insertAttendsSetMembers" method="get">
<html:hidden property="page" value="1"/>

<logic:empty name="infoStudentList"> 
<h2>
<bean:message key="message.editAttendsSetMembers.NoMembersToAdd" />
</h2>
</logic:empty>

<logic:notEmpty name="infoStudentList"> 
<bean:message key="message.editAttendsSetMembers.InsertMembers"/>
<br>
<br>

<table width="50%" cellpadding="0" border="0">

	
	<tr>
		<td class="listClasses-header">
		</td>
		<td class="listClasses-header"><bean:message key="label.teacher.StudentNumber" />
		</td>
		<td class="listClasses-header"><bean:message key="label.teacher.StudentName" />
		</td>
		<td class="listClasses-header"><bean:message key="label.teacher.StudentEmail" />
		</td>
	</tr>


	<logic:iterate id="infoStudent" name="infoStudentList">			
		<tr>	
			<td class="listClasses">
			<html:multibox property="studentCodesToInsert">
			<bean:write name="infoStudent" property="idInternal"/>
			</html:multibox>
			</td>	
			<td class="listClasses"><bean:write name="infoStudent" property="number"/>
			</td>	
			<bean:define id="infoPerson" name="infoStudent" property="infoPerson"/>		
			<td class="listClasses"><bean:write name="infoPerson" property="nome"/>
			</td>
			<td class="listClasses"><bean:write name="infoPerson" property="email"/>
			</td>
	 	</tr>	
	 </logic:iterate>
	 

</table>
<br>

<html:hidden property="method" value="insertAttendsSetMembers"/>
<html:hidden  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />


<html:submit styleClass="inputbutton"><bean:message key="button.insertAluno"/>                    		         	
</html:submit>       

<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>  

</logic:notEmpty>

</html:form>


</logic:present>

<logic:notPresent name="infoStudentList">
<h2>
<bean:message key="message.editAttendsSetMembers.NoMembersToAdd" />
</h2>
</logic:notPresent>