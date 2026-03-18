
package acme.features.any.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.audits.Report;

@Service
public class AnyReportShowService extends AbstractService<Any, Report> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyReportRepository	repository;

	private Report				report;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.report = this.repository.findReportById(id);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.report != null && !this.report.isDraftMode();

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {

		Tuple tuple = super.unbindObject(this.report, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "monthsActive", "hours");

		tuple.put("auditorId", this.report.getAuditor().getId());

		super.getResponse().addData(tuple);
	}

}
