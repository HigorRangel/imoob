package com.imoob.hml.service.serializers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.imoob.hml.model.Route;
import com.imoob.hml.model.DTO.route.RouteDTO;
import com.imoob.hml.repository.RouteRepository;

public class CustomRouteSerializer extends StdSerializer<Route> {

	@Autowired
    private RouteRepository routeRepository;
	
	@Autowired
	private ObjectMapper objectMapper;


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomRouteSerializer() {
		this(Route.class);
	}
	
	public CustomRouteSerializer(RouteRepository routeRepository, ObjectMapper mapper) {
		this(Route.class);

	    this.routeRepository = routeRepository;
	    this.objectMapper = mapper;

	}

	public CustomRouteSerializer(Class<Route> t) {
		super(t);
	}

	@Override
	public void serialize(Route route, JsonGenerator jsonGenerator, SerializerProvider serializers)
			throws IOException {
		RouteDTO routeDTO = new RouteDTO(route);
        objectMapper.writeValue(jsonGenerator, routeDTO);
	}

}
