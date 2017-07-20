package com.orange.pns.monit;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.server.AbstractServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.listing.ApiListingResource;
import lombok.val;
//import orange.busaas.api.application.config.ApplicationConfiguration;
import lombok.extern.slf4j.Slf4j;
import java.text.ParseException;
import java.util.EnumSet;

import javax.servlet.DispatcherType;

import com.orange.pns.monit.exception.RuntimeExceptionMapper;
import com.orange.pns.monit.ressource.*;
import com.orange.skeleton.api.exceptions.mappers.WebApplicationExceptionMapper;
import com.orange.skeleton.container.filters.ApiHeadersFilter;
import com.orange.skeleton.interceptors.RequestFilter;
import com.orange.skeleton.interceptors.ResponseFilter;

@Slf4j
public class MonitApplication extends Application<MonitConfiguration>
{
	public static void main(String[] args) throws Exception {
        new MonitApplication().run(args);
    }
	
	@Override
    public String getName() {
        return "monit";
    }
	
	 @Override
	    public void initialize(Bootstrap<MonitConfiguration> bootstrap) {
		 bootstrap.addBundle(new AssetsBundle("/swagger", "/swagger-ui", "index.html?url=../../swagger.json"));
		 //JmxReporter.forRegistry(bootstrap.getMetricRegistry()).build().start();   
	 }

    @Override
    public void run(MonitConfiguration configuration,Environment environment) 
    {
    	try {
    	CapacityResource resource;
		
			resource = new CapacityResource(configuration.getInfluxhost(),
														   configuration.getInfluxlogin(),
														   configuration.getInfluxpassword(),
														   configuration.getDbname(),
														   configuration.getReadlatencyseuil1(),
														   configuration.getReadlatencyseuil2(),
														   configuration.getLoadseuil1(),
														   configuration.getLoadseuil2(),
														   configuration.getHeapseuil1(),
														   configuration.getHeapseuil2(),
														   configuration.getHeaptotal()
														   );
		
    	CapacityListResource listResource = new CapacityListResource(configuration.getInfluxhost(),
				   													 configuration.getInfluxlogin(),
				   													 configuration.getInfluxpassword(),
				   													 configuration.getDbname());
    	MetricCassandra othermetric=new MetricCassandra(configuration.getInfluxhost(),
					 									configuration.getInfluxlogin(),
					 									configuration.getInfluxpassword(),
					 									configuration.getDbname());
    	
    	environment.jersey().register(resource);
    	environment.jersey().register(listResource);
    	environment.jersey().register(othermetric);
    	registerFilters(configuration, environment);
    	registerExceptions(configuration,environment);
    	registerSwagger(configuration, environment);
    	
    	} catch (ParseException e) {
			e.printStackTrace();
			//log.error("Monit Application: Connection Error");
		}
    	

    }
    private void registerFilters(MonitConfiguration config, Environment environment) {
    	//Skeleton Create RequestContext from internal request ID, SID and loaded configurations
    	environment.jersey().register(RequestFilter.class);
    	environment.jersey().register(ResponseFilter.class);
    	
        //map webappex throw by skeleton
    	environment.jersey().register(WebApplicationExceptionMapper.class);

        //Generate Internal ID in request headers, force 200, Set the Request and Internal Id in the request attributes (for access logs)
        environment.servlets().addFilter("Custom-Filter-Name", new ApiHeadersFilter()).addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        //initialize files path
        environment.servlets().addServletListeners(new com.orange.skeleton.Bootstrap());
    }
    
    private void registerExceptions(MonitConfiguration config, Environment environment)
    {
    	val sf= (AbstractServerFactory) config.getServerFactory();
    	sf.setRegisterDefaultExceptionMappers(false);    	
    	environment.jersey().register(new RuntimeExceptionMapper());
    }
    private void registerSwagger(MonitConfiguration config, Environment env) {
        SwaggerConfiguration swagger = config.getSwaggerConfig();
        if (swagger != null) {
            // Swagger providers
            env.jersey().register(new ApiListingResource());
        //    SwaggerHelper.setupCors4Swagger(env);
        } else {
            log.info("No swagger configuration");
        }
    }
}
