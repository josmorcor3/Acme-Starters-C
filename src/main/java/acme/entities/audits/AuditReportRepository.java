
package acme.entities.audits;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AuditReportRepository extends AbstractRepository {

	@Query("select round(datediff(ar.endMoment, ar.startMoment) / 30.0, 1) from AuditReport ar where ar.id = :id")
	Double computeMonthsActive(int id);

	@Query("select sum(s.hours) from AuditSection s where s.report.id = :id")
	Long computeHours(int id);

	@Query("select count(s) from AuditSection s where s.report.id = :id")
	Long countSectionsByAuditReportId(int id);
}
