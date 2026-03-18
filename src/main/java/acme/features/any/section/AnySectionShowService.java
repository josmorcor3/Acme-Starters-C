
package acme.features.any.section;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.principals.Any;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.audits.Section;
import acme.entities.audits.SectionKind;

@Service
public class AnySectionShowService extends AbstractService<Any, Section> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnySectionRepository	repository;

	private Section					section;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int sectionId;

		sectionId = super.getRequest().getData("id", int.class);
		this.section = this.repository.findSectionById(sectionId);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.section != null && !this.section.getReport().isDraftMode();

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		SelectChoices choices = SelectChoices.from(SectionKind.class, this.section.getKind());

		Tuple tuple = super.unbindObject(this.section, "name", "notes", "hours", "kind");

		tuple.put("kind", choices.getSelected().getKey());
		tuple.put("kinds", choices);

	}

}
