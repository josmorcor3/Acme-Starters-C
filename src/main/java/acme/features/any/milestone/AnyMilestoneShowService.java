
package acme.features.any.milestone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.principals.Any;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.campaigns.Milestone;
import acme.entities.campaigns.MilestoneKind;

@Service
public class AnyMilestoneShowService extends AbstractService<Any, Milestone> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyMilestoneRepository	repository;

	private Milestone				milestone;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.milestone = this.repository.findMilestoneById(id);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.milestone != null && !this.milestone.getCampaign().getDraftMode();

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		SelectChoices choices = SelectChoices.from(MilestoneKind.class, this.milestone.getKind());

		Tuple tuple = super.unbindObject(this.milestone, "title", "achievements", "effort", "kind");

		tuple.put("kind", choices.getSelected().getKey());
		tuple.put("kinds", choices);

	}

}
