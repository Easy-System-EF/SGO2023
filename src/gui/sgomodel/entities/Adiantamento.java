package gui.sgomodel.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Adiantamento extends Funcionario implements Serializable{

	private static final long serialVersionUID = 1L;
 
	private Integer numeroAdi;
	private Date dataAdi;
	private Double valeAdi;
	private Integer mesAdi;
	private Integer anoAdi;
	private Double valorAdi;
	private Integer osAdi;
	private Integer balcaoAdi;
	private Double comissaoAdi;
	private String tipoAdi;
	private Double salarioAdi;

	public static Double percComissao = 0.00; 
	
	public Adiantamento() {
		super();
	}

	public Adiantamento(Integer codigoFun, String nomeFun, String enderecoFun, String bairroFun, String cidadeFun,
			String ufFun, String cepFun, Integer dddFun, Integer telefoneFun, String cpfFun, String pixFun,
			Double comissaoFun, Double adiantamentoFun, Integer mesFun, Integer anoFun, String cargoFun,
			String situacaoFun, Double salarioFun, Date dataCadastroFun, Cargo cargo, Situacao situacao, Integer numeroAdi, Date dataAdi,
			Double valeAdi, Integer mesAdi, Integer anoAdi, Double valorAdi, Integer osAdi, Integer balcaoAdi,
			Double comissaoAdi, String tipoAdi, Double salarioAdi) {
		super(codigoFun, nomeFun, enderecoFun, bairroFun, cidadeFun, ufFun, cepFun, dddFun, telefoneFun, cpfFun, pixFun,
				comissaoFun, adiantamentoFun, mesFun, anoFun, cargoFun, situacaoFun, salarioFun, dataCadastroFun, cargo, 
				situacao);
		this.numeroAdi = numeroAdi;
		this.dataAdi = dataAdi;
		this.valeAdi = valeAdi;
		this.mesAdi = mesAdi;
		this.anoAdi = anoAdi;
		this.valorAdi = valorAdi;
		this.osAdi = osAdi;
		this.balcaoAdi = balcaoAdi;
		this.comissaoAdi = comissaoAdi;
		this.tipoAdi = tipoAdi;
		this.salarioAdi = salarioAdi;
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

/*
 * o vale, na cartela (venda), recebe o percentual de comissão p/ calculo de comissao
 */

	public Double getValeAdi() {
		return valeAdi;
	}

	public void setValeAdi(Double valeAdi) {
		this.valeAdi = valeAdi;
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

	public Double getValorAdi() {
		return valorAdi;
	}

	public void setValorAdi(Double valorAdi) {
		this.valorAdi = valorAdi;
	}

	public Integer getOsAdi() {
		if (osAdi == null) {
			osAdi = 0;
		}
		return osAdi;
	}

	public void setOsAdi(Integer osAdi) {
		if (osAdi == null) {
			osAdi = 0;
		}
		this.osAdi = osAdi;
	}

	public Integer getBalcaoAdi() {
		if (balcaoAdi == null) {
			balcaoAdi = 0;
		}
		return balcaoAdi;
	}

	public void setBalcaoAdi(Integer balcaoAdi) {
		if (balcaoAdi == null) {
			balcaoAdi = 0;
		}
		this.balcaoAdi = balcaoAdi;
	}

	public Double getComissaoAdi() {
		return comissaoAdi;
	}

	public void calculaComissao() {
		comissaoAdi = (valorAdi * percComissao) / 100;
	}

	public void setComissaoAdi(Double comissaoAdi) {
		if (comissaoAdi == null) {
			comissaoAdi = 0.00;
		}
		this.comissaoAdi = comissaoAdi;
	}


	public String getTipoAdi() {
		return tipoAdi;
	}

	public void setTipoAdi(String tipoAdi) {
		this.tipoAdi = tipoAdi;
	}

	public Double getSalarioAdi() {
		return salarioAdi; 
	}

	public void setSalarioAdi(Double salarioAdi) {
		if (salarioAdi == null) {
			salarioAdi = 0.00;
		}
		this.salarioAdi = salarioAdi;
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
		return "Adiantamento [numeroAdi=" + numeroAdi + ", dataAdi=" + dataAdi + ", valeAdi=" + valeAdi + ", mesAdi="
				+ mesAdi + ", anoAdi=" + anoAdi + ", valorAdi=" + valorAdi + ", osAdi=" + osAdi + ", balcaoAdi="
				+ balcaoAdi + ", comissaoAdi=" + comissaoAdi + ", tipoAdi=" + tipoAdi + ", salarioAdi=" + salarioAdi
				+ "]";
	}

}