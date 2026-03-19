
package acme.features.spokesperson.campaign;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.campaigns.Campaign;
import acme.realms.Spokesperson;

@Service
public class SpokespersonCampaignListService extends AbstractService<Spokesperson, Campaign> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SpokespersonCampaignRepository	repository;

	private Collection<Campaign>			campaigns;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int spokepersonId;

		spokepersonId = super.getRequest().getPrincipal().getActiveRealm().getId();
		this.campaigns = this.repository.findCampaignsBySpokespersonId(spokepersonId);
	}

	@Override
	public void authorise() {
		super.setAuthorised(true);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.campaigns, //
			"ticker", "name", "draftMode", "effort");
	}

}
