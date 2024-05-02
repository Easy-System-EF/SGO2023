package gui.sgomodel.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Adiantamento extends Funcionario implements Serializable{

	private static final long serialVersionUID = 1L;
 
	private Integer numeroAdi;
	private Date dataAdi;
	private Integer funAdi;
	private String nomeFunAdi;
	private String cargoAdi;
	private String situacaoAdi;
	private Double adiantamentoAdi;
	private Integer mesAdi;
	private Integer anoAdi;

	public Adiantamento() {
	}

	public Adiantamento(Integer numeroAdi, Date dataAdi, Integer funAdi, String nomeFunAdi,  String cargoAdi,
			Double adiantamentoAdi,  String situacaoAdi, Integer mesAdi, Integer anoAdi) {
		this.numeroAdi = numeroAdi;
		this.dataAdi = dataAdi;
		this.funAdi = funAdi;
		this.nomeFunAdi = nomeFunAdi;
		this.cargoAdi = cargoAdi;
		this.situacaoAdi = situacaoAdi;
		this.adiantamentoAdi = adiantamentoAdi;
		this.mesAdi = mesAdi;
		this.anoAdi = anoAdi;
	}

	public Integer getNumeroAdi() {
		return numeroAdi;
	}

	public void setNumeroAdi(Integer numeroAdi) {
		this.numeroAdi = numeroAdi;
	}

	public Date getDataAdi() {
		return dataAdi;
	}

	public void setDataAdi(Date dataAdi) {
		this.dataAdi = dataAdi;
	}

	public Integer getFunAdi() {
		return funAdi;
	}

	public void setFunAdi(Integer funAdi) {
		this.funAdi = funAdi;
	}

	public String getNomeFunAdi() {
		return nomeFunAdi;
	}

	public void setNomeFunAdi(String nomeFunAdi) {
		this.nomeFunAdi = nomeFunAdi;
	}

	public String getCargoAdi() {
		return cargoAdi;
	}

	public void setCargoAdi(String cargoAdi) {
		this.cargoAdi = cargoAdi;
	}

	public String getSituacaoAdi() {
		return situacaoAdi;
	}

	public void setSituacaoAdi(String situacaoAdi) {
		this.situacaoAdi = situacaoAdi;
	}

	public Double getAdiantamentoAdi() {
		return adiantamentoAdi;
	}

	public void setAdiantamentoAdi(Double adiantamentoAdi) {
		this.adiantamentoAdi = adiantamentoAdi;
	}

	public Integer getMesAdi() {
		return mesAdi;
	}

	public void setMesAdi(Integer mesAdi) {
		this.mesAdi = mesAdi;
	}

	public Integer getAnoAdi() {
		return anoAdi;
	}

	public void setAnoAdi(Integer anoAdi) {
		this.anoAdi = anoAdi;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(numeroAdi);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Adiantamento other = (Adiantamento) obj;
		return Objects.equals(numeroAdi, other.numeroAdi);
	}

	@Override
	public String toString() {
		return "Adiantamento [numeroAdi=" + numeroAdi + ", dataAdi=" + dataAdi + ", funAdi=" + funAdi + ", nomeFunAdi="
				+ nomeFunAdi + ", cargoAdi=" + cargoAdi + ", situacaoAdi=" + situacaoAdi + ", adiantamentoAdi="
				+ adiantamentoAdi + ", mesAdi=" + mesAdi + ", anoAdi=" + anoAdi + "]";
	}
}