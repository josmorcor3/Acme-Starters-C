
package acme.entities.audits;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface ReportRepository extends AbstractRepository {

	@Query("select sum(s.hours) from Section s where s.report.id = :id")
	Integer computeHours(int id);

	@Query("select r from Report r where r.ticker = :ticker")
	Report findReportByTicker(String ticker);

	@Query("select count(s) from Section s where s.report.id = :reportId")
	Long computeReportSections(int reportId);

}
