
package acme.features.inventor.invention;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.inventions.Invention;
import acme.realms.Inventor;

@Service
public class InventorInventionPublishService extends AbstractService<Inventor, Invention> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private InventorInventionRepository	repository;

	private Invention					invention;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.invention = this.repository.findInventionById(id);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.invention != null && this.invention.getDraftMode() && this.invention.getInventor().isPrincipal();

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		super.validateObject(this.invention);

		{
			boolean isNotPublished;

			isNotPublished = this.invention.getDraftMode();

			super.state(isNotPublished, "*", "acme.validation.already-published.message");
		}
		{
			if (this.invention.getStartMoment() != null) {
				boolean startMomentIsFuture;

				startMomentIsFuture = MomentHelper.isFuture(this.invention.getStartMoment());
				super.state(startMomentIsFuture, "startMoment", "acme.validation.startMoment-is-not-in-the-future.message");
			}
		}
		{
			if (this.invention.getEndMoment() != null) {
				boolean endMomentIsFuture;

				endMomentIsFuture = MomentHelper.isFuture(this.invention.getEndMoment());
				super.state(endMomentIsFuture, "endMoment", "acme.validation.endMoment-is-not-in-the-future.message");
			}
		}
		{
			boolean hasAtLeastOnePart;

			Long numberOfParts = this.repository.computeInventionParts(this.invention.getId());

			hasAtLeastOnePart = numberOfParts > 0;

			super.state(hasAtLeastOnePart, "*", "acme.validation.invention.published-without-parts.message");
		}
	}

	@Override
	public void execute() {
		this.invention.setDraftMode(false);
		this.repository.save(this.invention);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
	}

}
