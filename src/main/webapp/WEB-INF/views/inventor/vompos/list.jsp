<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="urn:jsptagdir:/WEB-INF/tags"%>

<acme:list>
	<acme:list-column code="inventor.vompo.list.label.subject" path="subject" width="20%"/>
	<acme:list-column code="inventor.vompo.list.label.startPeriod" path="startPeriod" width="30%"/>
	<acme:list-column code="inventor.vompo.list.label.finishPeriod" path="finishPeriod" width="30%"/>
</acme:list>