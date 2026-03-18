
package acme.features.any.report;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.audits.Report;

@Repository
public interface AnyReportRepository extends AbstractRepository {

	@Query("select ar from Report ar where ar.draftMode = false")
	Collection<Report> findPublishedReports();

	@Query("select ar from Report ar where ar.id = :id")
	Report findReportById(int id);

}
