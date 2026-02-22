
package acme.entities.campaigns;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface CampaignRepository extends AbstractRepository {

	@Query("select sum(m.effort) from Milestone m where d.campaign.id = :campaignId")
	Double totalEffortCampaign(int campaignId);
}
