package com.orendel.seam.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	// formatoFecha y FechaHora son public para ser utilizados como argumentos
	// al llamar a FechaUtil.  Por ejemplo:  FechaUtil.toString(fecha, FechaUtil.formatoFecha);
	
	public static String formatoFecha = "dd-MM-yyyy";
	public static String formatoHora = "hh:mm a";
	public static String formatoFechaHora = "dd-MM-yyyy hh:mm a";
	private static SimpleDateFormat formatter = new SimpleDateFormat(formatoFecha);
	
	public DateUtil() {
	}

	/**
	 * Permite obtener las fechas en espa�ol
	 */
	public static void setLocalES() {
		formatter = new SimpleDateFormat(formatoFecha, new Locale("es"));
	}
	
	/**
	 * Permite obtener las fechas en ingl�s
	 */
	public static void setLocalEN() {
		formatter = new SimpleDateFormat(formatoFecha, new Locale("en"));
	}
	
	/**
	 * Retorna el milisegundo de la hora actual.  Es utilizado para ayudar a separar
	 * los identificadores de sesiones generadas por un editor 
	 */
	public static String getMilisegundos() {
		String formatoSegundo = "S";
		formatter.applyPattern(formatoSegundo);
		return formatter.format(new Date());
	}
	
	public static String toString(Date fecha) {
		if (fecha != null) {
			formatter.applyPattern(formatoFecha);
			return formatter.format(fecha);
		} else {
			return "";
		}
	}
	
	/**
	 * Transforma un string a una fecha utilizando el formato suministrado.  Por ejemplo:  "dd-MM-yyyy"
	 * @param fecha Objeto de tipo Date
	 * @param formato formato a aplicar como: "MMMM dd, yyyy", "dd 'de' MMMM 'de' yyyy", etc
	 * @return string con la fecha formateada
	 */
	public static String toString(Date fecha, String formato) {
		formatter.applyPattern(formato);
		if (fecha != null) {
			String fechaTxt = formatter.format(fecha);
			return fechaTxt;
		} else {
			return "";
		}
	}
	
	/**
	 * Transforma un string a una fecha.  Utiliza el formato FechaUtil.formatoFecha por default.
	 * @param fecha fecha en formato "dd-MM-yyyy"
	 * @return
	 */
	public static Date toDate(String fecha) {
		if (fecha != null && fecha != "") {
			try {
				formatter.applyPattern(formatoFecha);
				return formatter.parse(fecha);
			} catch (ParseException e) {
				System.err.println("Error durante transformaci�n de fechas: " + e);
				return null;
			}
		} else {
			return null;
		}
	}
	
	public static Date toHour(String fecha) {
		if (fecha != null && fecha != "") {
			try {
				formatter.applyPattern(formatoHora);
				return formatter.parse(fecha);
			} catch (ParseException e) {
				System.err.println("Error durante transformaci�n de hora: " + e);
				return null;
			}
		} else {
			return null;
		}
	}
	
	public static Date toDateHour(String fecha) {
		if (fecha != null && fecha != "") {
			try {
				formatter.applyPattern(formatoFechaHora);
				return formatter.parse(fecha);
			} catch (ParseException e) {
				System.err.println("Error durante transformaci�n de fecha-hora: " + e);
				return null;
			}
		} else {
			return null;
		}
	}
	
	public static String ajustarFecha(String fechaTxt, int dias) {
		Calendar calendar = Calendar.getInstance();
		Date fecha = DateUtil.toDate(fechaTxt);
		long timeInMillis = fecha.getTime();
		calendar.setTimeInMillis(timeInMillis);
		calendar.add(Calendar.DATE, dias);
		fecha = calendar.getTime();
		String fechaAjustada = DateUtil.toString(fecha);
		return fechaAjustada;
	}
	
	public static Date ajustarFecha(Date fecha, int dias) {
		Calendar calendar = Calendar.getInstance();
		long timeInMillis = fecha.getTime();
		calendar.setTimeInMillis(timeInMillis);
		calendar.add(Calendar.DATE, dias);
		Date fechaAjustada = calendar.getTime();
		return fechaAjustada;
	}
	
	public static int getDiaSemana(Date fecha) {
		Calendar calendar = Calendar.getInstance();
		long timeInMillis = fecha.getTime();
		calendar.setTimeInMillis(timeInMillis);
		int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
		return diaSemana;
	}

}
