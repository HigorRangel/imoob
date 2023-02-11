package com.imoob.hml.service.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RequestUtils {
	

    private static AbstractHandlerMethodMapping<RequestMappingInfo> requestMappingHandlerMapping;

//    public static void setRequestMappingHandlerMapping(AbstractHandlerMethodMapping<RequestMappingInfo> handlerMapping) {
//        requestMappingHandlerMapping = handlerMapping;
//    }
//
//    public static boolean isRouteValid(String route) {
//        return requestMappingHandlerMapping.getHandler(route) != null;
//    }
    
    //TODO - Vai ser necessário criar uma classe que contenha todas as rotas. A cada vez que uma nova rota é criada, será necessário adicionar um novo registro.
}
