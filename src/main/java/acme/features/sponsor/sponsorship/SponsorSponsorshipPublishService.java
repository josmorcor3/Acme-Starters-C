
package acme.features.sponsor.sponsorship;

import java.util.Date;

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

		Date now = new Date();

		boolean startMomentIsInFuture;
		startMomentIsInFuture = MomentHelper.isAfter(this.sponsorship.getStartMoment(), now);

		super.state(startMomentIsInFuture, "startMoment", "acme.validation.startMoment-is-not-in-the-future");

		boolean endMomentIsInFuture;
		endMomentIsInFuture = MomentHelper.isAfter(this.sponsorship.getEndMoment(), now);

		super.state(endMomentIsInFuture, "endMoment", "acme.validation.endMoment-is-not-in-the-future");

		boolean hasDonations;

		Long count = this.repository.computeDonationsBySponsorship(this.sponsorship.getId());
		Long donations = count == null ? 0 : count;
		hasDonations = Boolean.TRUE.equals(this.sponsorship.getDraftMode()) && donations > 0;
		super.state(hasDonations, "*", "acme.validation.sponsorship.published-without-donations.message");
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
