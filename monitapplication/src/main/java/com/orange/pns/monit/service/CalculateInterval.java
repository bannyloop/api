package com.orange.pns.monit.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CalculateInterval {
	String starttime;
	String endtime;
	String interval;
	
	public CalculateInterval(String starttime,String endtime)
	{
		this.starttime=starttime;
		this.endtime=endtime;
	}
	
	public String calculate()
	{
		try
		{			
			if( checktime(this.starttime) && checktime(this.endtime))
			{
				SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
				Date start=format.parse(this.starttime);
				Date end= format.parse(this.endtime);
				long diff=end.getTime()-start.getTime();
				System.out.println("Date start = "+start.toString());
				System.out.println("Date end = "+end.toString());
				if(diff >0)
				{
					System.out.println("Diff = "+diff);
					long diffsecond=diff/1000;
					System.out.println("Diffsecond = "+diffsecond);
					//1000 points 
					long intervalsecond=diffsecond/1000;
					System.out.println("Interval second = "+intervalsecond);
					if(intervalsecond<=60)
					{
						this.interval="20s";
						return this.interval;
					}
					else if (intervalsecond>60 && intervalsecond<=360)
					{
						this.interval="1m";
						return this.interval;	
					}
					else if (intervalsecond>360 && intervalsecond<=1800)
					{
						this.interval="10m";
						return this.interval;
					}
					//  30min<time<1h
					else if (intervalsecond >1800 && intervalsecond< 3600)
					{
						this.interval="1h";
						return this.interval;
					}
				//  1h<time<1d
					else if (intervalsecond >3600 && intervalsecond< 86400)
					{
						this.interval="3h";
						return this.interval;
					}
					//1d<time<10d
					else if (intervalsecond >86400 && intervalsecond< 864000)
					{
						this.interval="1d";
						return this.interval;
					}
					//10d<time
					else if (intervalsecond >864000)
					{
						this.interval="30d";
						return this.interval;
					}
					else
					{
						throw new RuntimeException("CalculateInterval- interval is n/a for unknown reason");
					}
				}
				else
				{
					throw new IllegalArgumentException("Start time or end time is not correct");
				}
			}
			else
			{
				throw new IllegalArgumentException("Start time or end time is not correct");
			}
		}catch (ParseException e) {
			e.printStackTrace();
			 throw new IllegalArgumentException("Start time or end time format is not correct");
		}
	}
	
	public boolean checktime(String time) 
	{
		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date date;
		try {
			date = format.parse(time);	
			String str=format.format(date);
			if(str.equals(time))
				return true;
			else
				return false;
		
		} catch (ParseException e) {
			e.printStackTrace();
		    throw new IllegalArgumentException("Start time or end time format is not correct");
		}
	}
}
