
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

		if (this.sponsorship.getStartMoment() != null) {
			boolean startMomentIsInFuture;
			startMomentIsInFuture = MomentHelper.isFuture(this.sponsorship.getStartMoment());

			super.state(startMomentIsInFuture, "startMoment", "acme.validation.startMoment-is-not-in-the-future");
		}

		if (this.sponsorship.getEndMoment() != null) {
			boolean endMomentIsInFuture;
			endMomentIsInFuture = MomentHelper.isFuture(this.sponsorship.getEndMoment());

			super.state(endMomentIsInFuture, "endMoment", "acme.validation.endMoment-is-not-in-the-future");
		}
		boolean hasDonation;

		Long count = this.repository.computeDonationsBySponsorship(this.sponsorship.getId());
		Long donation = count == null ? 0 : count;
		hasDonation = Boolean.TRUE.equals(this.sponsorship.getDraftMode()) && donation > 0;

		super.state(hasDonation, "*", "acme.validation.sponsorship.published-without-donations.message");
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
