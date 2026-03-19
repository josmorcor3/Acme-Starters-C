
package acme.features.fundraiser.tactic;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.strategies.Strategy;
import acme.entities.strategies.Tactic;
import acme.entities.strategies.TacticKind;
import acme.realms.Fundraiser;

public class FundraiserTacticCreateService extends AbstractService<Fundraiser, Tactic> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FundraiserTacticRepository	repository;

	private Tactic						tactic;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int strategyId;
		Strategy strategy;

		strategyId = super.getRequest().getData("strategyId", int.class);
		strategy = this.repository.findStrategyById(strategyId);

		this.tactic = super.newObject(Tactic.class);
		this.tactic.setName("");
		this.tactic.setNotes("");
		this.tactic.setExpectedPercentage(0.00);
		this.tactic.setStrategy(strategy);
	}

	@Override
	public void authorise() {
		boolean status;
		int strategyId;
		Strategy strategy;

		strategyId = super.getRequest().getData("strategyId", int.class);
		strategy = this.repository.findStrategyById(strategyId);
		status = strategy != null && //
			this.tactic.getStrategy().getDraftMode() && this.tactic.getStrategy().getFundraiser().isPrincipal();

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.tactic, "name", "notes", "expectedPercentage", "kind");
	}

	@Override
	public void validate() {
		super.validateObject(this.tactic);
	}

	@Override
	public void execute() {
		this.repository.save(this.tactic);
	}

	@Override
	public void unbind() {
		SelectChoices choices;
		Tuple tuple;

		choices = SelectChoices.from(TacticKind.class, this.tactic.getKind());

		tuple = super.unbindObject(this.tactic, "name", "notes", "expectedPercentage", "kind");
		tuple.put("strategyId", super.getRequest().getData("strategyId", int.class));
		tuple.put("draftMode", this.tactic.getStrategy().getDraftMode());

		tuple.put("kind", choices.getSelected().getKey());
		tuple.put("kinds", choices);
	}

}
