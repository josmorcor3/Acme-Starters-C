
package acme.features.any.section;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractController;
import acme.entities.audits.Section;

@Controller
public class AnySectionController extends AbstractController<Any, Section> {

	// Constructors -----------------------------------------------------------

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("list", AnySectionListService.class);
		super.addBasicCommand("show", AnySectionShowService.class);
	}

}
