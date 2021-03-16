package project;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;       
import java.io.*;

public class DataDriver {

	public static void main(String[] args) 
	{
		ArrayList<DataPoint> training = new ArrayList<DataPoint>();
		ArrayList<DataPoint> test = new ArrayList<DataPoint>();
		
		training.add(new DataPoint(3.0, 5.0, "Green", false));
		training.add(new DataPoint(4.0, 3.0, "Green", false));
		training.add(new DataPoint(3.0, 2.0, "Green", false));
		training.add(new DataPoint(11.0, 9.0, "Blue", false));
		training.add(new DataPoint(10.0, 10.0, "Blue", false));
		training.add(new DataPoint(13.0, 11.0, "Blue", false));
		
		test.add(new DataPoint(5.0, 3.0));
		test.add(new DataPoint(13.0, 10.0));
		test.add(new DataPoint(4.0, 3.0));
		test.add(new DataPoint(11.0, 12.0));
		
		DummyPredictor p = new DummyPredictor();
		p.guess(training);
		System.out.println(p.test(test.get(0)));
		System.out.println(p.test(test.get(1)));
		System.out.println(p.test(test.get(2)));
		System.out.println(p.test(test.get(3)));
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame j = new JFrame();
		Container contentPane = j.getContentPane();
		j.setTitle("The number of accuracy and precision");
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane.setLayout(new FlowLayout());
		contentPane.add(new JLabel("Accuracy is: " + p.getAccuracy(test)));
		contentPane.add(new JLabel("Precision is: " + p.getPrecision(test)));
		j.pack();
		j.setVisible(true);
	}

}
