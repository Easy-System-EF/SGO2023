package gui.sgomodel.entities;

import java.io.Serializable;

public class FechamentoAnual implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer numeroAnual;
	private String mesAnual;
	private String doctoAnual;
	private String valorAnual;
	private String valorCustoAnual;
	private String valorComissaoAnual;
	private String valorResultadoAnual;
	private String valorAcumuladoAnual;

	public FechamentoAnual() {
	}

	public FechamentoAnual(Integer numeroAnual, String mesAnual, String doctoAnual, String valorAnual, String valorCustoAnual,
			String valorComissaoAnual, String valorResultadoAnual, String valorAcumuladoAnual) {
		this.numeroAnual = numeroAnual;
		this.mesAnual = mesAnual;
		this.doctoAnual = doctoAnual;
		this.valorAnual = valorAnual;
		this.valorCustoAnual = valorCustoAnual;
		this.valorComissaoAnual = valorComissaoAnual;
		this.valorResultadoAnual = valorResultadoAnual;
		this.valorAcumuladoAnual = valorAcumuladoAnual;
	}

	public Integer getNumeroAnual() {
		return numeroAnual;
	}

	public void setNumeroAnual(Integer numeroAnual) {
		this.numeroAnual = numeroAnual;
	}

	public String getMesAnual() {
		return mesAnual;
	}

	public void setMesAnual(String mesAnual) {
		this.mesAnual = mesAnual;
	}

	public String getDoctoAnual() {
		return doctoAnual;
	}

	public void setDoctoAnual(String doctoAnual) {
		this.doctoAnual = doctoAnual;
	}

	public String getValorAnual() {
		return valorAnual;
	}

	public void setValorAnual(String valorAnual) {
		this.valorAnual = valorAnual;
	}

	public String getValorCustoAnual() {
		return valorCustoAnual;
	}

	public void setValorCustoAnual(String valorCustoAnual) {
		this.valorCustoAnual = valorCustoAnual;
	}

	public String getValorComissaoAnual() {
		return valorComissaoAnual;
	}

	public void setValorComissaoAnual(String valorComissaoAnual) {
		this.valorComissaoAnual = valorComissaoAnual;
	}

	public String getValorResultadoAnual() {
		return valorResultadoAnual;
	}

	public void setValorResultadoAnual(String valorResultadoAnual) {
		this.valorResultadoAnual = valorResultadoAnual;
	}

	public String getValorAcumuladoAnual() {
		return valorAcumuladoAnual;
	}

	public void setValorAcumuladoAnual(String valorAcumuladoAnual) {
		this.valorAcumuladoAnual = valorAcumuladoAnual;
	}

	@Override
	public String toString() {
		return "Anual [numeroAnual=" + numeroAnual + ", mesAnual=" + mesAnual + ", doctoAnual=" + doctoAnual
				+ ", valorAnual=" + valorAnual + ", valorCustoAnual=" + valorCustoAnual + ", valorComissaoAnual="
				+ valorComissaoAnual + ", valorResultadoAnual=" + valorResultadoAnual + ", valorAcumuladoAnual="
				+ valorAcumuladoAnual + "]";
	}
}
