<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="urn:jsptagdir:/WEB-INF/tags"%>

<acme:form>
	<acme:input-textbox readonly="true" code="inventor.vompo.form.label.code" path="code"/>
	<jstl:if test="${command != 'create'}">
		<acme:input-textbox code="inventor.vompo.form.label.creationMoment" path="creationMoment" readonly="true"/>
	</jstl:if>
	<acme:input-textbox code="inventor.vompo.form.label.startPeriod" path="startPeriod" />
	<acme:input-textbox code="inventor.vompo.form.label.finishPeriod" path="finishPeriod" />
	<acme:input-textbox code="inventor.vompo.form.label.subject" path="subject"/>
	<acme:input-textbox code="inventor.vompo.form.label.explanation" path="explanation"/>
	<acme:input-money code="inventor.vompo.form.label.income" path="share"/>
	<acme:input-url code="inventor.vompo.form.label.moreInfo" path="moreInfo"/>

	<jstl:choose>
	    <jstl:when test="${acme:anyOf(command, 'show, update, delete')}">
	        <acme:submit code="inventor.vompo.form.button.update" action="/inventor/vompo/update"/>
	        <acme:submit code="inventor.vompo.form.button.delete" action="/inventor/vompo/delete"/>
	    </jstl:when>
	    <jstl:when test="${command == 'create'}">
	    	<acme:hidden-data path="itemId"/>
	        <acme:submit code="inventor.vompo.form.button.create" action="/inventor/vompo/create"/>
	    </jstl:when>
	</jstl:choose>
</acme:form>