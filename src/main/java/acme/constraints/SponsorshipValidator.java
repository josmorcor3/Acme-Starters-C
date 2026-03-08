
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.sponsorships.Sponsorship;
import acme.entities.sponsorships.SponsorshipRepository;

@Validator
public class SponsorshipValidator extends AbstractValidator<ValidSponsorship, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorshipRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidSponsorship annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Sponsorship sponsorship, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (sponsorship == null)
			result = true;
		else {
			{
				boolean uniqueSponsorship;
				Sponsorship existingSponsorship;

				existingSponsorship = this.repository.findSponsorshipByTicker(sponsorship.getTicker());
				uniqueSponsorship = existingSponsorship == null || existingSponsorship.equals(sponsorship);

				super.state(context, uniqueSponsorship, "ticker", "acme.validation.duplicated-ticker.message");
			}

			{
				boolean hasDonations;

				Long count = this.repository.findDonationsBySponsorshipId(sponsorship.getId());
				Long donations = count == null ? 0 : count;
				hasDonations = sponsorship.getDraftMode() || donations > 0;

				super.state(context, hasDonations, "*", "acme.validation.sponsorship.published-without-donations.message");
			}

			{
				boolean startMomentIsBeforeEndMoment;
				if (sponsorship.getStartMoment() != null && sponsorship.getEndMoment() != null) {
					startMomentIsBeforeEndMoment = MomentHelper.isBefore(sponsorship.getStartMoment(), sponsorship.getEndMoment());

					super.state(context, startMomentIsBeforeEndMoment, "endMoment", "acme.validation.invalid-time-interval.message");
				}

			}

			{
				boolean correctCurrency;

				correctCurrency = this.repository.computeSponsorshipCurrencies(sponsorship.getId()).stream().allMatch(c -> c.equals("EUR"));

				super.state(context, correctCurrency, "money currency", "acme.validation.invalid-currency.message");
			}

			result = !super.hasErrors(context);

		}

		return result;
	}

}
