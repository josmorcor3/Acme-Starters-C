
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.inventions.Invention;
import acme.entities.inventions.InventionRepository;

@Validator
public class InventionValidator extends AbstractValidator<ValidInvention, Invention> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private InventionRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidInvention annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Invention invention, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (invention == null)
			result = true;
		else {
			{
				boolean uniqueInvention;
				Invention existingInvention;

				existingInvention = this.repository.findInventionByTicker(invention.getTicker());
				uniqueInvention = existingInvention == null || existingInvention.equals(invention);

				super.state(context, uniqueInvention, "ticker", "acme.validation.duplicated-ticker.message");
			}
			{
				boolean startMomentIsBeforeEndMoment;

				if (invention.getStartMoment() != null && invention.getEndMoment() != null) {
					startMomentIsBeforeEndMoment = MomentHelper.isBefore(invention.getStartMoment(), invention.getEndMoment());
					super.state(context, startMomentIsBeforeEndMoment, "startMoment", "acme.validation.invalid-time-interval.message");
				}
			}
			result = !super.hasErrors(context);
		}

		return result;
	}
}
