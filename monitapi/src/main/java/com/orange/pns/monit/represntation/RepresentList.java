package com.orange.pns.monit.represntation;

import java.util.ArrayList;


public class RepresentList {

	private String intertal;
	private ArrayList<Double> capacityList;	
	
	public RepresentList(ArrayList<Double> capacity, String interval)
	{
		this.intertal=interval;
		this.capacityList=capacity;
	}

	public String getInterval()
	{
		return this.intertal;
	}
	public ArrayList<Double> getCapacity()
	{
		return this.capacityList;
	}
	
}
