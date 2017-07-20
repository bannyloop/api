package com.orange.pns.monit.service;

import java.util.ArrayList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenerateQueryCommand {
	String starttime;
	String endtime;
	String metric;
	ArrayList<String> fields; 
	
	public GenerateQueryCommand(String starttime,String endtime,String metric,String fieldTotal)
	{
		if(starttime==null||starttime.isEmpty())
			throw new IllegalArgumentException("starttime is missing");
		else if(endtime==null||endtime.isEmpty())
			throw new IllegalArgumentException("endtime is missing");
		else if(metric==null||metric.isEmpty())
			throw new IllegalArgumentException("metric parameter is missing");
		else if(fieldTotal==null||fieldTotal.isEmpty())
			throw new IllegalArgumentException("field parameter is missing");
			
		this.starttime=starttime;
		this.endtime=endtime;
		this.metric=metric;
		this.fields=splitFields(fieldTotal);

	//	System.out.println("GenerateQueryCommand fieldTotal="+fieldTotal);
	//	System.out.println("GenerateQueryCommand fields="+this.fields.toString());
		
	}
	
	public String generate()
	{
		try{
		CalculateInterval ci=new CalculateInterval(this.starttime,this.endtime);
		String interval=ci.calculate();
		System.out.println("GenerateQueryCommand interval="+interval);
		if(!interval.equals("n/a"))
		{
			String start=changeDateFormat(starttime);
			String end=changeDateFormat(endtime);
			String queryCommand="SELECT ";
			for(int i=0;i<this.fields.size();i++)
			{
				queryCommand=queryCommand+"mean("+"\""+fields.get(i)+"\"),";
			}
			if(this.fields.size()!=0)
			{
				queryCommand=queryCommand.substring(0, queryCommand.length()-1);
			}
			queryCommand=queryCommand+" FROM "+"\""+this.metric+"\""+" WHERE time>= "+"'"+start+"' AND time < '"+end+"' GROUP BY time("+interval+")";
			//log.info("GenerateQueryCommand-QueryCommand Generated is : "+queryCommand);
			return queryCommand;
		}
		else
			return null;
		}catch(IndexOutOfBoundsException e)
		{
			throw new IllegalArgumentException("fields param is not correct");
		}
		
	}
	
	public ArrayList<String> splitFields(String fieldTotal)
	{

			 
			try{
				ArrayList<String> result=new ArrayList<String>();  
				String[] fieldsPiece=fieldTotal.split(" ");			
				for(int i=0;i<fieldsPiece.length;i++)
				{
					if(!fieldsPiece[i].isEmpty())
					{
						result.add(fieldsPiece[i]);
					}
				}
				return result;
			}catch(NullPointerException e)
			{
				throw new IllegalArgumentException("Parameter field is not correct");
			}
	}

	
	public String changeDateFormat(String time) 
	{
		String timeChange=time.substring(0, 10)+"T"+time.substring(11, 13)+":"+time.substring(14, 16)+":"+time.substring(17, 19)+"Z";
		return timeChange;
	}
	
	public ArrayList<String> getFields()
	{
		return this.fields;
	}
}
