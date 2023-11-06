package gui.sgomodel.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import gui.util.DataStatic;

public class Material implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer codigoMat;
	private Integer grupoMat;
	private String nomeMat;
	private Double estMinMat;
	private Double saldoMat;
	private Double saidaCmmMat;
	private Double cmmMat;
	private Double precoMat;
	private Double vendaMat;
	private Integer vidaKmMat;
	private Integer vidaMesMat;
	private Double percentualClass;
	private char letraClass;
	private Date dataCadastroMat;

  	private Grupo grupo;

	public Material() {
	}

	public Material(Integer codigoMat, Integer grupoMat, String nomeMat, Double estMinMat, Double saldoMat,
			Double saidaCmmMat, Double cmmMat, Double precoMat, Double vendaMat, Integer vidaKmMat, Integer vidaMesMat,
			Double percentualClass, char letraClass, Date dataCadastroMat, Grupo grupo) {
		this.codigoMat = codigoMat;
		this.grupoMat = grupoMat;
		this.nomeMat = nomeMat;
		this.estMinMat = estMinMat;
		this.saldoMat = saldoMat;
		this.saidaCmmMat = saidaCmmMat;
		this.cmmMat = cmmMat;
		this.precoMat = precoMat;
		this.vendaMat = vendaMat;
		this.vidaKmMat = vidaKmMat;
		this.vidaMesMat = vidaMesMat;
		this.percentualClass = percentualClass;
		this.letraClass = letraClass;
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
		if (this.estMinMat == null) {
			this.estMinMat = 0.00;
		}
		return estMinMat;
	}

	public void setEstMinMat(Double estMinMat) {
		if (this.estMinMat == null) {
			this.estMinMat = 0.00;
		}
		this.estMinMat = estMinMat;
	}

	public Double getSaldoMat() {
		if (this.saldoMat == null) {
			this.saldoMat = 0.00;
		}
		return saldoMat;
	}

	public void setSaldoMat(Double saldoMat) {
		if (this.saldoMat == null) {
			this.saldoMat = 0.00;
		}
		this.saldoMat = saldoMat;
	}

//metodo	
	public void saidaSaldo(Double qtdS) {
		if (this.saldoMat == null) {
			this.saldoMat = 0.00;
		}
		saldoMat -= qtdS;
	}
	
//metodo	
	public void entraSaldo(Double qtdE) {
		if (this.saldoMat == null) {
			this.saldoMat = 0.00;
		}
		saldoMat += qtdE;
	}

	public Double getSaidaCmmMat() {
		if (this.saidaCmmMat == null) {
			this.saidaCmmMat = 0.00;
		}
		return saidaCmmMat;
	}

	public void setSaidaCmmMat(Double qtd) {
		if (this.saidaCmmMat == null) {
			this.saidaCmmMat = 0.00;
		}
		this.saidaCmmMat += qtd;
	}

	public Double getCmmMat() {
		if (cmmMat == null) {
			cmmMat = 0.00;
		}
		return cmmMat;
	}

	public void setCmmMat(Double cmmMat) {
		if (cmmMat == null) {
			cmmMat = 0.00;
		}
		this.cmmMat = calculaCmm();
	}

	public Double getPrecoMat() {
		if (this.precoMat == null) {
			this.precoMat = 0.00;
		}
		return precoMat;
	}

	public void setPrecoMat(Double precoMat) {
		if (this.precoMat == null) {
			this.precoMat = 0.00;
		}
		this.precoMat = precoMat;
	}

	public Double getVendaMat() {
		if (vendaMat == null) {
			vendaMat = 0.00;
		}
		return vendaMat;
	}

	public void setVendaMat(Double vendaMat) {
		if (vendaMat == null) {
			vendaMat = 0.00;
		}
		this.vendaMat = vendaMat;
	}

	public Integer getVidaKmMat() {
		if (vidaKmMat == null) {
			vidaKmMat = 0;
		}
		return vidaKmMat;
	}

	public void setVidaKmMat(Integer vidaKmMat) {
		if (vidaKmMat == null) {
			vidaKmMat = 0;
		}
		this.vidaKmMat = vidaKmMat;
	}

	public Integer getVidaMesMat() {
		if (vidaMesMat == null) {
			vidaMesMat = 0;
		}
		return vidaMesMat;
	}

	public void setVidaMesMat(Integer vidaMesMat) {
		if (vidaMesMat == null) {
			vidaMesMat = 0;
		}
		this.vidaMesMat = vidaMesMat;
	}

	public Double getPercentualClass() {
		return percentualClass;
	}

	public void setPercentualClass(Double percentualClass) {
		this.percentualClass = percentualClass;
	}
	
	public void calculaPercentual() {
		if (saldoMat > 0) {
			percentualClass = (((vendaMat - precoMat) * 100) / precoMat);
		}	
//			percentualClass = Math.ceil((((vendaMat - precoMat) * 100) / total));
	}

	public char getLetraClass() {
		return letraClass;
	}

	public void setLetraClass(char letraClass) {
		this.letraClass = letraClass;
	}

	public double letraClassMat(double acum, double custo) {
		double perc = ((precoMat) * 100) / custo;
		letraClass = ' ';
		if (saldoMat > 0) {
			letraClass = 'A';
			acum += perc;
			if (acum > 80.00) {
				if (acum < 90.01) {
					letraClass = 'B';
				} else {
					letraClass = 'C';
				}
			}	
		}
		return acum;
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

	public Double calculaCmm() {
	if (codigoMat == 2) {	
		if (saidaCmmMat != null) {
			LocalDate dt1 = DataStatic.criaLocalAtual();
			if (dataCadastroMat == null) {
				setDataCadastroMat(new Date());
			}
			LocalDate dt2 = DataStatic.dateParaLocal(dataCadastroMat);
			long dias = DataStatic.durationPositivo(dt2, dt1).toDays();
			int meses = 0;
//			if (getDataCadastroMat() != null) {
//				long dif = dataHoje.getTime() - dataCadastroMat.getTime();
//				long  dias = TimeUnit.DAYS.convert(dif, TimeUnit.MILLISECONDS);
				if (dias > 30) {
					meses = (int) (dias / 30);
					cmmMat = saidaCmmMat / meses;
				}
				else {
					cmmMat = saidaCmmMat;
				}	
		} 	
	}
		return cmmMat;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoMat == null) ? 0 : codigoMat.hashCode());
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
		Material other = (Material) obj;
		if (codigoMat == null) {
			if (other.codigoMat != null)
				return false;
		} else if (!codigoMat.equals(other.codigoMat))
			return false;
		return true;
	}
// 		Date dt  = new Date();
// 		Calendar cal = Calendar.getInstance();
// 		cal.setTime(dt);
// 		int aa = cal.get(Calendar.YEAR);
// 		int mm = cal.get(Calendar.MONTH);

	@Override
	public String toString() {
		return "Material [codigoMat=" + codigoMat + ", grupoMat=" + grupoMat + ", nomeMat=" + nomeMat + ", estMinMat="
				+ estMinMat + ", saidaCmmMat=" + saidaCmmMat + ", saldoMat=" + saldoMat + ", precoMat=" + precoMat
				+ ", vendaMat=" + vendaMat + ", vidaKmMat=" + vidaKmMat + ", vidaMesMat=" + vidaMesMat + ", cmmMat="
				+ cmmMat + ", dataCadastroMat=" + dataCadastroMat + ", percentualClass=" + percentualClass
				+ ", letraClass=" + letraClass + ", grupo=" + grupo + "]";
	}

}
