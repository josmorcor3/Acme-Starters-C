
package acme.features.spokesperson.milestone;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.campaigns.Campaign;
import acme.entities.campaigns.Milestone;

@Repository
public interface SpokespersonMilestoneRepository extends AbstractRepository {

	@Query("select m from Milestone m where m.id = :id")
	Milestone getMilestoneById(int id);

	@Query("select m from Milestone m where m.campaign.id = :id ")
	Collection<Milestone> findMilestonesByCampaignId(int id);

	@Query("select s from Strategy s where s.id = :strategyId")
	Campaign findStrategyById(int strategyId);

}
