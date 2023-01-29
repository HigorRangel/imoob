package com.imoob.hml.service.utils;

import java.text.Normalizer;
import java.text.Normalizer.Form;

public class StringUtils {
	
	/**
	 * Checks if value is null or Empty
	 * @param value
	 * @return
	 */
	public static boolean isNullOrEmpty(String value) {
		return value == null || value.isEmpty();
	}
	
	/**
	 * Checks if value is null, empty or blank
	 * @param value
	 * @return
	 */
	public static boolean isNullEmptyOrBlank(String value) {
		return value == null || value.isEmpty() || value.isBlank();
	}
	
	/**
	 * Remove diacritical marks from a string
	 * @param value
	 * @return
	 */
	public static String removeDiacriticalMarks(String value) {
		return Normalizer.normalize(value, Form.NFD);
	}
	
	/**
	 * Remove diacritical marks from a string
	 * @param value
	 * @return
	 */
	public static boolean containsDiacriticalMarks(String value) {
		return !Normalizer.isNormalized(value, Form.NFD);
	}
	
	
	/**
	 * Checks if value is only numbers
	 * @param value
	 * @return
	 */
	public static boolean isOnlyNumbers(String value) {
		return value.matches("[0-9]+");
	}
}
