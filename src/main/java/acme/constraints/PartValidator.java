
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.inventions.Part;

@Validator
public class PartValidator extends AbstractValidator<ValidPart, Part> {

	// Internal state ---------------------------------------------------------

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidPart annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Part part, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (part == null)
			result = true;
		else {
			{
				boolean correctCurrency;

				if (part.getCost() != null && part.getCost().getCurrency() != null) {
					correctCurrency = part.getCost().getCurrency().equals("EUR");
					super.state(context, correctCurrency, "cost", "acme.validation.invalid-currency.message");
				}

			}
			result = !super.hasErrors(context);
		}

		return result;
	}
}
