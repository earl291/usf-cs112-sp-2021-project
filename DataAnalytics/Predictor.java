package project;

import java.util.ArrayList;

public abstract class Predictor 
{
	abstract ArrayList<DataPoint> readData(String filename);
	
	abstract String test(DataPoint data);
	
	abstract double getAccuracy(ArrayList<DataPoint> data);
	
	abstract double getPrecision(ArrayList<DataPoint> data);

}
