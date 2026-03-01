
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.campaigns.Campaign;
import acme.entities.campaigns.CampaignRepository;

@Validator
public class CampaignValidator extends AbstractValidator<ValidCampaign, Campaign> {

	// Internal state ----------------------------------------------------------------------------------

	@Autowired
	private CampaignRepository repository;

	// ConstraintValidator interface -----------------------------------------------------------------------------------


	@Override
	protected void initialise(final ValidCampaign annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Campaign campaign, final ConstraintValidatorContext context) {
		// Campaign can be null
		assert context != null;

		boolean result;

		if (campaign == null)
			result = true;
		else {
			{
				boolean uniqueCampaign;
				Campaign existingCampaign;

				existingCampaign = this.repository.findCampaignByTicker(campaign.getTicker());
				uniqueCampaign = existingCampaign == null || existingCampaign.equals(campaign);

				super.state(context, uniqueCampaign, "ticker", "acme.validation.campaign.duplicated-ticker.message");

			}
			{
				boolean publishedCampaignHasAtLeastOneMilestone;
				Long totalMilestones = this.repository.totalMilestonesByCampaign(campaign.getId());

				publishedCampaignHasAtLeastOneMilestone = campaign.getDraftMode() || totalMilestones >= 1;

				super.state(context, publishedCampaignHasAtLeastOneMilestone, "*", "acme.validation.campaign.published-without-milestone.message");

			}
			{
				boolean startMomentIsBeforeEndMoment;

				startMomentIsBeforeEndMoment = MomentHelper.isBefore(campaign.getStartMoment(), campaign.getEndMoment());

				super.state(context, startMomentIsBeforeEndMoment, "time interval", "acme.validation.campaign.invalid-time-interval.message");
			}
			{
				boolean publishedCampaignStartMomentIsInTheFuture;

				publishedCampaignStartMomentIsInTheFuture = campaign.getDraftMode() || MomentHelper.isFuture(campaign.getStartMoment());

				super.state(context, publishedCampaignStartMomentIsInTheFuture, "startMoment", "acme.validation.campaign.invalid-start-moment.message");
			}
			{
				boolean publishedCampaignEndMomentIsInTheFuture;

				publishedCampaignEndMomentIsInTheFuture = campaign.getDraftMode() || MomentHelper.isFuture(campaign.getEndMoment());

				super.state(context, publishedCampaignEndMomentIsInTheFuture, "endMoment", "acme.validation.campaign.invalid-end-moment.message");
			}
			result = !super.hasErrors(context);
		}

		return result;
	}

}
