package com.orange.pns.monit.ressource;


import com.orange.pns.monit.api.IHistory;
import com.orange.pns.monit.connect.*;
import com.orange.pns.monit.represntation.*;
import com.orange.pns.monit.service.CalculateInterval;

import com.google.common.net.HttpHeaders;

import java.io.IOException;
import java.util.ArrayList;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class CapacityListResource implements IHistory{
	
	 private ArrayList<Double> capacity;
	 Connect connect;
	
	public CapacityListResource(String hostname, String login, String password, String dbname)
	{
		connect=new Connect();
		connect.connectInfluxdb(hostname, login, password, dbname);
	}
	
	@Override
	public Response getHistory(String starttime,String endtime) throws IOException
	{
		try{
			String interval=new CalculateInterval(starttime,endtime).calculate();		
			if(!interval.equals("n/a"))
			{
				capacity=connect.queryCapHistory(starttime, endtime, interval);
				RepresentList bodies=new RepresentList(capacity,interval);
				return Response.ok(bodies).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).header(HttpHeaders.SERVER,HeaderConstants.Server)
						.header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET").header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*").header(HttpHeaders.CACHE_CONTROL, "no cache")
						.build();
			}
			else
			{					
				throw new IllegalArgumentException("Start time or end time is not correct");
			}
		}catch(NullPointerException e)
		{
			throw new IllegalArgumentException("Start time or end time is missing");
		}catch(IOException e)
		{
			throw new IOException("Cannot connect to InlfuxDB");
		}
	}
	
	
}
