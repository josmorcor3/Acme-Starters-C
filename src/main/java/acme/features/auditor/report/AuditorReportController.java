
package acme.features.auditor.report;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.audits.Report;
import acme.realms.Auditor;

@Controller
public class AuditorReportController extends AbstractController<Auditor, Report> {

	// Constructors -----------------------------------------------------------

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("list", AuditorReportListService.class);
		super.addBasicCommand("show", AuditorReportShowService.class);
		super.addBasicCommand("create", AuditorReportCreateService.class);
		super.addBasicCommand("update", AuditorReportUpdateService.class);
		super.addBasicCommand("delete", AuditorReportDeleteService.class);

		super.addCustomCommand("publish", "update", AuditorReportPublishService.class);
	}

}
