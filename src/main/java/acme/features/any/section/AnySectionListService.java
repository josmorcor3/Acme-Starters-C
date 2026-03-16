
package acme.features.any.section;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.audits.Report;
import acme.entities.audits.Section;

public class AnySectionListService extends AbstractService<Any, Section> {

	@Autowired
	private AnySectionRepository	repository;

	private Report					report;
	private Collection<Section>		sections;


	@Override
	public void load() {
		int reportId;

		reportId = super.getRequest().getData("reportId", int.class);
		this.report = this.repository.findReportById(reportId);
		this.sections = this.repository.findSectionsByReport(reportId);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.report != null && !this.report.isDraftMode();

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.sections, "name", "notes", "hours", "kind");
	}

}
