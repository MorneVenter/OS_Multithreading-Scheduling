
import java.util.concurrent.TimeUnit;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.util.Random;
import java.util.List;


public class GUIFrame extends JFrame
{
	private MyScheduler s1;
	private JButton threatButton;
	private JButton addThreadButton;
	private List<MyThread> listOfThreads;
	private JPanel container, selectionPanel;
	private int maxThreads = 8, totalThreads;
	private boolean startNext;
	private JComboBox selectScheduler;

	public GUIFrame(int x, int y, int threads)
	{

		totalThreads = threads;

		startNext = false;

		listOfThreads = new ArrayList<MyThread>();	//initialize list, max 8 threads

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
		threatButton.setText("Start Threading / Reset");
		threatButton.setBackground(Color.white);
		threatButton.setPreferredSize(new Dimension(250,40));


		addThreadButton = new JButton();
		addThreadButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        addThread();
    } });
		addThreadButton.setText("Add Thread");
		addThreadButton.setBackground(Color.white);
		addThreadButton.setPreferredSize(new Dimension(250,40));

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
				listOfThreads.add(new MyThread(i+1, randomColor));
				container.add(listOfThreads.get(i).getGUI());
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
		add(addThreadButton);

		setVisible(true);
	}


	public void startThreading()
	{
		if(startNext)
		{
			GUIFrame g1 = new GUIFrame(650,650,2);
			g1.setVisible(true);
			this.dispose();
			return;
		}

		for(int i =0; i< totalThreads; i++)
		{
			listOfThreads.get(i).setDelay();
		}

		startNext = true;
		s1 = new MyScheduler(selectScheduler.getSelectedIndex(), listOfThreads, totalThreads);
		s1.start();

	}


	private void addThread()
	{
			Random rand = new Random();
			int r = rand.nextInt(255);
			int g = rand.nextInt(255);
			int b = rand.nextInt(255);
			Color randomColor = new Color(r, g, b);
			listOfThreads.add(new MyThread(totalThreads+1, randomColor));
			container.add(listOfThreads.get(totalThreads).getGUI());
			container.revalidate();
			container.repaint();
			listOfThreads.get(totalThreads).setDelay();

			totalThreads++;

			if(totalThreads>=maxThreads)
			{
				addThreadButton.setEnabled(false);
			}

			if(s1!=null)
			{
				s1.setTotalThreads(totalThreads);
			}



	}




public class MyScheduler extends Thread
{
			private List<MyThread> theseThreads;
			private int activeThread=0;
			private int totalThreads;
			private int x; 										//selected choice
			private int SRTactiveThread = -1;
			private int QUANTUM = 50;


			public MyScheduler(int y, List<MyThread> tt, int numThreads)
			{
				x = y;
				theseThreads = tt;
				totalThreads = numThreads;

			}

			public void setTotalThreads(int x)
			{
				totalThreads = x;
			}

			public void firstComefirstServe()
			{
				if(!theseThreads.get(activeThread).hasStopped())
				{
					if(!theseThreads.get(activeThread).isAlive())
							theseThreads.get(activeThread).start();
				}
				else
				{
					if(activeThread<totalThreads-1)
						activeThread++;

				}
			}



			public void shortestRemainingTime()
			{
					//get shortest time remaining threads
					boolean allThreadsDone = true;
					for(int i =0; i< theseThreads.size(); i++)
					{
						if(!theseThreads.get(i).hasStopped())
							allThreadsDone = false;
					}

					if(allThreadsDone)
					{
						return;
					}

					int[] shortestThread = new int[theseThreads.size()];
					for(int i =0; i< theseThreads.size(); i++)
					{
					  if(!theseThreads.get(i).hasStopped())
					  {
					    shortestThread[i] = ((2500 - theseThreads.get(i).barProgress)/theseThreads.get(i).delay);
					  }
					  else
					  {
					    shortestThread[i] = -1;
					  }
					}

					int smallest=0;

					for(int i =0; i< theseThreads.size(); i++)
					{
					  if(shortestThread[i]!=-1)
					    smallest = i;
					}

					if(theseThreads.get(smallest).hasStopped())
					  return;


					for(int i =0; i< theseThreads.size(); i++)
					{
					  if(shortestThread[i]!=-1)
					  {
					    if(shortestThread[smallest]>shortestThread[i] && !theseThreads.get(i).hasStopped())
					      smallest=i;
					  }
					}


					if(SRTactiveThread != smallest)
					{
						if(SRTactiveThread != -1)
							theseThreads.get(SRTactiveThread).suspend();
						SRTactiveThread = smallest;
						if(theseThreads.get(SRTactiveThread).isAlive())
							theseThreads.get(SRTactiveThread).resume();
						else
							theseThreads.get(SRTactiveThread).start();
					}

					try
					{
						Thread.sleep(40);
					}
					catch(Exception e){}

			}

			public void RoundRobin()
			{

				if(theseThreads.get(activeThread).hasStopped())
					{
						activeThread++;
						if(activeThread>=theseThreads.size())
							activeThread = 0;

						return;
					}

				if(theseThreads.get(activeThread).isAlive())
					theseThreads.get(activeThread).resume();
				else
					theseThreads.get(activeThread).start();

				try
				{
					Thread.sleep(QUANTUM);
				}
				catch(Exception e){}

				theseThreads.get(activeThread).suspend();
				activeThread++;

				if(activeThread>=theseThreads.size())
					activeThread = 0;

			}


			public void run()
			{
				while(true)
				{
					try
	        {
	          Thread.sleep(15);
	        }
	        catch(Exception e){}

					if(x==0)
						firstComefirstServe();
					else if(x==1)
						shortestRemainingTime();
					else if(x==2)
						shortestRemainingTime();
					else if(x==3)
						RoundRobin();
				}
			}


}


}
