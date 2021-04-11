package project;

import java.util.*;
import javax.swing.*;
import java.awt.*;

public class DriverClass {

	public static void main(String[] args) 
	{
		System.out.println("Please give me a odd integer: ");
		Scanner V1 = new Scanner(System.in);
		int y = V1.nextInt();
		V1.close();
		
		KNNPredictor knn = new KNNPredictor(y);
		if(knn.getK() == 0)
		{
			System.exit(0);
		}
		ArrayList<DataPoint> titanic = knn.readData("titanic.csv");
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame j = new JFrame();
		Container contentPane = j.getContentPane();
		j.setTitle("The number of accuracy and precision");
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane.setLayout(new FlowLayout());
		contentPane.add(new JLabel("Accuracy is: " + knn.getAccuracy(titanic) + "%"));
		contentPane.add(new JLabel("Precision is: " + knn.getPrecision(titanic) + "%"));
		j.pack();
		j.setVisible(true);

	}

}
