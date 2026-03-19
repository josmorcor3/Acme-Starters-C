
package acme.features.spokesperson.milestone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.campaigns.Milestone;
import acme.entities.campaigns.MilestoneKind;
import acme.realms.Spokesperson;

@Service
public class SpokespersonMilestoneShowService extends AbstractService<Spokesperson, Milestone> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SpokespersonMilestoneRepository	repository;

	private Milestone						milestone;

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

		status = this.milestone != null && // 
			(this.milestone.getCampaign().getSpokesperson().isPrincipal() || //
				!this.milestone.getCampaign().getDraftMode());

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		SelectChoices choices;
		Tuple tuple;

		choices = SelectChoices.from(MilestoneKind.class, this.milestone.getKind());

		tuple = super.unbindObject(this.milestone, "title", "achievements", "effort");
		tuple.put("kind", choices.getSelected().getKey());
		tuple.put("kinds", choices);

		tuple.put("campaignId", this.milestone.getCampaign().getId());
		tuple.put("draftMode", this.milestone.getCampaign().getDraftMode());
	}

}
