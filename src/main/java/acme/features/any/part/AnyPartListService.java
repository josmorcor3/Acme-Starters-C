/*
 * AnyJobListService.java
 *
 * Copyright (C) 2012-2026 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.any.part;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.inventions.Invention;
import acme.entities.inventions.Part;

@Service
public class AnyPartListService extends AbstractService<Any, Part> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyPartRepository	repository;

	private Invention			invention;
	private Collection<Part>	parts;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int inventionId;

		inventionId = super.getRequest().getData("inventionId", int.class);
		this.invention = this.repository.findInventionById(inventionId);
		this.parts = this.repository.findPartsByInventionId(inventionId);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.invention != null && !this.invention.getDraftMode();

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.parts, //
			"name", "description", "cost", "kind");
	}

}
