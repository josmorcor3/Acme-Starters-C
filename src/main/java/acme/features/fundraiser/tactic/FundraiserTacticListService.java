
package acme.features.fundraiser.tactic;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.strategies.Strategy;
import acme.entities.strategies.Tactic;
import acme.realms.Fundraiser;

@Service
public class FundraiserTacticListService extends AbstractService<Fundraiser, Tactic> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FundraiserTacticRepository	repository;

	private Collection<Tactic>			tactics;
	private Strategy					strategy;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int strategyId;

		strategyId = super.getRequest().getData("strategyId", int.class);
		this.strategy = this.repository.findStrategyById(strategyId);
		this.tactics = this.repository.findTacticsByStrategyId(strategyId);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.strategy != null && // 
			(this.strategy.getFundraiser().isPrincipal() || //
				!this.strategy.getDraftMode());

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		boolean showCreate;

		super.unbindObjects(this.tactics, //
			"name", "notes", "expectedPercentage", "kind");

		showCreate = this.strategy.getDraftMode() && this.strategy.getFundraiser().isPrincipal();
		super.unbindGlobal("showCreate", showCreate);
		super.unbindGlobal("strategyId", this.strategy.getId());
	}

}
