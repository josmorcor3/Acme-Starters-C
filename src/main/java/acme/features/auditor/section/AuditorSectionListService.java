
package acme.features.auditor.section;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.audits.Report;
import acme.entities.audits.Section;
import acme.realms.Auditor;

@Service
public class AuditorSectionListService extends AbstractService<Auditor, Section> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorSectionRepository	repository;

	private Report						report;
	private Collection<Section>			sections;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int reportId;

		reportId = super.getRequest().getData("reportId", int.class);
		this.report = this.repository.findReportById(reportId);
		this.sections = this.repository.findSectionsByReportId(reportId);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.report != null && (!this.report.isDraftMode() || this.report.getAuditor().isPrincipal());

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		boolean showCreate;

		super.unbindObjects(this.sections, "name", "notes", "hours", "kind");

		showCreate = this.report.isDraftMode() && this.report.getAuditor().isPrincipal();
		super.unbindGlobal("reportId", this.report.getId());
		super.unbindGlobal("showCreate", showCreate);
	}

}
