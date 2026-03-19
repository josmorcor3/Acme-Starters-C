
package acme.features.fundraiser.strategy;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.strategies.Strategy;
import acme.realms.Fundraiser;

public class FundraiserStrategyPublishService extends AbstractService<Fundraiser, Strategy> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FundraiserStrategyRepository	repository;

	private Strategy						strategy;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.strategy = this.repository.findStrategyById(id);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.strategy != null && this.strategy.getDraftMode() && //
			this.strategy.getFundraiser().isPrincipal();

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.strategy, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		super.validateObject(this.strategy);

		// validamos que START MOMENT esté en el futuro en el momento de creación
		if (this.strategy.getStartMoment() != null) {
			boolean startMomentIsInFuture;
			startMomentIsInFuture = MomentHelper.isFuture(this.strategy.getStartMoment());

			super.state(startMomentIsInFuture, "startMoment", "acme.validation.startMoment-is-not-in-the-future.message");
		}
		// validamos que END MOMENT esté en el futuro en el momento de creación
		if (this.strategy.getEndMoment() != null) {
			boolean endMomentIsInFuture;
			endMomentIsInFuture = MomentHelper.isFuture(this.strategy.getEndMoment());

			super.state(endMomentIsInFuture, "endMoment", "acme.validation.endMoment-is-not-in-the-future.message");
		}
		boolean hasTactics;

		Long count = this.repository.computeTacticsByStrategy(this.strategy.getId());
		Long tactics = count == null ? 0 : count;
		hasTactics = Boolean.TRUE.equals(this.strategy.getDraftMode()) && tactics > 0; // pongo el boolean true porque he puesto un dato en errata con el draftmode en null y salta

		super.state(hasTactics, "*", "acme.validation.strategy.published-without-tactics.message");
	}

	@Override
	public void execute() {
		this.strategy.setDraftMode(false);
		this.repository.save(this.strategy);
	}

	@Override
	public void unbind() {

		super.unbindObject(this.strategy, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");

	}

}
