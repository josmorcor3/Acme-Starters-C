
package acme.entities.audits;

import java.time.Duration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Moment;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoment.Constraint;
import acme.client.components.validation.ValidUrl;
import acme.client.helpers.MomentHelper;
import acme.realms.Auditor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AuditReport extends AbstractEntity {
	// Serialisation version --------------------------------------------------

	private static final long		serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	// @ValidTicker
	@Column(unique = true)
	private String					ticker;

	@Mandatory
	// @ValidHeader
	@Column
	private String					name;

	@Mandatory
	// @ValidText
	@Column
	private String					description;

	@Mandatory
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	@Column
	private Moment					startMoment;

	@Mandatory
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	@Column
	private Moment					endMoment;

	@Optional
	@ValidUrl
	@Column
	private String					moreInfo;

	@Mandatory
	// HINT: @Valid by default.
	@Column
	private boolean					draftMode;

	// Derived attributes -----------------------------------------------------

	@Transient
	@Autowired
	private AuditReportRepository	repository;


	@Transient
	public Double getMonthsActive() {
		Duration duration = MomentHelper.computeDuration(this.startMoment, this.endMoment);
		double months = duration.toDays() / 30.;
		return Math.round(months * 10) / 10.;
	}

	@Transient
	public Integer getHours() {
		Integer result = this.repository.computeHours(this.getId());
		return result == null ? 0 : result;
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne
	private Auditor auditor;
}
