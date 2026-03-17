
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.sponsorships.Donation;

@Validator
public class DonationValidator extends AbstractValidator<ValidDonation, Donation> {

	@Override
	protected void initialise(final ValidDonation annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Donation donation, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;

		if (donation != null) {

			boolean correctCurrency = true;

			if (donation.getMoney() != null && donation.getMoney().getCurrency() != null)
				correctCurrency = donation.getMoney().getCurrency().equals("EUR");
			else
				correctCurrency = false;

			super.state(context, correctCurrency, "money", "acme.validation.invalid-currency.message");

			result = !super.hasErrors(context);
		}

		return result;
	}
}
