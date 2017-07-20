//package com.orange.pns.monit;
//
//import java.util.EnumSet;
//
//import javax.servlet.DispatcherType;
//import javax.servlet.FilterRegistration;
//import javax.ws.rs.HttpMethod;
//import javax.ws.rs.core.HttpHeaders;
//
//import org.eclipse.jetty.servlets.CrossOriginFilter;
//
//import com.google.common.base.Joiner;
//import com.google.common.collect.ImmutableList;
//import com.google.common.collect.ImmutableMap;
//
//import io.dropwizard.setup.Environment;
//
//public final class SwaggerHelper {
//
//    public static void setupCors4Swagger(Environment environment) {
//
//        ImmutableList<String> methods = ImmutableList.of(HttpMethod.OPTIONS, HttpMethod.GET, HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.HEAD);
//
//        ImmutableList<String> headers = ImmutableList.of(HttpHeaders.ACCEPT, HttpHeaders.CONTENT_TYPE, HttpHeaders.CONTENT_LENGTH,
//                HttpHeaders.AUTHORIZATION, HttpHeaders.CACHE_CONTROL, HttpHeaders.ACCEPT_CHARSET, HttpHeaders.ACCEPT_ENCODING,
//                HttpHeaders.ACCEPT_LANGUAGE, "X-Request-ID",HttpHeaders.USER_AGENT);
//
//        // Configure CORS parameters
//        ImmutableMap<String, String> parameters = ImmutableMap.of(CrossOriginFilter.ALLOWED_METHODS_PARAM,
//                Joiner.on(',').join(methods), CrossOriginFilter.ALLOWED_ORIGINS_PARAM,"*", CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*", CrossOriginFilter.ALLOWED_HEADERS_PARAM, Joiner.on(',').join(headers), CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
//
//        FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
//        cors.setInitParameters(parameters);
//
//        // Add URL mapping
//        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
//    }
//
//}
