package com.orange.pns.monit.exception;
import java.io.IOException;
import java.util.Map;
import java.util.function.Function;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.google.common.collect.ImmutableMap;
import com.orange.skeleton.api.exceptions.ApiException;
import com.orange.skeleton.api.exceptions.CustomInternalStatus;
import com.orange.skeleton.api.exceptions.DefaultInternalStatus;
import com.orange.skeleton.api.exceptions.mappers.ApiExceptionMapper;
import com.sun.el.parser.ParseException;

import lombok.NonNull;
import lombok.val;
import lombok.extern.slf4j.Slf4j;


@Provider
@Slf4j
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

	//@Inject
	private ApiExceptionMapper skeletonExceptionMapper;
	
	@NonNull
    private final Map<Class<? extends Throwable>, Function<Throwable, CustomInternalStatus>> functions;
	
    public RuntimeExceptionMapper() {
        skeletonExceptionMapper =new ApiExceptionMapper();
    	functions = defaultHandlers();
    }
	
//  public RuntimeExceptionMapper() {
//      
//  }
//  
//	@Override
//	public Response toResponse(RuntimeException exception) {
//		
//		return Response.status(500).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).header(HttpHeaders.SERVER,HeaderConstants.Server)
//				.header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET").header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*").header(HttpHeaders.CACHE_CONTROL, "no cache")
//				.build();
//	}
    
	@Override
	public Response toResponse(RuntimeException exception) {
		val apiException = new ApiException(toStatus(exception),exception);
		log.debug("An exception occurred:", exception);
		return skeletonExceptionMapper.toResponse(apiException);
	}
	
	protected CustomInternalStatus toStatus(Throwable exception)
	{
		Class<?> klass = exception.getClass();

        while( klass != null){
        	log.debug("klass is not null");
            val handler = functions.get(klass);

            if( handler != null ){
            	log.debug("handler is not null");
                return handler.apply(exception);
            }

            klass = klass.getSuperclass();
        }

        return DefaultInternalStatus.API_INTERNAL_ERROR.externalDetails(exception.getMessage()).build(exception.getMessage());
	}
	
	protected Map<Class<? extends Throwable>, Function<Throwable, CustomInternalStatus>> defaultHandlers() {
		
		final Function<Throwable, CustomInternalStatus> networkErr =
				(e) -> (e.getCause() != null)
				? toStatus(e.getCause())
			    : DefaultInternalStatus.NETWORK_ERROR.externalDetails(e.getMessage()).build(e.getMessage());
		
		final Function<Throwable, CustomInternalStatus> errRuntime =
	            (e) -> (e.getCause() != null)
	            ? toStatus(e.getCause())
	            : DefaultInternalStatus.API_INTERNAL_ERROR.externalDetails(e.getMessage()).build(e.getMessage());
		
		return ImmutableMap.<Class<? extends Throwable>, Function<Throwable, CustomInternalStatus>>builder()
				.put(IOException.class, networkErr)
				.put(IllegalArgumentException.class, (e) -> DefaultInternalStatus.INVALID_PARAMETER.externalDetails(e.getMessage()).build(e.getMessage()))
				.put(ParseException.class, (e) -> toStatus(e.getCause()))
                .put(RuntimeException.class, errRuntime)
				.build();
		
	}
}
 