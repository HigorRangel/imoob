package com.imoob.hml.service.utils;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GeneralUtils {
	public String convertObjectToJson(Object object) throws JsonProcessingException {
		if (object == null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}

	public static Boolean isValidURL(String url) {
		String[] schemes = { "http", "https"};
		
		if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
		
		UrlValidator urlValidator = new UrlValidator(schemes, UrlValidator.ALLOW_LOCAL_URLS);
		return urlValidator.isValid(url);
	}
}
