package com.orange.pns.monit;

import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.orange.pns.monit.api.ICapacity;

import io.swagger.jaxrs.config.BeanConfig;
import lombok.Data;
import lombok.val;

@Data
public class SwaggerConfiguration implements Supplier<BeanConfig> {
    
    @NotNull
    @JsonProperty("resourcePackage")
    private String resourcePackage = ICapacity.class.getPackage().getName(); //"orange.busaas.api.application.resources";
    
    @NotNull
    @JsonProperty("title")
    private String title = "Monit service API";
   
    @NotNull
    @JsonProperty("basePath")
    private String basePath = "/monit/v1";
    
    @NotNull
    @JsonProperty("version")
    private String version = "0.1.0";

    @Override
    public @Nonnull BeanConfig get() {

        val config = new BeanConfig();

        config.setResourcePackage(resourcePackage);
        config.setTitle(title);
        config.setVersion(version);
        config.setBasePath(basePath);
        config.setScan(true);

        return config;
    }
}
