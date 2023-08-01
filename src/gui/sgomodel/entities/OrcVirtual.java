package gui.sgomodel.entities;

import java.io.Serializable;

/*
 * or�amento virtual para mater pre�o n�mero de materiais
 */

public class OrcVirtual implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer numeroVir;
	private String nomeMatVir;
	private Double quantidadeMatVir;
	private Double precoMatVir;
	private Double totalMatVir;
	private Integer numeroOrcVir;
	private Integer numeroBalVir;
	private Double custoMatVir;

	private Material material;
	
	public OrcVirtual() {
	}

	public OrcVirtual(Integer numeroVir, String nomeMatVir, Double quantidadeMatVir, 
			Double precoMatVir, Double totalMatVir, Integer numeroOrcVir, 
			Integer numeroBalVir, Double custoMatVir, Material material) {
		this.numeroVir = numeroVir;
		this.nomeMatVir = nomeMatVir;
		this.quantidadeMatVir = quantidadeMatVir;
		this.precoMatVir = precoMatVir;
		this.totalMatVir = totalMatVir;
		this.numeroOrcVir = numeroOrcVir;
		this.numeroBalVir = numeroBalVir;
		this.material = material;
		this.custoMatVir = custoMatVir;
	}

	public Integer getNumeroVir() {
		return numeroVir;
	}

	public void setNumeroVir(Integer numVir) {
		this.numeroVir = numVir;
	}

	public String getNomeMatVir() {
		return nomeMatVir;
	}

	public void setNomeMatVir(String nomMatVir) {
		this.nomeMatVir = nomMatVir;
	}

	public Double getQuantidadeMatVir() {
		if (quantidadeMatVir == null) {
			quantidadeMatVir = 0.00;
		}
		return quantidadeMatVir;
	}

	public void setQuantidadeMatVir(Double qtdMatVir) {
		this.quantidadeMatVir = qtdMatVir;
	}

	public Double getPrecoMatVir() {
		if (precoMatVir == null) {
			precoMatVir = 0.00;
		}
		return precoMatVir;
	}

	public void setPrecoMatVir(Double preMatVir) {
		this.precoMatVir = preMatVir;
	}

	public Double getTotalMatVir() {
		return totalMatVir = quantidadeMatVir * precoMatVir;
	}

	public void setTotalMatVir(Double totMatVir) {
		this.totalMatVir = totMatVir;
	}

	public Integer getNumeroOrcVir() {
		return numeroOrcVir;
	}

	public void setNumeroOrcVir(Integer numOrcVir) {
		this.numeroOrcVir = numOrcVir;
	}

	public Integer getNumeroBalVir() {
		return numeroBalVir;
	}

	public void setNumeroBalVir(Integer numBalVir) {
		this.numeroBalVir = numBalVir;
	}

	public Double getCustoMatVir() {
		return custoMatVir;
	}

	public void setCustoMatVir(Double cusMatVir) {
		this.custoMatVir = cusMatVir;
	}

	public void setMaterial(Material mat) {
		this.material = mat;
	}

	public Material getMaterial() {
		return material;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroOrcVir == null) ? 0 : numeroOrcVir.hashCode());
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
		OrcVirtual other = (OrcVirtual) obj;
		if (numeroOrcVir == null) {
			if (other.numeroOrcVir != null)
				return false;
		} else if (!numeroOrcVir.equals(other.numeroOrcVir))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OrcVirtual [numeroVir=" + numeroVir + ", nomeMatVir=" + nomeMatVir + ", quantidadeMatVir="
				+ quantidadeMatVir + ", precoMatVir=" + precoMatVir + ", totalMatVir=" + totalMatVir + ", numeroOrcVir="
				+ numeroOrcVir + ", numeroBalVir=" + numeroBalVir + ", custoMatVir=" + custoMatVir + ", material="
				+ material + "]";
	}
}
