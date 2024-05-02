package gui.sgomodel.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

import gui.util.Maria;

public class Material implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer codigoMat;
	private Integer grupoMat;
	private String nomeMat;
	private Double estMinMat;
	private Double entradaMat;
	private Double saidaMat;
	private Double saldoMat;
	private Double cmmMat;
	private Double precoMat;
	private Double vendaMat;
	private Integer vidaKmMat;
	private Integer vidaMesMat;
	private Double percentualMat;
	private char letraMat;
	private Date dataCadastroMat;

  	private Grupo grupo;

	public Material() {
	}

	public Material(Integer codigoMat, Integer grupoMat, String nomeMat, Double estMinMat, Double entradaMat,
			Double saidaMat, Double saldoMat, Double cmmMat, Double precoMat, Double vendaMat, Integer vidaKmMat,
			Integer vidaMesMat, Double percentualMat, char letraMat, Date dataCadastroMat, Grupo grupo) {
		this.codigoMat = codigoMat;
		this.grupoMat = grupoMat;
		this.nomeMat = nomeMat;
		this.estMinMat = estMinMat;
		this.entradaMat = entradaMat;
		this.saidaMat = saidaMat;
		this.saldoMat = saldoMat;
		this.cmmMat = cmmMat;
		this.precoMat = precoMat;
		this.vendaMat = vendaMat;
		this.vidaKmMat = vidaKmMat;
		this.vidaMesMat = vidaMesMat;
		this.percentualMat = percentualMat;
		this.letraMat = letraMat;
		this.dataCadastroMat = dataCadastroMat;
		this.grupo = grupo;
	}

	public Integer getCodigoMat() {
		return codigoMat;
	}

	public void setCodigoMat(Integer codigoMat) {
		this.codigoMat = codigoMat;
	}

	public Integer getGrupoMat() {
		return grupoMat;
	}

	public void setGrupoMat(Integer grupoMat) {
		this.grupoMat = grupoMat;
	}

	public String getNomeMat() {
		return nomeMat;
	}

	public void setNomeMat(String nomeMat) {
		this.nomeMat = nomeMat;
	}

	public Double getEstMinMat() {
		return estMinMat;
	}

	public void setEstMinMat(Double estMinMat) {
		this.estMinMat = estMinMat;
	}

	public Double getEntradaMat() {
		return entradaMat;
	}

	public void setEntradaMat(Double qtdE) {
		if (entradaMat == null) {
			entradaMat = 0.0;
		}
		this.entradaMat += qtdE;
	}

	public Double getSaidaMat() {
		return saidaMat;
	}

	public void setSaidaMat(Double qtdS) {
		if (saidaMat == null) {
			saidaMat = 0.0;
		}
		this.saidaMat += qtdS;
	}

	public Double getSaldoMat() {
		return saldoMat = entradaMat - saidaMat;
	}

	public void setSaldoMat(Double saldoMat) {
		this.saldoMat = saldoMat;
	}

	public Double getCmmMat() {
		return cmmMat = calculaCmm();
	}

	public Double calculaCmm() {
		if (saidaMat == null) {
			return 0.00;
		}
		if (saidaMat != null) {
			LocalDate dt1 = Maria.criaLocalAtual();
			if (dataCadastroMat == null) {
				setDataCadastroMat(new Date());
			}
			LocalDate dt2 = Maria.dateParaLocal(dataCadastroMat);
			long dias = Maria.durationPositivo(dt2, dt1).toDays();
			int meses = 0;
			if (dias > 30) {
				meses = (int) (dias / 30);
				cmmMat = saidaMat / meses;
			} else {
				cmmMat = saidaMat;
			}	
		}
		return cmmMat; 	
	}

	public Double getPrecoMat() {
		return precoMat;
	}

	public void setPrecoMat(Double precoMat) {
		this.precoMat = precoMat;
	}

	public Double getVendaMat() {
		return vendaMat;
	}

	public void setVendaMat(Double vendaMat) {
		this.vendaMat = vendaMat;
	}

	public Integer getVidaKmMat() {
		return vidaKmMat;
	}

	public void setVidaKmMat(Integer vidaKmMat) {
		this.vidaKmMat = vidaKmMat;
	}

	public Integer getVidaMesMat() {
		return vidaMesMat;
	}

	public void setVidaMesMat(Integer vidaMesMat) {
		this.vidaMesMat = vidaMesMat;
	}

	public Double getPercentualMat() {
		return percentualMat;
	}

	public void setPercentualMat(Double percentualMat) {
		this.percentualMat = percentualMat;
	}

	public char getLetraMat() {
		return letraMat;
	}

	public void setLetraMat(char letraMat) {
		this.letraMat = letraMat;
	}

	public Date getDataCadastroMat() {
		return dataCadastroMat;
	}

	public void setDataCadastroMat(Date dataCadastroMat) {
		this.dataCadastroMat = dataCadastroMat;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public void calculaPercentual() {
		if (saldoMat > 0) {
			percentualMat = (((vendaMat - precoMat) * 100) / precoMat);
		}	
//			percentualClass = Math.ceil((((vendaMat - precoMat) * 100) / total));
	}

	public double letraMat(double acum, double custo) {
		double perc = ((precoMat) * 100) / custo;
		letraMat = ' ';
		acum += perc;
		if (acum < 20.01) {
			letraMat = 'C';
		}
		if (acum > 20 && acum < 80.01) {
			letraMat = 'B';
		}
		if (acum > 80.00)  {
			letraMat = 'A';
		}
		return acum;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(codigoMat);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Material other = (Material) obj;
		return Objects.equals(codigoMat, other.codigoMat);
	}

	@Override
	public String toString() {
		return "MatMat [codigoMat=" + codigoMat + ", grupoMat=" + grupoMat + ", nomeMat=" + nomeMat + ", estMinMat="
				+ estMinMat + ", entradaMat=" + entradaMat + ", saidaMat=" + saidaMat + ", saldoMat=" + saldoMat
				+ ", cmmMat=" + cmmMat + ", precoMat=" + precoMat + ", vendaMat=" + vendaMat + ", vidaKmMat="
				+ vidaKmMat + ", vidaMesMat=" + vidaMesMat + ", percentualMat=" + percentualMat + ", letraMat="
				+ letraMat + ", dataCadastroMat=" + dataCadastroMat + ", grupo=" + grupo + "]";
	}
}
