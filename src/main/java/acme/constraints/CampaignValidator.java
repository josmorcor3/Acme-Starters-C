
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

				super.state(context, uniqueCampaign, "ticker", "acme.validation.duplicated-ticker.message");
			}

			{
				boolean publishedCampaignHasAtLeastOneMilestone;
				Long totalMilestones = this.repository.totalMilestonesByCampaign(campaign.getId());

				Long milestones = totalMilestones == null ? 0 : totalMilestones;
				publishedCampaignHasAtLeastOneMilestone = Boolean.TRUE.equals(campaign.getDraftMode()) || milestones > 0;

				super.state(context, publishedCampaignHasAtLeastOneMilestone, "*", "acme.validation.campaign.published-without-milestone.message");
			}

			{
				boolean startMomentIsBeforeEndMoment;

				if (campaign.getStartMoment() != null && campaign.getEndMoment() != null) {
					startMomentIsBeforeEndMoment = MomentHelper.isBefore(campaign.getStartMoment(), campaign.getEndMoment());

					super.state(context, startMomentIsBeforeEndMoment, "*", "acme.validation.invalid-time-interval.message");
				}
			}

			result = !super.hasErrors(context);
		}

		return result;
	}
}
