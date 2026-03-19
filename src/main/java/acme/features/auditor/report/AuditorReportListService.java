
package acme.features.auditor.report;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.audits.Report;
import acme.realms.Auditor;

@Service
public class AuditorReportListService extends AbstractService<Auditor, Report> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorReportRepository	repository;

	private Collection<Report>		reports;

	// AbstractService interface -------------------------------------------


	@Override
	public void authorise() {
		super.setAuthorised(true);
	}

	@Override
	public void load() {
		int auditorId;

		auditorId = super.getRequest().getPrincipal().getActiveRealm().getId();
		this.reports = this.repository.findReportsByAuditorId(auditorId);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.reports, "ticker", "name", "auditor.userAccount.identity.fullName", "description", "startMoment", "endMoment", "moreInfo", "monthsActive", "hours", "draftMode");
	}

}
