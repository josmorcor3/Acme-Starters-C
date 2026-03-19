
package acme.features.any.report;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractController;
import acme.entities.audits.Report;

@Controller
public class AnyReportController extends AbstractController<Any, Report> {

	// Constructors -----------------------------------------------------------

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("list", AnyReportListService.class);
		super.addBasicCommand("show", AnyReportShowService.class);
	}
}
