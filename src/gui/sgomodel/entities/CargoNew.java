package gui.sgomodel.entities;

import java.io.Serializable;

public class CargoNew implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer codigoCargoNew;
	private String nomeCargoNew;
	private Double salarioCargoNew;
	private Double comissaoCargoNew;
	
	public CargoNew() {
	}

	public CargoNew(Integer codigoCargoNew, String nomeCargoNew, Double salarioCargoNew, Double comissaoCargoNew) {
 		this.codigoCargoNew = codigoCargoNew;
		this.nomeCargoNew = nomeCargoNew;
		this.salarioCargoNew = salarioCargoNew;
		this.comissaoCargoNew = comissaoCargoNew;
	}

	public Integer getCodigoCargoNew() {
		return codigoCargoNew;
	}

	public void setCodigoCargoNew(Integer codigoCargoNew) {
		this.codigoCargoNew = codigoCargoNew;
	}

	public String getNomeCargoNew() {
		return nomeCargoNew;
	}

	public void setNomeCargoNew(String nomeCargoNew) {
		this.nomeCargoNew = nomeCargoNew;
	}

	public Double getSalarioCargoNew() {
		return salarioCargoNew;
	}

	public void setSalarioCargoNew(Double salarioCargoNew) {
		this.salarioCargoNew = salarioCargoNew;
	}

	public Double getComissaoCargoNew() {
		return comissaoCargoNew;
	}

	public void setComissaoCargoNew(Double comissaoCargoNew) {
		this.comissaoCargoNew = comissaoCargoNew;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoCargoNew == null) ? 0 : codigoCargoNew.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CargoNew other = (CargoNew) obj;
		if (codigoCargoNew == null) {
			if (other.codigoCargoNew != null)
				return false;
		} else if (!codigoCargoNew.equals(other.codigoCargoNew))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CargoNew [codigoCar=" + codigoCargoNew + ", nomeCargoNew=" + nomeCargoNew + ", salarioCargoNew=" + salarioCargoNew
				+ ", comissaoCargoNew=" + comissaoCargoNew + "]";
	}
	
}
