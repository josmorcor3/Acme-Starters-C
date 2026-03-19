
package acme.features.auditor.section;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.audits.Section;
import acme.entities.audits.SectionKind;
import acme.realms.Auditor;

@Service
public class AuditorSectionUpdateService extends AbstractService<Auditor, Section> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorSectionRepository	repository;

	private Section						section;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.section = this.repository.findSectionById(id);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.section != null && this.section.getReport().isDraftMode() && this.section.getReport().getAuditor().isPrincipal();

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
		SelectChoices choices = SelectChoices.from(SectionKind.class, this.section.getKind());

		Tuple tuple = super.unbindObject(this.section, "name", "notes", "hours");
		tuple.put("kind", choices.getSelected().getKey());
		tuple.put("kinds", choices);

		tuple.put("reportId", this.section.getReport().getId());
		tuple.put("draftMode", this.section.getReport().isDraftMode());
	}

}
