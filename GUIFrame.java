
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
	private JPanel container, selectionPanel;
	private int maxThreads = 8, totalThreads;
	private boolean startNext;
	private JComboBox selectScheduler;

	public GUIFrame(int x, int y, int threads)
	{

		totalThreads = threads;

		startNext = false;

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
		threatButton.setBackground(Color.white);
		threatButton.setPreferredSize(new Dimension(250,40));


		container = new JPanel();
		container.setBackground(Color.darkGray);
		container.setPreferredSize(new Dimension(650,500));
		Random rand = new Random();
		for(int i =0; i< totalThreads; i++)
		{
				int r = rand.nextInt(255);
				int g = rand.nextInt(255);
				int b = rand.nextInt(255);
				Color randomColor = new Color(r, g, b);
				listOfThreads[i] = new MyThread(i+1, randomColor);
				container.add(listOfThreads[i].getGUI());
		}


		String[] schedulerString = {"First-come-first-serve","Shortest Job First","Shortest Time Remaining","Round Robin","Priority","Multiple Queues"};
		selectScheduler = new JComboBox(schedulerString);
		selectScheduler.setSelectedIndex(0);
		selectionPanel = new JPanel();
		selectionPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		selectionPanel.setBackground(Color.darkGray);
		selectionPanel.setPreferredSize(new Dimension(650,40));
		selectionPanel.add(selectScheduler);


		add(selectionPanel);
		add(container);
		add(threatButton);

		setVisible(true);
	}


	public void startThreading()
	{
		if(startNext)
		{
			GUIFrame g1 = new GUIFrame(650,650,totalThreads);
			g1.setVisible(true);
			this.dispose();
			return;
		}

		for(int i =0; i< totalThreads; i++)
		{
			listOfThreads[i].setDelay();
		}

		for(int i =0; i< totalThreads; i++)
		{
			listOfThreads[i].start();
		}

		startNext = true;


	}




}
