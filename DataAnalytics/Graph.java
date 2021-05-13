package project;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;

public class Graph extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
    private int labelPadding = 40;
    private Color lineColor = new Color(255, 255, 254);

    // TODO: Add point colors for each type of data point
    private Color pointColor = new Color(255, 0, 255);
    private Color red = new Color(255, 0, 0);
    private Color blue = new Color(0, 0, 255);
    private Color cyan = new Color(0, 255, 255);
    private Color yellow = new Color(255, 255, 0);

    private Color gridColor = new Color(200, 200, 200, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);

    // TODO: Change point width as needed
    private static int pointWidth = 10;

    // Number of grids and the padding width
    private int numXGridLines = 6;
    private int numYGridLines = 6;
    private int padding = 40;

    private List<DataPoint> data;

    // TODO: Add a private KNNPredictor variable
    private KNNPredictor knn;
    
    static final int FPS_MIN = 2;
    static final int FPS_MAX = 25;
    static final int FPS_INIT = 5;
    int knum = 5;
    JLabel arr;
    JLabel pre;
    JSlider sli;
    
    	
	/**
	 * Constructor method
	 */
    public Graph(int K, String fileName) 
    {
    	 this.knn = new KNNPredictor(K);
         ArrayList<DataPoint> k = knn.readData(fileName);
         List<DataPoint> test = new ArrayList<>();
         List<DataPoint> train = new ArrayList<>();
         
         // Generates random DataPoints
         for (int i = 0; i < k.size(); i++) 
         {
             if(k.get(i).getIsTest())
             {
           	  test.add(k.get(i));
             }
             else
             {
           	  train.add(k.get(i));
             }
         }
        this.data = test;
        this.arr = new JLabel("Accuracy is: " + this.knn.getAccuracy(this.knn.getD()) + "%");
        this.pre = new JLabel("Precision is: " + this.knn.getPrecision(this.knn.getD()) + "%");
        this.sli = new JSlider(JSlider.HORIZONTAL, FPS_MIN, FPS_MAX, FPS_INIT);
        
        // TODO: Remove the above logic where random data is generated
        // TODO: instantiate the KNNPredictor variable
        // TODO: Run readData using input filename to split the data to test and training
        // TODO: Set this.data as the output of readData
    }
    
    public KNNPredictor getKNN()
    {
    	return this.knn;
    }
    
    public JLabel getArr()
    {
    	return this.arr;
    }
    
    public JLabel getPre()
    {
    	return this.pre;
    }
    
    public JSlider getSli()
    {
    	return this.sli;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double minF1 = getMinF1Data();
        double maxF1 = getMaxF1Data();
        double minF2 = getMinF2Data();
        double maxF2 = getMaxF2Data();

        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - 
        		labelPadding, getHeight() - 2 * padding - labelPadding);
        g2.setColor(Color.BLUE);

        double yGridRatio = (maxF2 - minF2) / numYGridLines;
        for (int i = 0; i < numYGridLines + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight() - ((i * (getHeight() - padding * 2 -
            		labelPadding)) / numYGridLines + padding + labelPadding);
            int y1 = y0;
            if (data.size() > 0) {
                g2.setColor(gridColor);
                g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                g2.setColor(Color.BLACK);
                String yLabel = String.format("%.2f", (minF2 + (i * yGridRatio)));
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 6, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        double xGridRatio = (maxF1 - minF1) / numXGridLines;
        for (int i = 0; i < numXGridLines + 1; i++) {
            int y0 = getHeight() - padding - labelPadding;
            int y1 = y0 - pointWidth;
            int x0 = i * (getWidth() - padding * 2 - labelPadding) / (numXGridLines) + padding + labelPadding;
            int x1 = x0;
            if (data.size() > 0) {
                g2.setColor(gridColor);
                g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                g2.setColor(Color.BLACK);
                String xLabel = String.format("%.2f", (minF1 + (i * xGridRatio)));
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(xLabel);
                g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        // Draw the main axis
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() -
        		padding, getHeight() - padding - labelPadding);

        // Draw the points
        paintPoints(g2, minF1, maxF1, minF2, maxF2);
    }

    private void paintPoints(Graphics2D g2, double minF1, double maxF1, double minF2, double maxF2) {
        Stroke oldStroke = g2.getStroke();
        g2.setColor(lineColor);
        g2.setStroke(GRAPH_STROKE);
        double xScale = ((double) getWidth() - (3 * padding) - labelPadding) /(maxF1 - minF1);
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (maxF2 - minF2);
        g2.setStroke(oldStroke);
        for (int i = 0; i < data.size(); i++) {
            int x1 = (int) ((data.get(i).getF1() - minF1) * xScale + padding + labelPadding);
            int y1 = (int) ((maxF2 - data.get(i).getF2()) * yScale + padding);
            int x = x1 - pointWidth / 2;
            int y = y1 - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;

            // TODO: Depending on the type of data and how it is tested, change color here.
            // You need to test your data here using the model to obtain the test value 
            // and compare against the true label.
            // Note that depending on how you implemented "test" method, you may need to 
            // modify KNNPredictor to store the output from readData.
            // You can also optimize further to compute accuracy and precision in a single
            // iteration.
            if(data.get(i).getLabel().equalsIgnoreCase("1"))
			{
				if(this.knn.test(data.get(i)).equalsIgnoreCase("1"))
				{
					g2.setColor(blue);
				}
				else
				{
					g2.setColor(yellow);
				}
			}
			else
			{
				if(this.knn.test(data.get(i)).equalsIgnoreCase("1"))
				{
					g2.setColor(cyan);
				}
				else
				{
					g2.setColor(red);
				}
			}
            g2.fillOval(x, y, ovalW, ovalH);
        }

    }

    /*
     * @Return the min values
     */
    private double getMinF1Data() {
        double minData = Double.MAX_VALUE;
        for (DataPoint pt : this.data) {
            minData = Math.min(minData, pt.getF1());
        }
        return minData;
    }

    private double getMinF2Data() {
        double minData = Double.MAX_VALUE;
        for (DataPoint pt : this.data) {
            minData = Math.min(minData, pt.getF2());
        }
        return minData;
    }


    /*
     * @Return the max values;
     */
    private double getMaxF1Data() {
        double maxData = Double.MIN_VALUE;
        for (DataPoint pt : this.data) {
            maxData = Math.max(maxData, pt.getF1());
        }
        return maxData;
    }

    private double getMaxF2Data() {
        double maxData = Double.MIN_VALUE;
        for (DataPoint pt : this.data) {
            maxData = Math.max(maxData, pt.getF2());
        }
        return maxData;
    }

    /* Mutator */
    public void setData(List<DataPoint> data) {
        this.data = data;
        invalidate();
        this.repaint();
    }

    /* Accessor */
    public List<DataPoint> getData() {
        return data;
    }

    /*  Run createAndShowGui in the main method, where we create the frame too and pack it in the panel*/
    private static void createAndShowGui(int K, String fileName) {


	    /* Main panel */
        Graph mainPanel = new Graph(K, fileName);
        JPanel l = new JPanel();
        JPanel s = new JPanel();
        JLabel cho = new JLabel("Choose the majority value", JLabel.CENTER);
        cho.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton run = new JButton("Run Test");
        run.addActionListener(mainPanel);
        
        
        // Feel free to change the size of the panel
        mainPanel.setPreferredSize(new Dimension(700, 600));
        mainPanel.getSli().setMajorTickSpacing(5);
        mainPanel.getSli().setMinorTickSpacing(1);
        mainPanel.getSli().setPaintTicks(true);
        mainPanel.getSli().setSnapToTicks(true);
        mainPanel.getSli().setPaintLabels(true);

        /* creating the frame */
        JFrame frame = new JFrame("CS 112 Lab Part 4");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        frame.add(l, BorderLayout.NORTH);
        frame.add(s, BorderLayout.SOUTH);
        l.add(mainPanel.getArr());
        l.add(mainPanel.getPre());
        s.add(cho);
        s.add(mainPanel.getSli());
        s.add(run);
        s.setLayout(new BoxLayout(s, BoxLayout.PAGE_AXIS));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) 
    {
    	JSlider j = this.sli;
    	knum = j.getValue() * 2 + 1;
    	this.knn.setK(knum);
    	arr.setText("Accuracy is: " + this.knn.getAccuracy(this.knn.getD()) + "%");
    	pre.setText("Precision is: " + this.knn.getPrecision(this.knn.getD()) + "%");
    }
      
    /* The main method runs createAndShowGui*/
    public static void main(String[] args) {
        int K = 99; // A value of K selected    
        String fileName = "titanic.csv"; // TODO: Change this to titanic.csv
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGui(K, fileName);
            }
        });
    }

}
