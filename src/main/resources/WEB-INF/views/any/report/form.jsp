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
	<acme:form-textbox code="any.reports.form.label.ticker" path="ticker"/>
	<acme:form-textbox code="any.reports.form.label.name" path="name"/>
	<acme:form-textarea code="any.reports.form.label.description" path="description"/>
	<acme:form-moment code="any.reports.form.label.startMoment" path="startMoment"/>
	<acme:form-moment code="any.reports.form.label.endMoment" path="endMoment"/>
	<acme:form-url code="any.reports.form.label.moreInfo" path="moreInfo"/>
	<acme:form-double code="any.reports.form.label.monthsActive" path="monthsActive"/>
	<acme:form-money code="any.reports.form.label.hours" path="hours"/>
	
	<acme:button code="any.reports.form.button.sections" action="/any/section/list?reportId=${id}"/>
	<acme:button code="any.reports.form.button.auditor" action="/any/auditor/show?id=${auditorId}"/>
</acme:form>
