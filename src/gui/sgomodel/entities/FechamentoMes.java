package gui.sgomodel.entities;

import java.io.Serializable;

public class FechamentoMes implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer numeroMensal;
	private String osMensal;
	private String balMensal;
	private String dataMensal;
	private String clienteMensal;
	private String funcionarioMensal;
	private String valorOsMensal;
	private String valorMaterialMensal;
	private String valorComissaoMensal;
	private String valorResultadoMensal;
	private String valorAcumuladoMensal;

	private Meses mes;
	private Anos ano;
		
	public FechamentoMes() {
	}

	public FechamentoMes(Integer numeroMensal, String osMensal, String balMensal, 
			String dataMensal, String clienteMensal, String funcionarioMensal, String valorOsMensal, 
			String valorMaterialMensal, String valorComissaoMensal, String valorResultadoMensal, 
			String valorAcumuladoMensal, Meses mes, Anos ano) {
		this.numeroMensal = numeroMensal;
		this.osMensal = osMensal;
		this.balMensal = balMensal;
		this.dataMensal = dataMensal;
		this.clienteMensal = clienteMensal;
		this.funcionarioMensal = funcionarioMensal;
		this.valorOsMensal = valorOsMensal;
		this.valorMaterialMensal = valorMaterialMensal;
		this.valorComissaoMensal = valorComissaoMensal;
		this.valorResultadoMensal = valorResultadoMensal;
		this.valorAcumuladoMensal = valorAcumuladoMensal;
		this.mes = mes;
		this.ano = ano;
	}

	public Integer getNumeroMensal() {
		return numeroMensal;
	}

	public void setNumeroMensal(Integer numeroMensal) {
		this.numeroMensal = numeroMensal;
	}

	public String getOsMensal() {
		return osMensal;
	}

	public void setOsMensal(String osMensal) {
		this.osMensal = osMensal;
	}

	public String getBalMensal() {
		return balMensal;
	}

	public void setBalMensal(String balMensal) {
		this.balMensal = balMensal;
	}

	public String getDataMensal() {
		return dataMensal;
	}

	public void setDataMensal(String dataMensal) {
		this.dataMensal = dataMensal;
	}

	public String getClienteMensal() {
		return clienteMensal;
	}

	public void setClienteMensal(String clienteMensal) {
		this.clienteMensal = clienteMensal;
	}

	public String getFuncionarioMensal() {
		return funcionarioMensal;
	}

	public void setFuncionarioMensal(String funcionarioMensal) {
		this.funcionarioMensal = funcionarioMensal;
	}

	public String getValorOsMensal() {
		return valorOsMensal;
	}

	public void setValorOsMensal(String valorOsMensal) {
		this.valorOsMensal = valorOsMensal;
	}

	public String getValorMaterialMensal() {
		return valorMaterialMensal;
	}

	public void setValorMaterialMensal(String valorMaterialMensal) {
		this.valorMaterialMensal = valorMaterialMensal;
	}

	public String getValorComissaoMensal() {
		return valorComissaoMensal;
	}

	public void setValorComissaoMensal(String valorComissaoMensal) {
		this.valorComissaoMensal = valorComissaoMensal;
	}

	public String getValorResultadoMensal() {
		return valorResultadoMensal;
	}

	public void setValorResultadoMensal(String valorResultadoMensal) {
		this.valorResultadoMensal = valorResultadoMensal;
	}

	public String getValorAcumuladoMensal() {
		return valorAcumuladoMensal;
	}

	public void setValorAcumuladoMensal(String valorAcumuladoMensal) {
		this.valorAcumuladoMensal = valorAcumuladoMensal;
	}

	public Meses getMes() {
		return mes;
	}

	public void setMes(Meses mes) {
		this.mes = mes;
	}

	public Anos getAno() {
		return ano;
	}

	public void setAno(Anos ano) {
		this.ano = ano;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroMensal == null) ? 0 : numeroMensal.hashCode());
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
		FechamentoMes other = (FechamentoMes) obj;
		if (numeroMensal == null) {
			if (other.numeroMensal != null)
				return false;
		} else if (!numeroMensal.equals(other.numeroMensal))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DadosFechamento [numeroMensal = " + numeroMensal + ", osMensal = " + osMensal + ", balMensal = " + balMensal
				+ ", dataMensal = " + dataMensal + ", clienteMensal = " + clienteMensal + ", funcionarioMensal = "
				+ funcionarioMensal + ", valorOsMensal = " + valorOsMensal + ", valorMaterialMensal = "
				+ valorMaterialMensal + ", valorComissaoMensal = " + valorComissaoMensal + ", valorResultadoMensal = "
				+ valorResultadoMensal + ", valorAcumuladoMensal = " + valorAcumuladoMensal + ", mes = " + mes + ", ano = "
				+ ano + "]";
	}
}
