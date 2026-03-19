
package acme.features.auditor.section;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.audits.Report;
import acme.entities.audits.Section;
import acme.entities.audits.SectionKind;
import acme.realms.Auditor;

@Service
public class AuditorSectionCreateService extends AbstractService<Auditor, Section> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorSectionRepository	repository;

	private Section						section;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int reportId;
		Report report;

		reportId = super.getRequest().getData("reportId", int.class);
		report = this.repository.findReportById(reportId);

		this.section = super.newObject(Section.class);
		this.section.setName("");
		this.section.setNotes("");
		this.section.setHours(0);

		this.section.setReport(report);
	}

	@Override
	public void authorise() {
		boolean status;
		int reportId;
		Report report;

		reportId = super.getRequest().getData("reportId", int.class);
		report = this.repository.findReportById(reportId);
		status = report != null && this.section.getReport().isDraftMode() && this.section.getReport().getAuditor().isPrincipal();

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.section, "name", "notes", "hours", "kind");
	}

	@Override
	public void validate() {
		super.validateObject(this.section);
	}

	@Override
	public void execute() {
		this.repository.save(this.section);
	}

	@Override
	public void unbind() {
		Tuple tuple = super.unbindObject(this.section, "name", "notes", "hours");

		SelectChoices choices = SelectChoices.from(SectionKind.class, this.section.getKind());
		tuple.put("kind", choices.getSelected().getKey());
		tuple.put("kinds", choices);

		tuple.put("reportId", this.section.getReport().getId());
	}

}
