<%--
- list.jsp
-
- Copyright (C) 2012-2026 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for spokesperson particular
- purposes.  The copyright owner does not offer spokesperson warranties or representations, nor do
- they accept spokesperson liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="spokesperson.milestone.list.label.title" path="title" width="40%"/>
	<acme:list-column code="spokesperson.milestone.list.label.effort" path="effort" width="30%"/>
	<acme:list-column code="spokesperson.milestone.list.label.kind" path="kind" width="30%"/>
</acme:list>

<jstl:if test="${showCreate}">
	<acme:button code="spokesperson.milestone.list.button.create" action="/spokesperson/milestone/create?campaignId=${campaignId}"/>
</jstl:if>

