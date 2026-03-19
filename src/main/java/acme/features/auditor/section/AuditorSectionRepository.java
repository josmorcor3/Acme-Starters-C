/*
 * EmployerDutyRepository.java
 *
 * Copyright (C) 2012-2026 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.auditor.section;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.audits.Report;
import acme.entities.audits.Section;

@Repository
public interface AuditorSectionRepository extends AbstractRepository {

	@Query("select r from Report r where r.id = :id")
	Report findReportById(int id);

	@Query("select s.report from Section s where s.id = :id")
	Report findReportBySectionId(int id);

	@Query("select s from Section s where s.id = :id")
	Section findSectionById(int id);

	@Query("select s from Section s where s.report.id = :reportId")
	Collection<Section> findSectionsByReportId(int reportId);

}
