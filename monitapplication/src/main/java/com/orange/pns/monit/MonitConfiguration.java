package com.orange.pns.monit;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import lombok.Data;


@Data
public class MonitConfiguration extends Configuration{
	@NotEmpty
    private String influxhost;
	
	@NotEmpty
    private String influxlogin;
	
	@NotEmpty
    private String influxpassword;
	
	@NotEmpty
    private String dbname;
	
	@NotNull
    private double readlatencyseuil1;
	
	@NotNull
    private double readlatencyseuil2;
	
	@NotNull
    private double loadseuil1;
	
	@NotNull
    private double loadseuil2;
	
	@NotNull
    private double heapseuil1;
	
	@NotNull
    private double heapseuil2;

	@NotNull
    private double heaptotal;
	
	@Valid
	@JsonProperty("swagger")
	private SwaggerConfiguration swaggerConfig;
	
	
	@JsonProperty
    public String getInfluxhost() {
        return this.influxhost;
    }
	@JsonProperty
    public void setInfluxhost(String influxhost) {
        this.influxhost = influxhost;
    }
	
	@JsonProperty
    public String getInfluxlogin() {
        return this.influxlogin;
    }
	
	@JsonProperty
    public String getInfluxpassword() {
        return this.influxpassword;
    }
	
	@JsonProperty
    public void setInfluxlogin(String Influxlogin) {
        this.influxlogin = Influxlogin;
    }
	
	@JsonProperty
    public String getDbname() {
        return this.dbname;
    }
	
	@JsonProperty
    public void setDbname(String dbname) {
        this.dbname = dbname;
    }
	
	@JsonProperty
    public double getReadlatencyseuil1() {
        return this.readlatencyseuil1;
    }
	
	@JsonProperty
    public void setReadlatencyseuil1(double readlatencyseuil1) {
        this.readlatencyseuil1 = readlatencyseuil1;
    }
	
	@JsonProperty
    public double getReadlatencyseuil2() {
        return this.readlatencyseuil2;
    }
	
	@JsonProperty
    public void setReadlatencyseuil2(double readlatencyseuil2) {
        this.readlatencyseuil2 = readlatencyseuil2;
    }
	
	@JsonProperty
    public double getLoadseuil1() {
        return this.loadseuil1;
    }
	
	@JsonProperty
    public void setLoadseuil1(double loadseuil1) {
        this.loadseuil1 = loadseuil1;
    }

	@JsonProperty
    public double getLoadseuil2() {
        return this.loadseuil2;
    }
	
	@JsonProperty
    public void setLoadseuil2(double loadseuil2) {
        this.loadseuil2 = loadseuil2;
    }
	
	@JsonProperty
    public double getHeapseuil1() {
        return this.heapseuil1;
    }
	
	@JsonProperty
    public void setHeapseuil1(double heapseuil1) {
        this.heapseuil1 = heapseuil1;
    }

	@JsonProperty
    public double getHeapseuil2() {
        return this.heapseuil2;
    }
	
	@JsonProperty
    public void setHeapseuil2(double heapseuil2) {
        this.heapseuil2 = heapseuil2;
    }
	
	@JsonProperty
    public double getHeaptotal() {
        return this.heaptotal;
    }
	
	@JsonProperty
    public void setHeaptotal(double heaptotal) {
        this.heaptotal = heaptotal;
    }
}
