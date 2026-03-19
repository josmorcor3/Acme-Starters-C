
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.audits.Report;
import acme.entities.audits.ReportRepository;

@Validator
public class ReportValidator extends AbstractValidator<ValidReport, Report> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ReportRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidReport annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Report report, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (report == null)
			result = true;
		else {
			{
				boolean uniqueReport;
				Report existingReport;

				existingReport = this.repository.findReportByTicker(report.getTicker());
				uniqueReport = existingReport == null || existingReport.equals(report);

				super.state(context, uniqueReport, "ticker", "acme.validation.duplicated-ticker.message");
			}
			{
				boolean publishedReportHasAtLeastOneSection;
				Long numberOfSections = this.repository.computeReportSections(report.getId());

				publishedReportHasAtLeastOneSection = report.isDraftMode() || numberOfSections > 0;

				super.state(context, publishedReportHasAtLeastOneSection, "*", "acme.validation.report.published-without-section.message");
			}
			{
				boolean startMomentIsBeforeEndMoment;

				if (report.getStartMoment() != null && report.getEndMoment() != null) {
					startMomentIsBeforeEndMoment = MomentHelper.isBefore(report.getStartMoment(), report.getEndMoment());
					super.state(context, startMomentIsBeforeEndMoment, "startMoment", "acme.validation.invalid-time-interval.message");
				}
			}
			{

			}
			result = !super.hasErrors(context);
		}

		return result;
	}
}
