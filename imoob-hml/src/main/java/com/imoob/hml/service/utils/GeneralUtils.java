package com.imoob.hml.service.utils;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

@Service
public class GeneralUtils {
	
	private static final ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build().setSerializationInclusion(JsonInclude.Include.NON_NULL);

	public static String convertObjectToJson(Object object) throws JsonProcessingException {
		if (object == null) {
			return null;
		}
		return objectMapper.writeValueAsString(object);
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
