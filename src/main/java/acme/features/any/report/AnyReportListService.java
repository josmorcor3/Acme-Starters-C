
package acme.features.any.report;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.audits.Report;

@Service
public class AnyReportListService extends AbstractService<Any, Report> {

	@Autowired
	private AnyReportRepository		repository;

	private Collection<Report>	reports;


	@Override
	public void load() {
		this.reports = this.repository.findPublishedReports();
	}

	@Override
	public void authorise() {
		super.setAuthorised(true);
	}

	@Override
	public void unbind() {
		super.getResponse().addData(super.unbindObjects(this.reports, "ticker", "name", "monthsActive", "hours"));
	}

}
