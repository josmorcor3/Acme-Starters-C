
package acme.features.spokesperson.campaign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.campaigns.Campaign;
import acme.realms.Spokesperson;

@Service
public class SpokespersonCampaignPublishService extends AbstractService<Spokesperson, Campaign> {

	@Autowired
	private SpokespersonCampaignRepository	repository;

	private Campaign						campaign;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.campaign = this.repository.findCampaignById(id);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.campaign != null && this.campaign.getDraftMode() && //
			this.campaign.getSpokesperson().isPrincipal();

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.campaign, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		super.validateObject(this.campaign);

		if (this.campaign.getStartMoment() != null) {
			boolean startMomentIsInFuture;
			startMomentIsInFuture = MomentHelper.isFuture(this.campaign.getStartMoment());

			super.state(startMomentIsInFuture, "startMoment", "acme.validation.startMoment-is-not-in-the-future.message");
		}

		if (this.campaign.getEndMoment() != null) {
			boolean endMomentIsInFuture;
			endMomentIsInFuture = MomentHelper.isFuture(this.campaign.getEndMoment());

			super.state(endMomentIsInFuture, "endMoment", "acme.validation.endMoment-is-not-in-the-future.message");
		}
		boolean hasMilestones;

		Long count = this.repository.computeMilestonesByCampaign(this.campaign.getId());
		Long milestones = count == null ? 0 : count;
		hasMilestones = Boolean.TRUE.equals(this.campaign.getDraftMode()) && milestones > 0;

		super.state(hasMilestones, "*", "acme.validation.campaign.published-without-milestone.message");
	}

	@Override
	public void execute() {
		this.campaign.setDraftMode(false);
		this.repository.save(this.campaign);
	}

	@Override
	public void unbind() {

		super.unbindObject(this.campaign, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
	}

}
