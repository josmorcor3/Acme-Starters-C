
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.invention.list.label.ticker" path="ticker" width="20%"/>
	<acme:list-column code="any.invention.list.label.name" path="name" width="30%"/>
	<acme:list-column code="any.invention.list.label.inventorName" path="inventor.userAccount.identity.fullName" width="20%"/>
	<acme:list-column code="any.invention.list.label.cost" path="cost" width="20%"/>
	<acme:list-hidden path="monthsActive"/>
	<acme:list-hidden path="description"/>
	<acme:list-hidden path="startMoment"/>
	<acme:list-hidden path="endMoment"/>
	<acme:list-hidden path="moreInfo"/>
</acme:list>
