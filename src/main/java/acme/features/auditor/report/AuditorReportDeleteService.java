
package acme.features.auditor.report;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.audits.Report;
import acme.entities.audits.Section;
import acme.realms.Auditor;

@Service
public class AuditorReportDeleteService extends AbstractService<Auditor, Report> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorReportRepository	repository;

	private Report					report;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.report = this.repository.findReportById(id);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.report != null && this.report.isDraftMode() && this.report.getAuditor().isPrincipal();

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		;
	}

	@Override
	public void validate() {
		;
	}

	@Override
	public void execute() {
		Collection<Section> sections;

		sections = this.repository.findSectionsByReportId(this.report.getId());
		this.repository.deleteAll(sections);
		this.repository.delete(this.report);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.report, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
	}

}
