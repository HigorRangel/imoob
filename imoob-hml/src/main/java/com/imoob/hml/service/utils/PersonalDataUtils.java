package com.imoob.hml.service.utils;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

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
}
