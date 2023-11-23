package gui.sgomodel.entities;

import java.io.Serializable;

public class FechamentoAno implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer numeroAnual;
	private String osAnual;
	private String balAnual;
	private String dataAnual;
	private String clienteAnual;
	private String funcionarioAnual;
	private String valorOsAnual;
	private String valorMaterialAnual;
	private String valorComissaoAnual;
	private String valorResultadoAnual;
	private String valorAcumuladoAnual;

	public FechamentoAno() {
	}

	public FechamentoAno(Integer numeroAnual, String osAnual, String balAnual, 
			String dataAnual, String clienteAnual, String funcionarioAnual, String valorOsAnual, 
			String valorMaterialAnual, String valorComissaoAnual, String valorResultadoAnual, 
			String valorAcumuladoAnual) {
		this.numeroAnual = numeroAnual;
		this.osAnual = osAnual;
		this.balAnual = balAnual;
		this.dataAnual = dataAnual;
		this.clienteAnual = clienteAnual;
		this.funcionarioAnual = funcionarioAnual;
		this.valorOsAnual = valorOsAnual;
		this.valorMaterialAnual = valorMaterialAnual;
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

	public String getOsAnual() {
		return osAnual;
	}

	public void setOsAnual(String osAnual) {
		this.osAnual = osAnual;
	}

	public String getBalAnual() {
		return balAnual;
	}

	public void setBalAnual(String balAnual) {
		this.balAnual = balAnual;
	}

	public String getDataAnual() {
		return dataAnual;
	}

	public void setDataAnual(String dataAnual) {
		this.dataAnual = dataAnual;
	}

	public String getClienteAnual() {
		return clienteAnual;
	}

	public void setClienteAnual(String clienteAnual) {
		this.clienteAnual = clienteAnual;
	}

	public String getFuncionarioAnual() {
		return funcionarioAnual;
	}

	public void setFuncionarioAnual(String funcionarioAnual) {
		this.funcionarioAnual = funcionarioAnual;
	}

	public String getValorOsAnual() {
		return valorOsAnual;
	}

	public void setValorOsAnual(String valorOsAnual) {
		this.valorOsAnual = valorOsAnual;
	}

	public String getValorMaterialAnual() {
		return valorMaterialAnual;
	}

	public void setValorMaterialAnual(String valorMaterialAnual) {
		this.valorMaterialAnual = valorMaterialAnual;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroAnual == null) ? 0 : numeroAnual.hashCode());
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
		FechamentoAno other = (FechamentoAno) obj;
		if (numeroAnual == null) {
			if (other.numeroAnual != null)
				return false;
		} else if (!numeroAnual.equals(other.numeroAnual))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DadosFechamento [numeroAnual = " + numeroAnual + ", osAnual = " + osAnual + ", balAnual = " + balAnual
				+ ", dataAnual = " + dataAnual + ", clienteAnual = " + clienteAnual + ", funcionarioAnual = "
				+ funcionarioAnual + ", valorOsAnual = " + valorOsAnual + ", valorMaterialAnual = "
				+ valorMaterialAnual + ", valorComissaoAnual = " + valorComissaoAnual + ", valorResultadoAnual = "
				+ valorResultadoAnual + ", valorAcumuladoAnual = " + valorAcumuladoAnual + "]";
	}
}
