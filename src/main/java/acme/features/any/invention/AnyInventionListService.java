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

package acme.features.any.invention;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.inventions.Invention;

@Service
public class AnyInventionListService extends AbstractService<Any, Invention> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyInventionRepository	repository;

	private Collection<Invention>	inventions;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		this.inventions = this.repository.findPublishedInventions();
	}

	@Override
	public void authorise() {
		super.setAuthorised(true);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.inventions, //
			"ticker", "name", "inventor.userAccount.identity.fullName", "cost", "monthsActive", "description", "startMoment", "endMoment", "moreInfo");
	}

}
