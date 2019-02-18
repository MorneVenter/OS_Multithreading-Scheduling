
import java.util.concurrent.TimeUnit;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.util.Random;

public class GUIFrame extends JFrame
{

	private JButton threatButton;
	private MyThread[] listOfThreads;
	private JPanel container;
	private int maxThreads = 8, totalThreads;

	public GUIFrame(int x, int y, int threads)
	{

		totalThreads = threads;
		if(totalThreads>8)
			System.exit(0);


		listOfThreads = new MyThread[maxThreads];	//initialize array, max 8 threads

		setTitle("Threading");
		setSize(x,y);
		setResizable(false);
		getContentPane().setBackground(Color.darkGray);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new FlowLayout());


		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);


		threatButton = new JButton();
		threatButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        startThreading();
    } });
		threatButton.setText("Start Threading");
		threatButton.setBackground(Color.gray);
		threatButton.setPreferredSize(new Dimension(250,40));


		container = new JPanel();
		//container.setLayout(new GridLayout(4,2,15,15));
		container.setBackground(Color.darkGray);
		container.setPreferredSize(new Dimension(650,600));
		Random rand = new Random();
		for(int i =0; i< totalThreads; i++)
		{
				int r = rand.nextInt(255);
				int g = rand.nextInt(255);
				int b = rand.nextInt(255);
				System.out.println(r +" "+g+" "+b);
				Color randomColor = new Color(r, g, b);
				listOfThreads[i] = new MyThread(i+1, randomColor);
				container.add(listOfThreads[i].getGUI());
		}


		add(container);
		add(threatButton);

		setVisible(true);
	}


	public void startThreading()
	{
	//	if(x1!=null)
	//		x1.stopped = true; //use method
	//	if(x2!=null)
	//		x2.stopped = true;
	//	if(x3!=null)
  //	x3.stopped = true;

		for(int i =0; i< totalThreads; i++)
		{
			listOfThreads[i].setDelay();
		}

		for(int i =0; i< totalThreads; i++)
		{
			listOfThreads[i].start();
		}


	}




}
