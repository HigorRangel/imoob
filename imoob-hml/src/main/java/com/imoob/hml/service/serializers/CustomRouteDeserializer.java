package com.imoob.hml.service.serializers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.imoob.hml.model.Route;
import com.imoob.hml.model.Route;
import com.imoob.hml.repository.RouteRepository;
import com.imoob.hml.service.exceptions.ResourceNotFoundException;

public class CustomRouteDeserializer extends StdDeserializer<Route> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	@Autowired
	private RouteRepository routeRepository;

//	@Autowired
//	private ObjectMapper objectMapper;

	public CustomRouteDeserializer(Class<?> vc) {
		super(vc);
	}

	public CustomRouteDeserializer(RouteRepository routeRepository) {
	        this(Route.class);
	        this.routeRepository = routeRepository;
	    }


	@Override
	public Route deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {

		ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

		Route route = mapper.readValue(p, Route.class);
	
//		String id = p.getValueAsString();

		Long id = route.getId();
		if (id == null || id == 0) {
	        return route;
	    }
//	    Long routeId = Long.parseLong(id);
	    
	    Route routeReturn = routeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id, Route.class));
	    return routeReturn;
		
		
		
//		ObjectMapper mapper = new ObjectMapper();
//        ObjectCodec codec = p.getCodec();
//
//		JsonNode node = codec.readTree(p);
//        Long id = node.asLong();
//		return routeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id, Route.class));

	}

}
