
package acme.features.any.milestone;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.campaigns.Campaign;
import acme.entities.campaigns.Milestone;

@Repository
public interface AnyMilestoneRepository extends AbstractRepository {

	@Query("select c from Campaign c where c.id = :campaignId")
	Campaign findCampaignById(int campaignId);

	@Query("select m from Milestone m where m.id = :id")
	Milestone findMilestoneById(int id);

	@Query("select m from Milestone m where m.campaign.id = :campaignId")
	Collection<Milestone> findMilestonesByCampaign(int campaignId);

}
