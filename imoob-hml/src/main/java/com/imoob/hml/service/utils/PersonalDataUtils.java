package com.imoob.hml.service.utils;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.imoob.hml.service.exceptions.GeneralException;

public class PersonalDataUtils {
	
	public static boolean isValidEmailAddress(String email) {
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
			return true;
		} catch (AddressException e) {
			return false;
		}
	}
	
	/**
	 * Removes the symbols ".", "-" and "/" from a CNPJ.
	 *
	 * @param cnpj The CNPJ to be treated
	 * @return The CNPJ without symbols
	 */
	public static String cnpjRemoveSymbols(String cnpj) {
		return cnpj.replace(".", "").replace("-", "").replace("/", "");
	}

	/**
	 * Adds the symbols ".", "/" and "-" to a CNPJ.
	 * Checks if the CNPJ is valid before adding the symbols.
	 *
	 * @param cnpj The CNPJ to be treated
	 * @return The CNPJ with symbols
	 * @throws GeneralException If the CNPJ is invalid
	 */
	public static String cnpjPutSymbols(String cnpj) {
		cnpj = cnpjRemoveSymbols(cnpj);

		if (!StringUtils.isNullOrEmpty(cnpj) && StringUtils.isOnlyNumbers(cnpj) && cnpj.length() == 14) {
			return cnpj.replaceFirst("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
		}
		throw new GeneralException("O CNPJ não foi inserido corretamente. Deve ter 14 dígitos, sendo somente números."); 
	}

}
