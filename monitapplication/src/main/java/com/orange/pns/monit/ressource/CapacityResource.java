package com.orange.pns.monit.ressource;



import com.google.common.net.HttpHeaders;
import com.orange.pns.monit.represntation.*;
import com.orange.pns.monit.service.CalculateCapacity;

import com.orange.pns.monit.api.ICapacity;

import java.io.IOException;
import java.text.ParseException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class CapacityResource implements ICapacity{
	

	private double capacity;
	private String hostname;
	private String login;
	private String password;
	private String dbname;
    private double readlatencyseuil1;
    private double readlatencyseuil2;
    private double loadseuil1;
    private double loadseuil2;
    private double heapseuil1;
    private double heapseuil2;
	private CalculateCapacity calculate;

	public CapacityResource(String hostname,String login, String password,String dbname, 
			double readseuil1, double readseuil2, double loadseuil1, double loadseuil2,
			double heapseuil1, double heapseuil2, double heaptotal) throws ParseException
	{
		this.hostname=hostname;
		this.login=login;
		this.password=password;
		this.dbname=dbname;
		this.readlatencyseuil1=readseuil1;
		this.readlatencyseuil2=readseuil2;
		this.loadseuil1=loadseuil1;
		this.loadseuil2=loadseuil2;
		this.heapseuil1=heapseuil1*heaptotal/100;
		this.heapseuil2=heapseuil2*heaptotal/100;
		calculate=new CalculateCapacity();	
		
	}

	@Override
	public Response getCapacity(String idClient, String vitesse1) throws IOException {
          try {
        	     double vitesse2=Double.parseDouble(vitesse1);
				 capacity=calculate.calCapacity(hostname, login, password, dbname,
													   readlatencyseuil1,readlatencyseuil2,
													   loadseuil1,loadseuil2,
													   heapseuil1,heapseuil2,vitesse2
													   );
				
			
			
				 Represent bodies=new Represent(capacity);
				 return Response.ok(bodies).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).header(HttpHeaders.SERVER,HeaderConstants.Server)
						.header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET").header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*").header(HttpHeaders.CACHE_CONTROL, "no cache")
						.build();	
			  }catch(NumberFormatException e)
           	   {
				 e.printStackTrace(); 
				 throw new IllegalArgumentException(e.getMessage());
               } 
              catch (IOException e) {					
			     e.printStackTrace();
				 throw new IOException(e.getMessage());
			  }
		
	}
}
