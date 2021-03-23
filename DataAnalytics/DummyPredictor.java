package project;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Math;

public class DummyPredictor extends Predictor
{
	private double greenAvg;
	private double blueAvg;
	
	public ArrayList<DataPoint> readData(String filename)
	{
		ArrayList<DataPoint> d = new ArrayList<DataPoint>();
		try
		{
			Scanner v1 = new Scanner(new File(filename));
			v1.useLocale(Locale.US);
			while(v1.hasNext())
			{
				d.add(new DataPoint(v1.nextDouble(), v1.nextDouble(), v1.next(), v1.nextBoolean()));
			}
		}
		catch (FileNotFoundException ex)
		{
			System.out.println("File Not Found");
		}
		double f1 = 0.0;
		double f2 = 0.0;
		for(int a = 0; a < d.size(); a++)
		{
			f1 = f1 + d.get(a).getF1();
			f2 = f2 + d.get(a).getF2();
		}
		this.greenAvg = f1 / d.size();
		this.blueAvg = f2 / d.size();
		return d;
	}
	
	public double getAccuracy(ArrayList<DataPoint> data)
	{
		double acc = 0.0;
		double co = 0.0;
		for(int a = 0; a < data.size(); a++)
		{
			if(data.get(a).getF1() > 6.0 && data.get(a).getF2() > 8.0)
			{
				if(this.test(data.get(a)).equals("Blue"))
				{
					co = co + 1.0;
				}
			}
			else
			{
				if(this.test(data.get(a)).equals("Green"))
				{
					co = co + 1.0;
				}
			}
		}
		acc = co / data.size();
		return acc;
	}
	
	public double getPrecision(ArrayList<DataPoint> data)
	{
		double div = 0.0;
		for(int a = 0; a < data.size(); a++)
		{
			div = div + Math.abs(data.get(a).getF1() - this.greenAvg) + Math.abs(data.get(a).getF2() - this.blueAvg);
		}
		div = div / data.size();
		return div;
	}
	
	public String test(DataPoint data)
	{
		if(Math.abs(data.getF1() - this.greenAvg) > Math.abs(data.getF2() - this.blueAvg))
		{
			return "Blue";
		}
		else
		{
			return "Green";
		}
	}

}
