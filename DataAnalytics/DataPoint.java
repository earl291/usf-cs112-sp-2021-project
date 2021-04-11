package project;

public class DataPoint
{
	private double f1;
	private double f2;
	private String label;
	private boolean isTest;
	
	public DataPoint(double f1, double f2, String label, boolean test)
	{
		this.f1 = f1;
		this.f2 = f2;
		setLabel(label);
		this.isTest = test;
	}
	
	public DataPoint(double f1, double f2)
	{
		this.f1 = f1;
		this.f2 = f2;
		this.label = null;
		this.isTest = true;
	}
	
	public DataPoint()
	{
		this.f1 = 0.0;
		this.f2 = 0.0;
		this.label = null;
		this.isTest = false;
	}
	
	public Double getF1()
	{
		return this.f1;
	}
	
	public double getF2()
	{
		return this.f2;
	}
	
	public String getLabel()
	{
		return this.label;
	}
	
	public boolean getIsTest()
	{
		return this.isTest;
	}
	
	public void setF1(Double f)
	{
		if(f < 0)
		{
			return;
		}
		this.f1 = f;
	}
	
	public void setF2(double f)
	{
		if(f < 0)
		{
			return;
		}
		this.f2 = f;
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}
	
	public void setIsTest(boolean t)
	{
		this.isTest = t;
	}

}
