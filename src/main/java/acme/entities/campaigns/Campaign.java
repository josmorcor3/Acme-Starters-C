
package acme.entities.campaigns;

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
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidUrl;
import acme.client.helpers.MomentHelper;
import acme.constraints.ValidCampaign;
import acme.constraints.ValidHeader;
import acme.constraints.ValidText;
import acme.constraints.ValidTicker;
import acme.realms.Spokesperson;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidCampaign
public class Campaign extends AbstractEntity {

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
	@Temporal(TemporalType.TIMESTAMP) //Como hay varias formas de definir el tiempo se usa temporal
	private Date				startMoment;

	@Mandatory
	//Cuando sea un producto real habria que implementar también una restrocción para la acción de crear 
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

	@Transient
	@Autowired
	private CampaignRepository	repository;


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
	@ValidNumber(min = 0)
	@Transient
	public Double getEffort() {
		Double result;
		Double effort = this.repository.totalEffortCampaign(this.getId());

		if (effort == null)
			result = 0.;
		else
			result = effort;
		return result;

	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Spokesperson spokesperson;

}
