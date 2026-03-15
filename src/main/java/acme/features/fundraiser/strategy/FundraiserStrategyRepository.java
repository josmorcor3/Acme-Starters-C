
package acme.features.fundraiser.strategy;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.strategies.Strategy;
import acme.entities.strategies.Tactic;

@Repository
public interface FundraiserStrategyRepository extends AbstractRepository {

	@Query("select s from Strategy s where s.fundraiser.id = :fundraiserId")
	Collection<Strategy> findStrategiesByFundraiserId(int fundraiserId);

	@Query("select s from Strategy s where s.id = :strategyId")
	Strategy findStrategyById(int strategyId);

	@Query("select t from Tactic t where t.strategy.id = :id ")
	Collection<Tactic> findTacticsByStrategyId(int id);

	@Query("select count(t) from Tactic t where t.strategy.id = :strategyId")
	Long computeTacticsByStrategy(int strategyId);

}
