
package acme.features.sponsor.sponsorship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.sponsorships.Sponsorship;
import acme.realms.Sponsor;

@Service
public class SponsorSponsorshipPublishService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorSponsorshipRepository	repository;

	private Sponsorship						sponsorship;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.sponsorship = this.repository.findSponsorshipById(id);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.sponsorship != null && this.sponsorship.getDraftMode() && //
			this.sponsorship.getSponsor().isPrincipal();

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.sponsorship, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		super.validateObject(this.sponsorship);

		{
			boolean isNotPublished;

			isNotPublished = this.sponsorship.getDraftMode();

			super.state(isNotPublished, "*", "acme.validation.already-published.message");
		}
		{
			boolean startMomentIsFuture;

			startMomentIsFuture = MomentHelper.isFuture(this.sponsorship.getStartMoment());
			super.state(startMomentIsFuture, "startMoment", "acme.validation.start-moment-is-not-in-the-future.message");
		}
		{
			boolean endMomentIsFuture;

			endMomentIsFuture = MomentHelper.isFuture(this.sponsorship.getEndMoment());
			super.state(endMomentIsFuture, "endMoment", "acme.validation.end-moment-is-not-in-the-future.message");
		}
		{
			boolean hasAtLeastOnePart;

			Long numberOfParts = this.repository.computeDonationsBySponsorship(this.sponsorship.getId());

			hasAtLeastOnePart = numberOfParts > 0;

			super.state(hasAtLeastOnePart, "*", "acme.validation.sponsorship.published-without-parts.message");
		}
	}

	@Override
	public void execute() {
		this.sponsorship.setDraftMode(false);
		this.repository.save(this.sponsorship);
	}

	@Override
	public void unbind() {

		super.unbindObject(this.sponsorship, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");

	}

}
