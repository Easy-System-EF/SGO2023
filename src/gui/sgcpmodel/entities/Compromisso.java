package gui.sgcpmodel.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import gui.sgcpmodel.entities.consulta.ParPeriodo;

 
public class Compromisso implements Serializable {

 	private static final long serialVersionUID = 1L;

 	private Integer idCom;
 	private Integer codigoFornecedorCom;
 	private String nomeFornecedorCom;
   	private Integer nnfCom;
  	private Date dataCom;
  	private Date dataVencimentoCom;
 	private Double valorCom;
 	private Integer parcelaCom;
 	private Integer prazoCom;
 	private Integer situacaoCom;
 	
  	public Fornecedor fornecedor;
 	public TipoConsumo tipoConsumo;
 	public ParPeriodo periodo;
 	
   	public Compromisso() {
	}

	public Compromisso(Integer idCom, Integer codigoFornecedorCom, String nomeFornecedorCom, Integer nnfCom, Date dataCom, 
			Date dataVencimentoCom, Double valorCom, Integer parcelaCom, Integer prazoCom,
			Fornecedor fornecedor, TipoConsumo tipoConsumo, ParPeriodo periodo, Integer situacaoCom) {
		this.idCom = idCom;
		this.codigoFornecedorCom = codigoFornecedorCom; 
		this.nomeFornecedorCom = nomeFornecedorCom;
   		this.nnfCom = nnfCom;
		this.dataCom = dataCom;
		this.dataVencimentoCom = dataVencimentoCom;
		this.valorCom = valorCom;
		this.parcelaCom = parcelaCom;
		this.prazoCom = prazoCom;
		this.fornecedor = fornecedor;
		this.tipoConsumo = tipoConsumo;  
		this.periodo = periodo;
		this.situacaoCom = situacaoCom;
  	}
 
	public Integer getIdCom() {
		return idCom;
	}

	public void setIdCom(Integer idCom) {
		this.idCom = idCom;
	}

	public Integer getCodigoFornecedorCom() {
		return codigoFornecedorCom;
	}

	public void setCodigoFornecedorCom(Integer codigoFornecedorCom) {
		this.codigoFornecedorCom = codigoFornecedorCom;
	}

 	public String getNomeFornecedorCom() {
		return nomeFornecedorCom;
	}

	public void setNomeFornecedorCom(String nomeFornecedorCom) {
		this.nomeFornecedorCom = nomeFornecedorCom;
	}

	public Integer getNnfCom() {
		return nnfCom;
	}

	public void setNnfCom(Integer nnfCom) {
		this.nnfCom = nnfCom;
	}

	public Date getDataCom() {
		return dataCom;
	}

	public void setDataCom(Date dataCom) {
		this.dataCom = dataCom;
	}

	public Date getDataVencimentoCom() {
		return dataVencimentoCom;
	}

	public void setDataVencimentoCom(Date dataVencimentoCom) {
		this.dataVencimentoCom = dataVencimentoCom;
	}

	public Double getValorCom() {
		return valorCom;
	}

	public void setValorCom(Double valorCom) {
		this.valorCom = valorCom;
	}

	public Integer getParcelaCom() {
		return parcelaCom;
	}

	public void setParcelaCom(Integer parcelaCom) {
		this.parcelaCom = parcelaCom;
	}

	public Integer getPrazoCom() {
		return prazoCom;
	}

	public void setPrazoCom(Integer prazoCom) {
		this.prazoCom = prazoCom;
	}

 	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

 	public TipoConsumo getTipoConsumo() {
		return tipoConsumo;
	}

	public void setTipoConsumo(TipoConsumo tipoConsumo) {
		this.tipoConsumo = tipoConsumo;
	}

 	public ParPeriodo getParPeriodo() {
		return periodo;
	}

	public void setParPeriodo(ParPeriodo periodo) {
		this.periodo = periodo;
	}

	public Integer getSituacaoCom() {
		return situacaoCom;
	}

	public void setSituacaoCom(Integer situacaoCom) {
		this.situacaoCom = situacaoCom;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigoFornecedorCom, nnfCom);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Compromisso other = (Compromisso) obj;
		return Objects.equals(codigoFornecedorCom, other.codigoFornecedorCom) && Objects.equals(nnfCom, other.nnfCom);
	}

	@Override
	public String toString() {
		return "Compromisso [idCom=" + idCom + ", codigoFornecedorCom=" + codigoFornecedorCom + ", nomeFornecedorCom="
				+ nomeFornecedorCom + ", nnfCom=" + nnfCom + ", dataCom=" + dataCom + ", dataVencimentoCom="
				+ dataVencimentoCom + ", valorCom=" + valorCom + ", parcelaCom=" + parcelaCom + ", prazoCom=" + prazoCom
				+ ", situacaoCom=" + situacaoCom + ", fornecedor=" + fornecedor + ", tipoConsumo=" + tipoConsumo
				+ ", periodo=" + periodo + "]";
	}
}	