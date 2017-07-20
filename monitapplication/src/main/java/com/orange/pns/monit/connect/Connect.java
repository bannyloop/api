package com.orange.pns.monit.connect;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.InfluxDB.ConsistencyLevel;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Series;


import com.orange.pns.monit.model.MetricModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Connect {
		
		String url;
		String username;
		String password;
		String dbName;
		String queryCommand;
		InfluxDB influxDB;
		QueryResult queryResult;
		String dbType;		//influxdb or prometheus or ..
		
		public Connect()
		{
			
		}
		
		public void connectInfluxdb(String url,String username,String password,String dbname)
		{
			this.url=url;
			this.username=username;
			this.password=password;
			this.dbName=dbname;
			this.dbType="influxdb";
			this.influxDB = InfluxDBFactory.connect(url, username, password);

		}
		
		public ArrayList<Double> queryInfluxdbList(String queryCommand) throws IOException
		{
			//log.debug("Connect : Start query InfluxDB");
				ArrayList<Double> list=new ArrayList<Double>();
				if(dbType.equals("influxdb"))
				{
					this.queryCommand= queryCommand;
			    	Query query = new Query(queryCommand, this.dbName);
			    	this.queryResult= influxDB.query(query);
			    	
			    	if(queryResult.getResults().get(0).getSeries()!=null)
			    	{
						Series serie=queryResult.getResults().get(0).getSeries().get(0);
				     	for(int i=0; i<serie.getValues().size();i++)
				     	{   
				     		if((Double)serie.getValues().get(i).get(1)!=null && (Double)serie.getValues().get(i).get(1)!=0)
				     		{	    			
				     				     		
				     			list.add((Double)serie.getValues().get(i).get(1));
				     		}
				     	}
				     	return list;
			     	}
			    	else
			    	{
			    			log.debug("InfluxDB query result is null");
			    			return null;
			    	}
				}
				else
				{
//						log.debug("Database type is not InfluxDB");
//						return null;
					throw new RuntimeException("InfluxDB config in Monit Configuration file is not correct");
				}
			}

			
		
		public ArrayList<Double> queryInfluxdbHistory(String queryCommand) throws IOException
		{
			
				ArrayList<Double> capacity=new ArrayList<Double>();
				if(dbType.equals("influxdb"))
				{
					this.queryCommand= queryCommand;
			    	Query query = new Query(queryCommand, this.dbName);
			    	this.queryResult= influxDB.query(query);
			    	
			    	if(queryResult.getResults().get(0).getSeries()!=null)
			    	{
						Series serie=queryResult.getResults().get(0).getSeries().get(0);
						
				     	for(int i=0; i<serie.getValues().size();i++)
				     	{   
				     		if((Double)serie.getValues().get(i).get(1)!=null && (Double)serie.getValues().get(i).get(1)!=0)
				     		{	    			
				     				     		
				     			capacity.add((Double)serie.getValues().get(i).get(1));
				     		}
				     		else
				     		{
				     			capacity.add((double) 0);
				     		}
				     	}
				     	//log.debug("Connect- Capacity history query result is returned");
				     	return capacity;
			     	}
			    	else
			    		{
			    			//log.debug("Connect- Query result is null because no result is returned by InfluxDB");
			    			return null;
			    		}
				}
				else
				{
					//log.debug("Connect- Query result is null because database is not InfluxDB");
					//return null;
					throw new RuntimeException("InfluxDB config in Monit Configuration file is not correct");
				}
			
			
		}
		
		public ArrayList<MetricModel> queryOtherMetric(ArrayList<String> fields,String queryCommand) throws IOException
		{
			if(queryCommand!=null)
			{
				ArrayList<MetricModel> result=new ArrayList<MetricModel>();
				QueryResult queryResult=queryInfluxdb(this.dbName,queryCommand);
				if(queryResult.getResults().get(0).getSeries()!=null)
				{
					List<Series> series=queryResult.getResults().get(0).getSeries();
					for(int i=0;i<series.get(0).getValues().size();i++)
					{
						String metric=series.get(0).getName();
						HashMap<String,String> fieldValue=new HashMap<String,String>();
						for(int j=0;j<fields.size();j++)
						{
							if(series.get(0).getValues().get(i).get(j+1)!=null)
								fieldValue.put(fields.get(j), series.get(0).getValues().get(i).get(j+1).toString());
							else
								fieldValue.put(fields.get(j), String.valueOf(0));
						}
						String date=series.get(0).getValues().get(i).get(0).toString();
						date.replace('T','-');
						date.replace('Z', ' ');
						MetricModel resultpiece=new MetricModel(metric,fieldValue,date);
						result.add(resultpiece);
					}
					log.debug("Connect - Query Result is returned successefully");
					return result;
				}
				else
					{
						//log.debug("Connect - Query Result returned by InfluxDB is null");
						return null;
					}
			}
			else
				{
					throw new IllegalArgumentException("Query Param is not correct");
				}
		}
		
		public QueryResult queryInfluxdb(String dbName,String queryCommand) throws IOException,RuntimeException
		{
//			try
//			{
				if(dbType.equals("influxdb"))
				{
					this.dbName=dbName;
					this.queryCommand= queryCommand;
					System.out.println("Connect querycommand="+queryCommand);
			    	Query query = new Query(queryCommand, dbName);
			    	System.out.println("Connect Query Command est = "+queryCommand);
			    	this.queryResult= influxDB.query(query);
			    	return this.queryResult;
				}
				else
					return null;
//			}catch(RuntimeException e)
//			{
//				e.printStackTrace();
//				throw new IllegalArgumentException("Query param are not correct");
//			}
			
		}
		
		public void writeCapacity(double capacity)
		{
			BatchPoints batchPoints = BatchPoints.database(this.dbName)    										 
					 .retentionPolicy("autogen")
					 .consistency(ConsistencyLevel.ALL)
					 .build();
			Point point1 = Point.measurement("capacity")
					.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
					.addField("capacity", capacity)
					.build();
			batchPoints.point(point1);
			influxDB.write(batchPoints);
		}
		
		public ArrayList<Double> queryCapHistory(String starttime,String endtime,String interval) throws IOException 
		{
			log.debug("Connect Start Query Capacity HIstory");
			String start=changeDateFormat(starttime);
			String end=changeDateFormat(endtime);
			String queryCommand="SELECT mean(\"capacity\") FROM \"capacity\" WHERE time >= '"+ start + "' AND time <= '"+end + "' GROUP BY time("+interval+")";
			log.debug("Connect Query Command is :"+queryCommand);
			ArrayList<Double> history=this.queryInfluxdbHistory(queryCommand);
			return history;
		}
		
		public String changeDateFormat(String time) 
		{
			String timeChange=time.substring(0, 10)+"T"+time.substring(11, 13)+":"+time.substring(14, 16)+":"+time.substring(17, 19)+"Z";
			return timeChange;
		}
		
		
		
	
}
