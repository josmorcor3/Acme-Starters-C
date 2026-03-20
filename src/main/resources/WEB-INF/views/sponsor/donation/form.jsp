<%--
- form.jsp
-
- Copyright (C) 2012-2026 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this artefact. It has been tested carefully, but it is not guaranteed for sponsor particular
- purposes.  The copyright owner does not offer sponsor warranties or representations, nor do
- they accept sponsor liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:form-textbox code="sponsor.donation.form.label.name" path="name"/>
	<acme:form-textarea code="sponsor.donation.form.label.notes" path="notes"/>
	<acme:form-money code="sponsor.donation.form.label.money" path="money"/>
	<acme:form-select code="sponsor.donation.form.label.kind" path="kind" choices="${kinds}"/>

	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete') && draftMode == true}">
			<acme:submit code="sponsor.donation.form.button.update" action="/sponsor/donation/update"/>
			<acme:submit code="sponsor.donation.form.button.delete" action="/sponsor/donation/delete"/>
		</jstl:when>
		
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="sponsor.donation.form.button.create" action="/sponsor/donation/create?sponsorshipId=${sponsorshipId}"/>
		</jstl:when>		
	</jstl:choose>
	

</acme:form>