package gui.sgomodel.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import gui.util.Maria;

public class MatAnterior implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer codigoMatAnt;
	private Integer grupoMatAnt;
	private String nomeMatAnt;
	private Double estMinMatAnt;
	private Double saidaCmmMatAnt;
	private Double saldoMatAnt;
	private Double precoMatAnt;
	private Double vendaMatAnt;
	private Integer vidaKmMatAnt;
	private Integer vidaMesMatAnt;
	private Double cmmMatAnt;
	private Date dataCadastroMatAnt;
	private Double percentualClass;
	private char letraClass;

  	private Grupo grupo;

	public MatAnterior() {
	}

	public MatAnterior(Integer codigoMatAnt, String nomeMatAnt, Integer grupoMatAnt, Double estMinMatAnt, Double saidaCmmMatAnt,
			Double saldoMatAnt, Double precoMatAnt, Double vendaMatAnt, Integer vidaKmMatAnt, Integer vidaMesMatAnt, Double cmmMatAnt,
			Date dataCadastroMatAnt, Grupo grupo, Double percentualClass, char letraClass) {
		this.codigoMatAnt = codigoMatAnt;
		this.nomeMatAnt = nomeMatAnt;
		this.grupoMatAnt = grupoMatAnt;
		this.estMinMatAnt = estMinMatAnt;
		this.saidaCmmMatAnt = saidaCmmMatAnt;
		this.saldoMatAnt = saldoMatAnt;
		this.precoMatAnt = precoMatAnt;
		this.vendaMatAnt = vendaMatAnt;
		this.vidaKmMatAnt = vidaKmMatAnt;
		this.vidaMesMatAnt = vidaMesMatAnt;
		this.cmmMatAnt = cmmMatAnt;
		this.dataCadastroMatAnt = dataCadastroMatAnt;
		this.grupo = grupo;
		this.percentualClass = percentualClass;
		this.letraClass = letraClass;
	}

	public Integer getCodigoMatAnt() {
		return codigoMatAnt;
	}

	public void setCodigoMatAnt(Integer codigoMatAnt) {
		this.codigoMatAnt = codigoMatAnt;
	}

	public String getNomeMatAnt() {
		return nomeMatAnt;
	}

	public void setNomeMatAnt(String nomeMatAnt) {
		this.nomeMatAnt = nomeMatAnt;
	}

	public Integer getGrupoMatAnt() {
		return grupoMatAnt;
	}

	public void setGrupoMatAnt(Integer grupoMatAnt) {
		this.grupoMatAnt = grupoMatAnt;
	}

	public Double getEstMinMatAnt() {
		if (this.estMinMatAnt == null) {
			this.estMinMatAnt = 0.00;
		}
		return estMinMatAnt;
	}

	public void setEstMinMatAnt(Double estMinMatAnt) {
		if (this.estMinMatAnt == null) {
			this.estMinMatAnt = 0.00;
		}
		this.estMinMatAnt = estMinMatAnt;
	}

	public Double getSaidaCmmMatAnt() {
		if (this.saidaCmmMatAnt == null) {
			this.saidaCmmMatAnt = 0.00;
		}
		return saidaCmmMatAnt;
	}

	public void setSaidaCmmMatAnt(Double qtd) {
		if (this.saidaCmmMatAnt == null) {
			this.saidaCmmMatAnt = 0.00;
		}
		this.saidaCmmMatAnt += qtd;
	}

	public Double getSaldoMatAnt() {
		if (this.saldoMatAnt == null) {
			this.saldoMatAnt = 0.00;
		}
		return saldoMatAnt;
	}

	public void setSaldoMatAnt(Double saldoMatAnt) {
		if (this.saldoMatAnt == null) {
			this.saldoMatAnt = 0.00;
		}
		this.saldoMatAnt = saldoMatAnt;
	}

	public void saidaSaldo(Double qtdS) {
		if (this.saldoMatAnt == null) {
			this.saldoMatAnt = 0.00;
		}
		saldoMatAnt -= qtdS;
	}
	
	public void entraSaldo(Double qtdE) {
		if (this.saldoMatAnt == null) {
			this.saldoMatAnt = 0.00;
		}
		saldoMatAnt += qtdE;
	}

	public Double getPrecoMatAnt() {
		if (this.precoMatAnt == null) {
			this.precoMatAnt = 0.00;
		}
		return precoMatAnt;
	}

	public void setPrecoMatAnt(Double precoMatAnt) {
		if (this.precoMatAnt == null) {
			this.precoMatAnt = 0.00;
		}
		this.precoMatAnt = precoMatAnt;
	}

	public Double getVendaMatAnt() {
		if (vendaMatAnt == null) {
			vendaMatAnt = 0.00;
		}
		return vendaMatAnt;
	}

	public void setVendaMatAnt(Double vendaMatAnt) {
		if (vendaMatAnt == null) {
			vendaMatAnt = 0.00;
		}
		this.vendaMatAnt = vendaMatAnt;
	}

	public Integer getVidaKmMatAnt() {
		if (vidaKmMatAnt == null) {
			vidaKmMatAnt = 0;
		}
		return vidaKmMatAnt;
	}

	public void setVidaKmMatAnt(Integer vidaKmMatAnt) {
		if (vidaKmMatAnt == null) {
			vidaKmMatAnt = 0;
		}
		this.vidaKmMatAnt = vidaKmMatAnt;
	}

	public Integer getVidaMesMatAnt() {
		if (vidaMesMatAnt == null) {
			vidaMesMatAnt = 0;
		}
		return vidaMesMatAnt;
	}

	public void setVidaMesMatAnt(Integer vidaMesMatAnt) {
		if (vidaMesMatAnt == null) {
			vidaMesMatAnt = 0;
		}
		this.vidaMesMatAnt = vidaMesMatAnt;
	}

	public Double getCmmMatAnt() {
		if (cmmMatAnt == null) {
			cmmMatAnt = 0.00;
		}
		return cmmMatAnt;
	}

	public void setCmmMatAnt(Double cmmMatAnt) {
		if (cmmMatAnt == null) {
			cmmMatAnt = 0.00;
		}
		this.cmmMatAnt = calculaCmm();
	}

	public Date getDataCadastroMatAnt() {
		return dataCadastroMatAnt;
	}

	public void setDataCadastroMatAnt(Date dataCadastroMatAnt) {
		this.dataCadastroMatAnt = dataCadastroMatAnt;
	}

	public Double getPercentualClass() {
		return percentualClass;
	}

	public void setPercentualClass(Double percentualClass) {
		this.percentualClass = percentualClass;
	}
	
	public void calculaPercentual() {
		percentualClass = (vendaMatAnt * 100) / precoMatAnt;
	}

	public char getLetraClass() {
		return letraClass;
	}

	public void setLetraClass(char letraClass) {
		this.letraClass = letraClass;
	}

	public void letraClassCli(double acum) {
		if ( acum < 80.01) {
			letraClass = 'A';
		} else {
			if (acum < 95.00) {
				letraClass = 'B';
			} else {
				letraClass = 'C';
			}
		}
	}
	
	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public Double calculaCmm() {
		if (saidaCmmMatAnt != null) {
			LocalDate dt1 = Maria.criaLocalAtual();
			if (dataCadastroMatAnt == null) {
				setDataCadastroMatAnt(new Date());
			}
			LocalDate dt2 = Maria.dateParaLocal(dataCadastroMatAnt);
			long dias = Maria.durationPositivo(dt2, dt1).toDays();
			int meses = 0;
//			if (getDataCadastroMatAnt() != null) {
//				long dif = dataHoje.getTime() - dataCadastroMatAnt.getTime();
//				long  dias = TimeUnit.DAYS.convert(dif, TimeUnit.MILLISECONDS);
				if (dias > 30) {
					meses = (int) (dias / 30);
					cmmMatAnt = saidaCmmMatAnt / meses;
				}
				else {
					cmmMatAnt = saidaCmmMatAnt;
				}	
		} 	
		return cmmMatAnt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoMatAnt == null) ? 0 : codigoMatAnt.hashCode());
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
		MatAnterior other = (MatAnterior) obj;
		if (codigoMatAnt == null) {
			if (other.codigoMatAnt != null)
				return false;
		} else if (!codigoMatAnt.equals(other.codigoMatAnt))
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
		return "MatAnterial [codigoMatAnt=" + codigoMatAnt + ", nomeMatAnt=" + nomeMatAnt + ", estMinMatAnt="
				+ estMinMatAnt + ", saidaCmmMatAnt=" + saidaCmmMatAnt + ", saldoMatAnt=" + saldoMatAnt + ", precoMatAnt=" + precoMatAnt
				+ ", vendaMatAnt=" + vendaMatAnt + ", vidaKmMatAnt=" + vidaKmMatAnt + ", vidaMesMatAnt=" + vidaMesMatAnt + ", cmmMatAnt="
				+ cmmMatAnt + ", dataCadastroMatAnt=" + dataCadastroMatAnt + ", percentualClass=" + percentualClass
				+ ", letraClass=" + letraClass + ", grupo=" + grupo + "]";
	}

}
