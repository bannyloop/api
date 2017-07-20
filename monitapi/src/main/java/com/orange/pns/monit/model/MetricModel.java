package com.orange.pns.monit.model;

import java.util.Date;
import java.util.HashMap;

public class MetricModel {
	String metric;
	HashMap<String,String> fieldValue;
	String time;
	
	public MetricModel(String metric,HashMap<String,String> fieldValue,String time)
	{
		this.metric=metric;
		this.fieldValue=fieldValue;
		this.time=time;
	}
	
	public String getMetric()
	{
		return this.metric;
	}
	
	public void setMetric(String metric)
	{
		this.metric=metric;
	}
	
	public HashMap<String,String> getFieldValue()
	{
		return this.fieldValue;
	}
	
	public void setFieldValue(HashMap<String,String> fieldValue)
	{
		this.fieldValue=fieldValue;
	}
	
	public String getTime()
	{
		return this.time;
	}
	
	public void setTime(String time)
	{
		this.time=time;
	}
}
