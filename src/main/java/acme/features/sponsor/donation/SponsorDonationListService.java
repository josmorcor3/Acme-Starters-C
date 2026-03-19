
package acme.features.sponsor.donation;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.sponsorships.Donation;
import acme.entities.sponsorships.Sponsorship;
import acme.realms.Sponsor;

@Service
public class SponsorDonationListService extends AbstractService<Sponsor, Donation> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorDonationRepository	repository;

	private Collection<Donation>		donations;
	private Sponsorship					sponsorship;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int sponsorshipId;

		sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		this.sponsorship = this.repository.findSponsorshipById(sponsorshipId);
		this.donations = this.repository.findDonationsBySponsorshipId(sponsorshipId);
	}

	@Override
	public void authorise() {
		boolean status;
		status = this.sponsorship != null && (this.sponsorship.getSponsor().isPrincipal() || //
			!this.sponsorship.getDraftMode());

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		boolean showCreate;

		super.unbindObjects(this.donations, //
			"name", "notes", "money", "kind");

		showCreate = this.sponsorship.getDraftMode() && this.sponsorship.getSponsor().isPrincipal();
		super.unbindGlobal("showCreate", showCreate);
		super.unbindGlobal("sponsorshipId", this.sponsorship.getId());
	}

}
