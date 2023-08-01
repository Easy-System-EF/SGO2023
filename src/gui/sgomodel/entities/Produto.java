package gui.sgomodel.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Produto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer codigoProd;
	private Integer grupoProd;
	private String nomeProd;
	private Double estMinProd;
	private Double saldoProd;
	private Double saidaCmmProd;
	private Double cmmProd;
	private Double precoProd;
	private Double vendaProd;
	private Integer vidaKmProd;
	private Integer vidaMesProd;
	private Double percentualProd;
	private char letraProd;
	private Date dataCadastroProd;

	private static double acum = 0.00;
  	private Grupo grupo;

	public Produto() {
	}
	
	public Produto(Integer codigoProd, Integer grupoProd, String nomeProd, Double estMinProd, Double saldoProd,
			Double saidaCmmProd, Double cmmProd, Double precoProd, Double vendaProd, Integer vidaKmProd,
			Integer vidaMesProd, Double percentualProd, char letraProd, Date dataCadastroProd, Grupo grupo) {
		this.codigoProd = codigoProd;
		this.grupoProd = grupoProd;
		this.nomeProd = nomeProd;
		this.estMinProd = estMinProd;
		this.saldoProd = saldoProd;
		this.saidaCmmProd = saidaCmmProd;
		this.cmmProd = cmmProd;
		this.precoProd = precoProd;
		this.vendaProd = vendaProd;
		this.vidaKmProd = vidaKmProd;
		this.vidaMesProd = vidaMesProd;
		this.percentualProd = percentualProd;
		this.letraProd = letraProd;
		this.dataCadastroProd = dataCadastroProd;
		this.grupo = grupo;
	}

	public Integer getCodigoProd() {
		return codigoProd;
	}

	public void setCodigoProd(Integer codigoProd) {
		this.codigoProd = codigoProd;
	}

	public Integer getGrupoProd() {
		return grupoProd;
	}

	public void setGrupoProd(Integer grupoProd) {
		this.grupoProd = grupoProd;
	}

	public String getNomeProd() {
		return nomeProd;
	}

	public void setNomeProd(String nomeProd) {
		this.nomeProd = nomeProd;
	}

	public Double getEstMinProd() {
		return estMinProd;
	}

	public void setEstMinProd(Double estMinProd) {
		this.estMinProd = estMinProd;
	}

	public Double getSaldoProd() {
		return saldoProd;
	}

	public void setSaldoProd(Double saldoProd) {
		this.saldoProd = saldoProd;
	}

	public void setSaidaCmmProd(Double saidaCmmProd) {
		this.saidaCmmProd = saidaCmmProd;
	}

	public Double getSaidaCmmProd() {
		return saidaCmmProd;
	}

	public Double getCmmProd() {
		return cmmProd;
	}

	public void setCmmProd(Double cmmProd) {
		this.cmmProd = cmmProd;
//		this.cmmProd = calculaCmm();
	}

	public Double getPrecoProd() {
		return precoProd;
	}

	public void setPrecoProd(Double precoProd) {
		this.precoProd = precoProd;
	}

	public Double getVendaProd() {
		return vendaProd;
	}

	public void setVendaProd(Double vendaProd) {
		this.vendaProd = vendaProd;
	}

	public Integer getVidaKmProd() {
		return vidaKmProd;
	}

	public void setVidaKmProd(Integer vidaKmProd) {
		this.vidaKmProd = vidaKmProd;
	}

	public Integer getVidaMesProd() {
		return vidaMesProd;
	}

	public void setVidaMesProd(Integer vidaMesProd) {
		this.vidaMesProd = vidaMesProd;
	}

	public Double getPercentualProd() {
		return percentualProd;
	}

	public void setPercentualProd(Double percentualProd) {
		this.percentualProd = percentualProd;
	}

	public void calculaPercentual(double total) {
		if (saldoProd > 0) {
//			percentualProd = Math.ceil((((vendaProd - precoProd) * 100) / total));
			percentualProd = (((vendaProd - precoProd) * 100) / total);
		}	
	}

	public char getLetraProd() {
		return letraProd;
	}

	public void setLetraProd(char letraProd) {
		this.letraProd = letraProd;
	}

	public void letraClassCli() {
		letraProd = ' ';
		if (saldoProd > 0) {
			acum += percentualProd;
			letraProd = 'A';
			if (acum > 20.00) {
				if (acum < 50.01) {
					letraProd = 'B';
				} else {
					letraProd = 'C';
				}
			}	
		}
	}
	
	public Date getDataCadastroProd() {
		return dataCadastroProd;
	}

	public void setDataCadastroProd(Date dataCadastroProd) {
		this.dataCadastroProd = dataCadastroProd;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

//	public Double calculaCmm() {
//		if (saidaCmmProd != null) {
//			Date dataHoje = new Date();
//			LocalDate dt1 = DataStatic.dateParaLocal(dataHoje);
//			LocalDate dt2 = DataStatic.dateParaLocal(dataCadastroProd);
//			long dias = DataStatic.durationPositivo(dt1, dt2).toDays();
//			long meses = 0;
////			long dif = dataHoje.getTime() - dataCadastroProd.getTime();
////			long  dias = TimeUnit.DAYS.convert(dif, TimeUnit.MILLISECONDS);
//			if (dias > 30) {
//				meses = dias / 30;
//				cmmProd = saidaCmmProd / meses;
//			}
//			else {
//				cmmProd = saidaCmmProd;
//			}
//		} 	
//		return cmmProd;
//	}

	@Override
	public int hashCode() {
		return Objects.hash(codigoProd);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Produto other = (Produto) obj;
		return Objects.equals(codigoProd, other.codigoProd);
	}

	@Override
	public String toString() {
		return "Produto [codigoProd=" + codigoProd + ", grupoProd=" + grupoProd + ", nomeProd=" + nomeProd
				+ ", estMinProd=" + estMinProd + ", saldoProd=" + saldoProd + ", saidaCmmProd=" + saidaCmmProd
				+ ", cmmProd=" + cmmProd + ", precoProd=" + precoProd + ", vendaProd=" + vendaProd + ", vidaKmProd="
				+ vidaKmProd + ", vidaMesProd=" + vidaMesProd + ", percentualProd=" + percentualProd + ", letraProd="
				+ letraProd + ", dataCadastroProd=" + dataCadastroProd + ", grupo=" + grupo + "]";
	}

// 		Date dt  = new Date();
// 		Calendar cal = Calendar.getInstance();
// 		cal.setTime(dt);
// 		int aa = cal.get(Calendar.YEAR);
// 		int mm = cal.get(Calendar.MONTH);
//      saidaCmmProd é só para calculo do cmm	
}
