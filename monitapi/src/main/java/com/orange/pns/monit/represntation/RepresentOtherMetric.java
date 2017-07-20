package com.orange.pns.monit.represntation;

import java.util.ArrayList;

import com.orange.pns.monit.model.*;

public class RepresentOtherMetric {
	
	ArrayList<MetricModel> result;
	
	public RepresentOtherMetric(ArrayList<MetricModel> result)
	{
		this.result=result;
	}
	
	public ArrayList<MetricModel> getResult()
	{
		return this.result;
	}
	
	
}
