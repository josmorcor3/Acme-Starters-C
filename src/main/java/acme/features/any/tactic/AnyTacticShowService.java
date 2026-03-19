
package acme.features.any.tactic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.principals.Any;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.strategies.Tactic;
import acme.entities.strategies.TacticKind;

@Service
public class AnyTacticShowService extends AbstractService<Any, Tactic> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyTacticRepository	repository;

	private Tactic				tactic;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.tactic = this.repository.getTacticById(id);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.tactic != null && !this.tactic.getStrategy().getDraftMode();

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		SelectChoices choices;
		Tuple tuple;

		choices = SelectChoices.from(TacticKind.class, this.tactic.getKind());

		tuple = super.unbindObject(this.tactic, "name", "notes", "expectedPercentage");
		tuple.put("kind", choices.getSelected().getKey());
		tuple.put("kinds", choices);
	}

}
