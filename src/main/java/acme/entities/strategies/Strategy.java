
package acme.entities.strategies;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoment.Constraint;
import acme.client.components.validation.ValidUrl;
import acme.client.helpers.MomentHelper;
import acme.constraints.ValidHeader;
import acme.constraints.ValidStrategy;
import acme.constraints.ValidText;
import acme.constraints.ValidTicker;
import acme.realms.Fundraiser;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidStrategy
public class Strategy extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidTicker
	@Column(unique = true)
	private String				ticker;

	@Mandatory
	@ValidHeader
	@Column
	private String				name;

	@Mandatory
	@ValidText
	@Column
	private String				description;

	@Mandatory
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				startMoment;

	@Mandatory
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				endMoment;

	@Optional
	@ValidUrl
	@Column
	private String				moreInfo;

	@Mandatory
	@Valid
	@Column
	private Boolean				draftMode;

	// Derived attributes -----------------------------------------------------

	@Mandatory
	@Valid
	@Transient
	@Autowired
	private StrategyRepository	repository;


	@Mandatory
	@Valid
	@Transient
	public Double getMonthsActive() {
		if (this.startMoment == null || this.endMoment == null)
			return 0.;
		Double months = MomentHelper.computeDifference(this.startMoment, this.endMoment, ChronoUnit.MONTHS);
		return Math.round(months * 10) / 10.;
	}

	@Mandatory
	@Valid
	@Transient
	public Double getExpectedPercentage() {
		Double result;
		Double total = this.repository.totalExpectedPercentagesTactics(this.getId());
		result = total == null ? 0. : total;
		return result;
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Fundraiser fundraiser;

}
