<%--
- form.jsp
-
- Copyright (C) 2012-2026 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:form-textbox code="auditor.report.form.label.ticker" path="ticker"/>	
	<acme:form-textbox code="auditor.report.form.label.name" path="name"/>
	<acme:form-textarea code="auditor.report.form.label.description" path="description"/>
	<acme:form-moment code="auditor.report.form.label.startMoment" path="startMoment"/>
	<acme:form-moment code="auditor.report.form.label.endMoment" path="endMoment"/>
	<acme:form-url code="auditor.report.form.label.moreInfo" path="moreInfo"/>

	<jstl:choose>	 
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:form-double code="auditor.report.form.label.monthsActive" path="monthsActive"/>
			<acme:form-double code="auditor.report.form.label.hours" path="hours"/>
			
			<acme:button code="auditor.report.form.button.sections" action="/auditor/section/list?reportId=${id}"/>			
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:form-double code="auditor.report.form.label.monthsActive" path="monthsActive" readonly="true"/>
			<acme:form-integer code="auditor.report.form.label.hours" path="hours" readonly="true"/>

			<acme:button code="auditor.report.form.button.sections" action="/auditor/section/list?reportId=${id}"/>
			<acme:submit code="auditor.report.form.button.update" action="/auditor/report/update"/>
			<acme:submit code="auditor.report.form.button.delete" action="/auditor/report/delete"/>
			<acme:submit code="auditor.report.form.button.publish" action="/auditor/report/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="auditor.report.form.button.create" action="/auditor/report/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>
