
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
				boolean publishedInventionHasAtLeastOnePart;
				Long numberOfParts = this.repository.computeInventionParts(invention.getId());

				publishedInventionHasAtLeastOnePart = invention.getDraftMode() || numberOfParts > 0;

				super.state(context, publishedInventionHasAtLeastOnePart, "*", "acme.validation.invention.published-without-parts.message");
			}
			{
				boolean startMomentIsNotNull;
				startMomentIsNotNull = invention.getStartMoment() != null;
				super.state(context, startMomentIsNotNull, "startMoment", "acme.validation.null-start-moment.message");
			}
			{
				boolean endMomentIsNotNull;
				endMomentIsNotNull = invention.getEndMoment() != null;
				super.state(context, endMomentIsNotNull, "endMoment", "acme.validation.null-end-moment.message");
			}
			{
				boolean startMomentIsBeforeEndMoment;

				startMomentIsBeforeEndMoment = MomentHelper.isBefore(invention.getStartMoment(), invention.getEndMoment());

				super.state(context, startMomentIsBeforeEndMoment, "time interval", "acme.validation.invalid-time-interval.message");
			}
			{
				boolean correctCurrency;

				correctCurrency = this.repository.computeInventionCurrencies(invention.getId()).stream().allMatch(c -> c.equals("EUR"));

				super.state(context, correctCurrency, "money currency", "acme.validation.invalid-currency.message");
			}
			result = !super.hasErrors(context);
		}

		return result;
	}
}
