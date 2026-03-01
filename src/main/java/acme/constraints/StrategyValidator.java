
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.strategies.Strategy;
import acme.entities.strategies.StrategyRepository;
import acme.entities.strategies.Tactic;

@Validator
public class StrategyValidator extends AbstractValidator<ValidStrategy, Strategy> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private StrategyRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidStrategy annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Strategy strategy, final ConstraintValidatorContext context) {
		// HINT: job can be null
		assert context != null;

		boolean result;

		if (strategy == null)
			result = true;
		else {
			{
				boolean uniqueStrategy;
				Strategy existingStrategy;

				existingStrategy = this.repository.findStrategyByTicker(strategy.getTicker());
				uniqueStrategy = existingStrategy == null || existingStrategy.equals(strategy);

				super.state(context, uniqueStrategy, "ticker", "acme.validation.job.duplicated-ticker.message");
			}

			{
				boolean hasTactics;

				if (!strategy.getDraftMode()) {
					List<Tactic> tactics = this.repository.findTacticsByStrategy(strategy.getId());
					hasTactics = !tactics.isEmpty();

					super.state(context, hasTactics, "*", "acme.validation.strategy.no-tactics.message");

					boolean validDates;
					if (strategy.getStartMoment() != null && strategy.getEndMoment() != null) {
						validDates = MomentHelper.isBefore(strategy.getStartMoment(), strategy.getEndMoment());

						super.state(context, validDates, "endMoment", "acme.validation.strategy.endMoment-no-after.startMoment.message");
					}

				}

			}

			/*
			 * 
			 * {
			 * boolean correctWorkload;
			 * 
			 * correctWorkload = strategy.isDraftMode() || strategy.getWorkLoad() == 100.00;
			 * 
			 * super.state(context, correctWorkload, "*", "acme.validation.job.workload.message");
			 * }
			 * {
			 * Date minimumDeadline;
			 * boolean correctDeadline;
			 * 
			 * if (strategy.isDraftMode() && strategy.getDeadline() != null) {
			 * minimumDeadline = MomentHelper.deltaFromCurrentMoment(7, ChronoUnit.DAYS);
			 * correctDeadline = MomentHelper.isAfterOrEqual(strategy.getDeadline(), minimumDeadline);
			 * 
			 * super.state(context, correctDeadline, "deadline", "acme.validation.job.deadline.message");
			 * }
			 * }
			 */
			result = !super.hasErrors(context);
		}

		return result;
	}

}
