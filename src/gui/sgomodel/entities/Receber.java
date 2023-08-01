package gui.sgomodel.entities;

import java.io.Serializable;
import java.util.Date;

import gui.sgcpmodel.entities.consulta.ParPeriodo;
 
public class Receber implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer numeroRec;
	private Integer funcionarioRec;
	private Integer clienteRec;
	private String nomeClienteRec;
	private Integer osRec;
	private Date dataOsRec;
	private String placaRec;
	private Integer parcelaRec;
	private String formaPagamentoRec;
	private Double valorRec;
	private Date dataVencimentoRec;
	private Date dataPagamentoRec;
	private Double jurosRec;
	private Double descontoRec;
	private Double totalRec;
	private Double valorPagoRec;

	/* forma pgto
	 * 1 = Dinheiro
	 * 2 = Pix
	 * 3 = D�bito
	 * 4 = CC	
	 */

	public ParPeriodo periodo;
	
	public Receber() {
	}

	public Receber(Integer numeroRec, Integer funcionarioRec, Integer clienteRec, String nomeClienteRec, Integer osRec,
			Date dataOsRec, String placaRec, Integer parcelaRec, String formaPagamentoRec, Double valorRec,
			Date dataVencimentoRec, Date dataPagamentoRec, ParPeriodo periodo, Double jurosRec, Double descontoRec, 
			Double totalRec, Double valorPagoRec) {
		this.numeroRec = numeroRec;
		this.funcionarioRec = funcionarioRec;
		this.clienteRec = clienteRec;
		this.nomeClienteRec = nomeClienteRec;
		this.osRec = osRec;
		this.dataOsRec = dataOsRec;
		this.placaRec = placaRec;
		this.parcelaRec = parcelaRec;
		this.formaPagamentoRec = formaPagamentoRec;
		this.valorRec = valorRec;
		this.dataVencimentoRec = dataVencimentoRec;
		this.dataPagamentoRec = dataPagamentoRec;
		this.periodo = periodo;
		this.jurosRec = jurosRec;
		this.descontoRec = descontoRec;
		this.totalRec = totalRec;
		this.valorPagoRec = valorPagoRec;
	}

	public Integer getNumeroRec() {
		return numeroRec;
	}

	public void setNumeroRec(Integer numeroRec) {
		this.numeroRec = numeroRec;
	}

	public Integer getFuncionarioRec() {
		return funcionarioRec;
	}

	public void setFuncionarioRec(Integer funcionarioRec) {
		this.funcionarioRec = funcionarioRec;
	}

	public Integer getClienteRec() {
		return clienteRec;
	}

	public void setClienteRec(Integer clienteRec) {
		this.clienteRec = clienteRec;
	}

	public String getNomeClienteRec() {
		return nomeClienteRec;
	}

	public void setNomeClienteRec(String nomeClienteRec) {
		this.nomeClienteRec = nomeClienteRec;
	}

	public Integer getOsRec() {
		return osRec;
	}

	public void setOsRec(Integer osRec) {
		this.osRec = osRec;
	}

	public Date getDataOsRec() {
		return dataOsRec;
	}

	public void setDataOsRec(Date dataOsRec) {
		this.dataOsRec = dataOsRec;
	}

	public String getPlacaRec() {
		return placaRec;
	}

	public void setPlacaRec(String placaRec) {
		this.placaRec = placaRec;
	}

	public Integer getParcelaRec() {
		return parcelaRec;
	}

	public void setParcelaRec(Integer parcelaRec) {
		this.parcelaRec = parcelaRec;
	}

	public String getFormaPagamentoRec() {
		return formaPagamentoRec;
	}

	public void setFormaPagamentoRec(String formaPagamentoRec) {
		this.formaPagamentoRec = formaPagamentoRec;
	}

	public Double getValorRec() {
		return valorRec;
	}

	public void setValorRec(Double valorRec) {
		this.valorRec = valorRec;
	}

	public Date getDataVencimentoRec() {
		return dataVencimentoRec;
	}

	public void setDataVencimentoRec(Date dataVencimentoRec) {
		this.dataVencimentoRec = dataVencimentoRec;
	}

	public Date getDataPagamentoRec() {
		return dataPagamentoRec;
	}

	public void setDataPagamentoRec(Date dataPagamentoRec) {
		this.dataPagamentoRec = dataPagamentoRec;
	}

	public ParPeriodo getPeriodo() {
		return periodo;
	}

	public void setPeriodo(ParPeriodo periodo) {
		this.periodo = periodo;
	}
	
	public Double getJurosRec() {
		if (jurosRec == null) {
			jurosRec = 0.00;
		}
		return jurosRec;
	}

	public void setJurosRec(Double jurosRec) {
		if (jurosRec == null) {
			jurosRec = 0.00;
		}
		this.jurosRec = jurosRec;
	}

	public Double getDescontoRec() {
		if (descontoRec == null) {
			descontoRec = 0.00;
		}
		return descontoRec;
	}

	public void setDescontoRec(Double descontoRec) {
		if (descontoRec == null) {
			descontoRec = 0.00;
		}
		this.descontoRec = descontoRec;
	}

	public Double getTotalRec() {
		return totalRec;
	}

	public void setTotalRec(Double totalRec) {
		this.totalRec = (valorRec + jurosRec) - descontoRec;
	}

	public Double getValorPagoRec() {
		return valorPagoRec;
	}

	public void setValorPagoRec(Double valorPagoRec) {
		this.valorPagoRec = valorPagoRec;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroRec == null) ? 0 : numeroRec.hashCode());
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
		Receber other = (Receber) obj;
		if (numeroRec == null) {
			if (other.numeroRec != null)
				return false;
		} else if (!numeroRec.equals(other.numeroRec))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Receber [numeroRec=" + numeroRec + ", funcionarioRec=" + funcionarioRec + ", clienteRec=" + clienteRec
				+ ", nomeClienteRec=" + nomeClienteRec + ", osRec=" + osRec + ", dataOsRec=" + dataOsRec + ", placaRec="
				+ placaRec + ", parcelaRec=" + parcelaRec + ", formaPagamentoRec=" + formaPagamentoRec + ", valorRec="
				+ valorRec + ", dataVencimentoRec=" + dataVencimentoRec + ", dataPagamentoRec=" + dataPagamentoRec
				+ ", jurosRec=" + jurosRec + ", descontoRec=" + descontoRec + ", totalRec=" + totalRec
				+ ", valorPagoRec=" + valorPagoRec + ", periodo=" + periodo + "]";
	}
}
