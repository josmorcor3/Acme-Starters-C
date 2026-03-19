
package acme.features.any.section;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.audits.Report;
import acme.entities.audits.Section;

@Repository
public interface AnySectionRepository extends AbstractRepository {

	@Query("select r from Report r where r.id = :reportId")
	Report findReportById(int reportId);

	@Query("select s from Section s where s.id = :sectionId")
	Section findSectionById(int sectionId);

	@Query("select s from Section s where s.report.id = :reportId")
	Collection<Section> findSectionsByReport(int reportId);

}
