package gui.sgomodel.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Comissao implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer numeroCom;
	private Date dataCom;
	private Integer funCom;
	private String nomeFunCom;
	private String cargoCom;
	private String situacaoCom;
	private Integer OSCom;
	private Integer BalcaoCom;
	private Integer mesCom;
	private Integer anoCom;
	private Double percentualCom;
	private Double produtoCom;
	private Double comissaoCom;
	
	public Comissao() {
	}

	public Comissao(Integer numeroCom, Date dataCom, Integer funCom, String nomeFunCom, String cargoCom, String situacaoCom, 
			Integer OSCom, Integer BalcaoCom, Integer mesCom, Integer anoCom,  Double percentualCom, Double produtoCom, 
			Double comissaoCom) {
		this.numeroCom = numeroCom;
		this.dataCom = dataCom;
		this.funCom = funCom;
		this.nomeFunCom = nomeFunCom;
		this.cargoCom = cargoCom;
		this.situacaoCom = situacaoCom;
		this.OSCom = OSCom;
		this.BalcaoCom = BalcaoCom;
		this.mesCom = mesCom;
		this.anoCom = anoCom;
		this.percentualCom = percentualCom;
		this.produtoCom = produtoCom;
		this.comissaoCom = comissaoCom;
	}

	public Integer getNumeroCom() {
		return numeroCom;
	}

	public void setNumeroCom(Integer numeroCom) {
		this.numeroCom = numeroCom;
	}

	public Date getDataCom() {
		return dataCom;
	}

	public void setDataCom(Date dataCom) {
		this.dataCom = dataCom;
	}

	public Integer getFunCom() {
		return funCom;
	}

	public void setFunCom(Integer funCom) {
		this.funCom = funCom;
	}

	public String getNomeFunCom() {
		return nomeFunCom;
	}

	public void setNomeFunCom(String nomeFunCom) {
		this.nomeFunCom = nomeFunCom;
	}

	public String getCargoCom() {
		return cargoCom;
	}

	public void setCargoCom(String cargoCom) {
		this.cargoCom = cargoCom;
	}

	public String getSituacaoCom() {
		return situacaoCom;
	}

	public void setSituacaoCom(String situacaoCom) {
		this.situacaoCom = situacaoCom;
	}

	public Integer getOSCom() {
		return OSCom;
	}

	public void setOSCom(Integer oSCom) {
		OSCom = oSCom;
	}

	public Integer getBalcaoCom() {
		return BalcaoCom;
	}

	public void setBalcaoCom(Integer balcaoCom) {
		BalcaoCom = balcaoCom;
	}

	public Integer getMesCom() {
		return mesCom;
	}

	public void setMesCom(Integer mesCom) {
		this.mesCom = mesCom;
	}

	public Integer getAnoCom() {
		return anoCom;
	}

	public void setAnoCom(Integer anoCom) {
		this.anoCom = anoCom;
	}

	public Double getPercentualCom() {
		return percentualCom;
	}

	public void setPercentualCom(Double percentualCom) {
		this.percentualCom = percentualCom;
	}

	public Double getProdutoCom() {
		return produtoCom;
	}

	public void setProdutoCom(Double produtoCom) {
		this.produtoCom = produtoCom;
	}

	public Double getComissaoCom() {
		return comissaoCom;
	}

	public void setComissaoCom() {
		this.comissaoCom = calculoComissao();
	}
	
	public Double calculoComissao() {
		comissaoCom = (produtoCom * percentualCom) / 100;
		return comissaoCom;
	}

	@Override
	public int hashCode() {
		return Objects.hash(numeroCom);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comissao other = (Comissao) obj;
		return Objects.equals(numeroCom, other.numeroCom);
	}

	@Override
	public String toString() {
		return "Comissao [numeroCom=" + numeroCom + ", dataCom=" + dataCom + ", funCom=" + funCom + ", nomeFunCom="
				+ nomeFunCom + ", cargoCom=" + cargoCom + ", situacaoCom=" + situacaoCom + ", OSCom=" + OSCom
				+ ", BalcaoCom=" + BalcaoCom + ", mesCom=" + mesCom + ", anoCom=" + anoCom + ", percentualCom="
				+ percentualCom + ", produtoCom=" + produtoCom + ", comissaoCom=" + comissaoCom + "]";
	}
}
