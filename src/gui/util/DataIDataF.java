package gui.util;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class DataIDataF {

	public static LocalDate ldt = Maria.criaLocalAtual();
	public static int aa = Maria.anoDaData(ldt);
	public static int mm = Maria.mesDaData(ldt);
	public static int df = Maria.ultimoDiaMes(ldt);
	public static int dd = 01;
	public static Calendar cal = Calendar.getInstance();
	public static Date dt = new Date();
	public static Date dti = new Date();
	public static Date dtf = new Date();
		
	public static Date datai() {
		cal.setTime(dt);
		cal.set(Calendar.DAY_OF_MONTH, dd);
		cal.set(Calendar.MONTH, mm - 1);
		cal.set(Calendar.YEAR, aa);
		dti = cal.getTime();
		return dti;
	}
	
	public static Date dataf() {
		cal.setTime(dt);
		cal.set(Calendar.DAY_OF_MONTH, df);
		cal.set(Calendar.MONTH, mm - 1);
		cal.set(Calendar.YEAR, aa);
		dtf = cal.getTime();
		return dtf;
	}
	
}
