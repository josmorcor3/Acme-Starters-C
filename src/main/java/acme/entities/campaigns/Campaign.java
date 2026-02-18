
package acme.entities.campaigns;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Moment;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoment.Constraint;
import acme.client.components.validation.ValidUrl;
import acme.realms.Spokesperson;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Campaign extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	// @ValidTicker
	@Column(unique = true)
	private String				ticker;

	@Mandatory
	// @ValidHeader
	@Column
	private String				name;

	@Mandatory
	// @ValidText
	@Column
	private String				description;

	@Mandatory
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	@Temporal(TemporalType.TIMESTAMP) //Como hay varias formas de definir el tiempo se usa temporal
	private Moment				startMoment;

	@Mandatory
	//Cuando sea un producto real habria que implementar también una restrocción para la acción de crear 
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	@Temporal(TemporalType.TIMESTAMP)
	private Moment				endMoment;

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
	//Hay que calcular los meses de diferencia entre start y end pero creo que hay que pasarlos primero a otro tipo
	// para poder aplicar la función que me realiza esa operación
	public Double getMonthsActive() {
		double result = 0.0;
		return result;
	}

	@Transient
	//Tengo que acceder al atributo de effort de la entidad Milestone pero no se como hacerlo si tengo la relación
	//mapeada en la clase Milestone, tendría que añadir aqui también la relación???
	public Double getEffort() {
		double result = 0.0;
		return result;

	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Spokesperson spokesperson;

}
