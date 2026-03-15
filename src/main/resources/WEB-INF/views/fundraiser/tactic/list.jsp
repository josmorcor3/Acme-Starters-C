<%--
- list.jsp
-
- Copyright (C) 2012-2026 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for fundraiser particular
- purposes.  The copyright owner does not offer fundraiser warranties or representations, nor do
- they accept fundraiser liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="fundraiser.tactic.list.label.name" path="name" width="40%"/>
	<acme:list-column code="fundraiser.tactic.list.label.expectedPercentage" path="expectedPercentage" width="30%"/>
	<acme:list-column code="fundraiser.tactic.list.label.kind" path="kind" width="30%"/>
</acme:list>

<jstl:if test="${showCreate}">
	<acme:button code="fundraiser.tactic.list.button.create" action="/fundraiser/tactic/create?strategyId=${strategyId}"/>
</jstl:if>




