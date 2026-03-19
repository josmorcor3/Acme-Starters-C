
package acme.entities.inventions;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface InventionRepository extends AbstractRepository {

	@Query("select sum(p.cost.amount) from Part p where p.invention.id = :inventionId")
	Double computeInventionCost(int inventionId);

	@Query("select i from Invention i where i.ticker = :ticker")
	Invention findInventionByTicker(String ticker);

	@Query("select count(p) from Part p where p.invention.id = :inventionId")
	Long computeInventionParts(int inventionId);

	@Query("select distinct p.cost.currency from Part p where p.invention.id = :inventionId")
	List<String> computeInventionCurrencies(int inventionId);
}
