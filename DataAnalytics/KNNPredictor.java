package project;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Math;
import java.text.DecimalFormat;

public class KNNPredictor extends Predictor
{
	private int k;
	private int sPass = 0;
	private int nPass = 0;
	private ArrayList<DataPoint> d = new ArrayList<DataPoint>();
	
	public KNNPredictor(int a)
	{
		if(a % 2 == 1)
		{
			this.k = a;
		}
		else
		{
			System.out.println("The number shouble be odd number!");
		}
	}
	
	public void setK(int n)
	{
		this.k = n;	
	}
	
	public int getK()
	{
		return this.k;
	}
	
	public ArrayList<DataPoint> getD()
	{
		return this.d;
	}
	
	private List<String> getRecordFromLine(String line)
	{
		List<String> values = new ArrayList<String>();
		try (Scanner rowScanner = new Scanner(line))
		{
			rowScanner.useDelimiter(",");
			while (rowScanner.hasNext())
			{
				values.add(rowScanner.next());
			}
		}
		return values;
	}
	
	public ArrayList<DataPoint> readData(String filename)
	{
		try
		{
			Scanner v1 = new Scanner(new File(filename));
			v1.useLocale(Locale.US);
			Random rand = new Random();
			v1.nextLine();
			while(v1.hasNextLine())
			{
				List<String> records = getRecordFromLine(v1.nextLine());
				double randNum = rand.nextDouble();
				if(!(records.get(5).equalsIgnoreCase("") || records.size() < 7))
				{
					if(randNum < 0.9)
					{
						if(records.get(1).equals("1"))
						{
							sPass = sPass + 1;
						}
						else
						{
							nPass = nPass + 1;
						}
						d.add(new DataPoint(Double.parseDouble(records.get(5)), Double.parseDouble(records.get(6)), records.get(1), false));
					}
					else
					{
						d.add(new DataPoint(Double.parseDouble(records.get(5)), Double.parseDouble(records.get(6)), records.get(1), true));
					}
				}
			}
			v1.close();
		}
		catch (FileNotFoundException ex)
		{
			System.out.println("File Not Found");
		}
		System.out.println("The number of people that survived in train data: " + sPass);
		System.out.println("The number of people that died in train data: " + nPass);
		return d;
	}
	
	private double getDistance(DataPoint p1, DataPoint p2)
	{
		double d;
		d = Math.sqrt((Math.pow(p2.getF1() - p1.getF1(), 2)) + (Math.pow(p2.getF2() - p1.getF2(), 2)));
		return d;
	}
	
	public String test(DataPoint data)
	{
		Double[][] arr = new Double[sPass + nPass][2];
		double dis;
		int r = 0;
		int l = 0;
		if(data.getIsTest())
		{
			for(int a = 0; a < d.size(); a ++)
			{
				if(!d.get(a).getIsTest())
				{
					dis = getDistance(data, d.get(a));
					arr[r][0] = dis;
					arr[r][1] = Double.parseDouble(d.get(a).getLabel());
					r++;
				}
			}
			java.util.Arrays.sort(arr, new java.util.Comparator<Double[]>() {
				public int compare(Double[] c, Double[] d) {
					return c[0].compareTo(d[0]);
				}
			});
			for(int t = 0; t < this.k; t++)
			{
				if(arr[t][1] == 1.0)
				{
					l = l + 1;
				}
				else
				{
					l = l - 1;
				}
			}
		}
		if(l > 0)
		{
			return "1";
		}
		else
		{
			return "0";
		}
	}
	
	public double getAccuracy(ArrayList<DataPoint> data)
	{
		double truePositive = 0;
		double falsePositive = 0;
		double falseNegative = 0;
		double trueNegative = 0;
		double acc;
		DecimalFormat df = new DecimalFormat("###.##");
		for(int a = 0; a < d.size(); a ++)
		{
			if(d.get(a).getIsTest())
			{
				if(d.get(a).getLabel().equalsIgnoreCase("1"))
				{
					if(this.test(d.get(a)).equalsIgnoreCase("1"))
					{
						truePositive = truePositive + 1;
					}
					else
					{
						falseNegative = falseNegative + 1;
					}
				}
				else
				{
					if(this.test(d.get(a)).equalsIgnoreCase("1"))
					{
						falsePositive = falsePositive + 1;
					}
					else
					{
						trueNegative = trueNegative + 1;
					}
				}
			}
		}
		acc = ((truePositive + trueNegative) / (truePositive + falseNegative + falsePositive + trueNegative)) * 100;
		acc = Double.parseDouble(df.format(acc));
		return acc;
	}
	
	public double getPrecision(ArrayList<DataPoint> data)
	{
		double truePositive = 0;
		double falsePositive = 0;
		double falseNegative = 0;
		double trueNegative = 0;
		double pre;
		DecimalFormat df = new DecimalFormat("###.##");
		for(int a = 0; a < d.size(); a ++)
		{
			if(d.get(a).getIsTest())
			{
				if(d.get(a).getLabel().equalsIgnoreCase("1"))
				{
					if(this.test(d.get(a)).equalsIgnoreCase("1"))
					{
						truePositive = truePositive + 1;
					}
					else
					{
						falseNegative = falseNegative + 1;
					}
				}
				else
				{
					if(this.test(d.get(a)).equalsIgnoreCase("1"))
					{
						falsePositive = falsePositive + 1;
					}
					else
					{
						trueNegative = trueNegative + 1;
					}
				}
			}
		}
		pre = ((truePositive) / (truePositive + falseNegative)) * 100;
		pre = Double.parseDouble(df.format(pre));
		return pre;
	}

}
