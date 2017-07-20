package com.orange.pns.monit.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import com.orange.pns.monit.connect.Connect;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CalculateCapacity {
	private double capacity;
	private double readLatency;
	private double load;
	private double heap;
	private double vitesse;
	private Connect connect;
	public CalculateCapacity()
	{
		this.capacity=0;
		new ArrayList<Double>();
	}
	
	public double calCapacity(String host,String login,String password,String dbname,
			double readseuil1, double readseuil2, double loadseuil1, double loadseuil2,
							 double heapseuil1, double heapseuil2, double vitesse) throws IOException 
	{	
		 this.vitesse =vitesse;
		 log.info("Calculate Cassandra Capacity");
		 // connect with the database -InfluxDB or Prometheus
		 connect = new Connect();
		 log.debug("InfluxDB Connectting: {}",host);
		 log.debug("InfluxDB Connectting to database :{}", dbname);
		 connect.connectInfluxdb(host, login, password,dbname);
		
			try {
				getReadLatency();
				getLoad();
				getHeap();
			}catch(NullPointerException e)
			{
				e.printStackTrace();
				throw new RuntimeException("InfluxDB config in Monit configuration file is not correct.");
			}
			catch(ParseException e)
			{
				e.printStackTrace();
				throw new RuntimeException("This is ParseException");
			}
			catch(IOException e)
			{
				e.printStackTrace();
				throw new IOException("Cannot conncect to InfluxDB");
			}
			

		

		 // strategy to calculate the capacity
		 if(this.readLatency>0 && this.load>0 && this.heap>0 && 
		    this.readLatency<readseuil1 && this.load < loadseuil1 && this.heap < heapseuil1)
		 {
			 this.capacity=100;
			 connect.writeCapacity(this.capacity+this.vitesse);
			 return this.capacity;
		 }
		 else if((this.readLatency>readseuil1 && this.readLatency<readseuil2)||
				 (this.load>loadseuil1 && this.load <loadseuil2)||
				 (this.heap>heapseuil1 && this.heap < heapseuil2))
		 {
			 this.capacity=50;
			 connect.writeCapacity(this.capacity+this.vitesse);
			 return this.capacity;
		 }
		 else if(this.readLatency>readseuil2 || this.load>loadseuil2 || this.heap>heapseuil2)
		 {			
			 this.capacity=-100;
			 connect.writeCapacity(this.capacity+this.vitesse);
			 return this.capacity;
		 }
		 else
		 {	
			 this.capacity=0;
			 connect.writeCapacity(this.capacity+this.vitesse);
			 return this.capacity;
		 }
		 
		 
	}
	private void getReadLatency() throws ParseException, NullPointerException, IOException
	{
		String queryCommand="select mean(\"ReadLatency_75thPercentile\") from \"cassandraColumnFamily\" where time > now() - 1m GROUP BY time(20s)";
		ArrayList<Double> readLatencyList;

			readLatencyList = connect.queryInfluxdbList(queryCommand);
		
//		if(readLatencyList!=null)
//		{
			if(readLatencyList.size()>0)
			{
				//log.info("InfluxDB: ReadLatency is charged");
				this.readLatency=readLatencyList.get(0)/1000;
				for(int i=1;i<readLatencyList.size();i++)
				{
					if(this.readLatency<readLatencyList.get(i))
					{
						this.readLatency=readLatencyList.get(i)/1000;
					}
				}
			}
			else
			{
				this.readLatency=-1;
				log.error("InfluxDB: readLatency failed to charge");
			}
//		}
//		else
//			this.readLatency=-1;
	}
	
	private void getLoad() throws ParseException, NullPointerException, IOException
	{
		String queryCommand="select mean(\"load1\") from \"system\" where time > now() - 1m GROUP BY time(20s)";
		ArrayList<Double> loadList=connect.queryInfluxdbList(queryCommand);
//		if(loadList!=null)
//		{
			if(loadList.size()>0)
			{
				//log.info("InfluxDB: Load is charged");
				this.load=loadList.get(0);
				for(int i=1;i<loadList.size();i++)
				{
					if(this.load<loadList.get(i))
					{
						this.load=loadList.get(i);
					}
				}
			}
			else
			{
				this.load=-1;
				log.error("InfluxDB: load failed to charge");
			}
//		}
//		else
//			this.load=-1;
	}
	
	private void getHeap() throws ParseException, NullPointerException, IOException
	{
		String queryCommand="select mean(\"HeapMemoryUsage\") from \"javaMemory\" where time > now() - 1m GROUP BY time(20s)";
		ArrayList<Double> heapList=connect.queryInfluxdbList(queryCommand);
//		if(heapList!=null){
			if(heapList.size()>0)
			{
				//log.info("InfluxDB: Heap is charged");
				this.heap=heapList.get(0)/(1024*1024*1024);
				for(int i=1;i<heapList.size();i++)
				{
					if(this.heap<(heapList.get(i)/(1024*1024*1024)))
					{
						this.heap=heapList.get(i)/(1024*1024*1024);
					}
				}
			}
			else
			{
				this.heap=-1;
				log.error("InfluxDB: heap failed to charge");
			}
//		}
//		else
//			this.heap=-1;
	}
	

}
