package project;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;       
import java.io.*;

public class DataDriver {

	public static void main(String[] args) 
	{
		DummyPredictor p = new DummyPredictor();
		ArrayList<DataPoint> example = p.readData("ReadValue.txt");
		
		for(int a = 0; a < example.size(); a++)
		{
			if(example.get(a).getIsTest())
			{
				System.out.println(p.test(example.get(a)));
			}
		}
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame j = new JFrame();
		Container contentPane = j.getContentPane();
		j.setTitle("The number of accuracy and precision");
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane.setLayout(new FlowLayout());
		contentPane.add(new JLabel("Accuracy is: " + p.getAccuracy(example)));
		contentPane.add(new JLabel("Precision is: " + p.getPrecision(example)));
		j.pack();
		j.setVisible(true);
	}

}
