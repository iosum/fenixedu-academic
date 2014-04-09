<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<html:xhtml/>

<bean:define id="style" type="java.lang.String" toScope="request">
	width: 100%; float: left; background-color: #019AD7; background-image: url(<%= request.getContextPath() %>/images/site/bolonha_bck.gif); background-repeat: repeat-x;
</bean:define>

<logic:equal name="actual$site" property="bannerAvailable" value="true">
 	<bean:define id="banner" type="net.sourceforge.fenixedu.domain.UnitSiteBanner" name="actual$site" property="currentBanner" toScope="request"/>
	<bean:define id="style" type="java.lang.String" value="<%= "width: 100%; float: left; " + ((banner.getColor()!=null) ? "background-color: " + banner.getColor() + ";" : "") + (banner.hasBackgroundImage() ? " background-image: url('" + banner.getBackgroundImage().getDownloadUrl() +"'); background-repeat: " + banner.getRepeatType().getRepresentation() + ";" : "") %>" toScope="request"/>
</logic:equal>

<logic:notEqual name="actual$site" property="showBanner" value="true"> 
	<bean:define id="style" type="java.lang.String" value="" toScope="request"/>

	<logic:notEqual name="actual$site" property="showIntroduction" value="true">
		<bean:define id="hideBannerContainer" value="true" toScope="request"/>
	
		<logic:equal name="actual$site" property="showFlags" value="true">
			 <jsp:include page="../../../i18n.jsp"/>
		</logic:equal>
	</logic:notEqual>
</logic:notEqual>

<logic:notPresent name="hideBannerContainer">
	<div class="usitebanner" style="<%= style %>">
			<logic:equal name="actual$site" property="showFlags" value="true">
				 <jsp:include page="../../../i18n.jsp"/>
			</logic:equal>
		<logic:equal name="actual$site" property="showIntroduction" value="true">
			<logic:present name="actual$site" property="description">
				<div class="usiteintrofloated">
					<fr:view name="actual$site" property="description" layout="html"/>
				</div>
			</logic:present>
			<logic:notPresent name="actual$site" property="description">
				<div class="usiteintrofloated" style="background-color: #eeeeee; height: 150px;">
		
				</div>
			</logic:notPresent>
		</logic:equal>
	
		<logic:equal name="actual$site" property="showBanner" value="true">
			<logic:present name="banner">
				<bean:define id="banner" name="banner" type="net.sourceforge.fenixedu.domain.UnitSiteBanner"/>
				<logic:empty name="banner" property="link">
					<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><img src="<%= banner.getMainImage().getDownloadUrl() %>"/>
				</logic:empty>
				<logic:notEmpty name="banner" property="link">
					<a href="<bean:write name="banner" property="link"/>" target="_blank">
						<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><img src="<%= banner.getMainImage().getDownloadUrl() %>"/>
					</a>
				</logic:notEmpty>
			</logic:present>
			<logic:notPresent name="banner">
				<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="http://www.bolonha.ist.eu"><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><img src="<%= request.getContextPath() %>/images/site/bolonha.gif" alt="Com o <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>, entra no melhor ensino superior europeu - www.bolonha.ist.eu" width="420" height="150"/></a>
			</logic:notPresent>
		</logic:equal>
	</div>
	<div style="clear: both;"></div>
</logic:notPresent>

