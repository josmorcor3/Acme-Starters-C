
package acme.features.auditor.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.audits.Report;
import acme.realms.Auditor;

@Service
public class AuditorReportPublishService extends AbstractService<Auditor, Report> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorReportRepository	repository;

	private Report					report;

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

		status = this.report != null && this.report.isDraftMode() && this.report.getAuditor().isPrincipal();

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.report, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		super.validateObject(this.report);

		{
			boolean isNotPublished;

			isNotPublished = this.report.isDraftMode();

			super.state(isNotPublished, "*", "acme.validation.already-published.message");
		}
		{
			if (this.report.getStartMoment() != null) {
				boolean startMomentIsFuture;

				startMomentIsFuture = MomentHelper.isFuture(this.report.getStartMoment());
				super.state(startMomentIsFuture, "startMoment", "acme.validation.startMoment-is-not-in-the-future.message");
			}
		}
		{
			if (this.report.getEndMoment() != null) {
				boolean endMomentIsFuture;

				endMomentIsFuture = MomentHelper.isFuture(this.report.getEndMoment());
				super.state(endMomentIsFuture, "endMoment", "acme.validation.endMoment-is-not-in-the-future.message");
			}
		}
		{
			boolean hasAtLeastOneSection;

			Long numberOfSections = this.repository.computeReportSections(this.report.getId());

			hasAtLeastOneSection = numberOfSections > 0;

			super.state(hasAtLeastOneSection, "*", "acme.validation.report.published-without-sections.message");
		}
	}

	@Override
	public void execute() {
		this.report.setDraftMode(false);
		this.repository.save(this.report);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.report, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
	}

}
