package com.orange.pns.monit.ressource;

import java.io.IOException;
import java.util.ArrayList;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.common.net.HttpHeaders;
import com.orange.pns.monit.api.IMetricCassandra;
import com.orange.pns.monit.connect.Connect;
import com.orange.pns.monit.model.MetricModel;
import com.orange.pns.monit.represntation.RepresentOtherMetric;
import com.orange.pns.monit.service.GenerateQueryCommand;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MetricCassandra implements IMetricCassandra {
	
	Connect connect;
	
	public MetricCassandra(String hostname,String login,String password, String dbname)
	{
		connect=new Connect();
		connect.connectInfluxdb(hostname, login, password, dbname);
	}
	
	@Override
	public Response getMetricCassandra(String starttime,String endtime,
										String metric,String fields) throws IOException
	{
			
			GenerateQueryCommand generator=new GenerateQueryCommand(starttime,endtime,metric,fields);
			String queryCommand=generator.generate();
			System.out.println("MetricCassandra queryCommand = "+queryCommand);
			ArrayList<MetricModel> result;
			try {
				result = connect.queryOtherMetric(generator.getFields(),queryCommand);
			} catch (IOException e) {
				e.printStackTrace();
				throw new IOException("Cannot connect to InfluxDB");
			}
				if(result!=null)
				{
					RepresentOtherMetric bodies=new RepresentOtherMetric(result);
					return Response.ok(bodies).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).header(HttpHeaders.SERVER,HeaderConstants.Server)
							.header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET").header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*").header(HttpHeaders.CACHE_CONTROL, "no cache")
							.build();
				}
				else
				{
					throw new IllegalArgumentException("Query parameters are not correct.");
				}
			
			

		
	}
}