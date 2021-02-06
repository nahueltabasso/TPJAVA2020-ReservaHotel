package utils;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

	/**
	 * Devuelve true si una cadena contiene algun caracter numerico,
	 * caso contrario retorna false
	 * @param cad
	 * @return
	 */
	public static boolean cadContainsDigit(String cad) {
		for (int i = 0; i < cad.length(); i++) {
			char caracter = cad.charAt(i);
			if (Character.isDigit(caracter)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna la cadena que recibe como parametro en mayusculas
	 * @param cad
	 * @return
	 */
	public static String cadToUpperCase(String cad) {
		return cad.toUpperCase();
	}
	
	/**
	 * Retorna true si una cadena que recibe como parametros
	 * contiene alguna caracter no numerico
	 * @param cad
	 * @return
	 */
	public static boolean cadContainsLetters(String cad) {
		for (int i = 0; i < cad.length(); i++) {
			char caracter = cad.charAt(i);
			if (!Character.isDigit(caracter)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true cuando la lista que recibe como parametro
	 * es null o esta vacia
	 * @param list
	 * @return
	 */
    public static boolean isNullOrEmpty(List<?> list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }
   
    /**
     * Retorna true cuando el email es valido, caso contrario retorna false
     * @param email
     * @return
     */
	public static boolean validaEmail(String email) {
		// Patron para validar el email
		Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		Matcher match = pattern.matcher(email);
		if (match.find()) {
			return true;
		} else {
			return false;
		}
	}
	
    /**
     * Retorna true cuando el precio es valido, caso contrario retorna false
     * @param precio
     * @return
     */
	public static boolean validaPrecio(String precio) {
		final String regExp = "[0-9]+([,.][0-9]{1,2})?";
		final Pattern pattern = Pattern.compile(regExp);
		if (pattern.matches(regExp, precio)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Retorna true si el cuit de una persona es valido
	 * @param cuit
	 * @param dni
	 * @param sexo
	 * @return
	 */
	public static boolean validarCuit(String cuit, String dni, String sexo) {
		// Eliminamos todos los caracteres que no sean numeros
		cuit = cuit.replaceAll("[^\\d]", "");
		// Controllamos si son 11 numeros lo que quedaron, caso contrario return false
		if (cuit.length() != 11) {
			return false;
		}
		// Separamos en partes al cuit
		String primeraParteCuit = cuit.substring(0, 2);
		String segundaParteCuit = cuit.substring(2, 10);
		
		if (sexo.equalsIgnoreCase("M")) {
			if (!primeraParteCuit.equalsIgnoreCase("20")) {
				return false;
			}
		} else {
			if (!primeraParteCuit.equalsIgnoreCase("27")) {
				return false;
			}
		}
		// Validamos la segunda parte debe coincidir con el dni 
		if (!segundaParteCuit.equalsIgnoreCase(dni)) {
			return false;
		}
		// Si pasa todas las validaciones return true
		return true;
	}
	
	/**
	 * Retorna true si la password es segura caso contrario retorna false
	 * @param password
	 * @return
	 */
	public static boolean validaSeguridadPassword(String password) {
		Integer size = password.length();
		if (size >= 8) {
			if (cadContainsLetters(password) && cadContainsDigit(password)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Retorna la diferencia de dias entre dos fechas
	 * @param fecha1
	 * @param fecha2
	 * @return
	 */
	public static int diferenciaEntreDosFechas(Date fecha1, Date fecha2) {
		int dias = (int) ((fecha2.getTime() - fecha1.getTime()) / 86400000);
		return dias;
	}


}
