
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
	private JSlider defSlider;
	private JSlider priorSlider;

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

		priorSlider = new JSlider(JSlider.HORIZONTAL,2, 10, 5);
		priorSlider.setMajorTickSpacing(2);
		priorSlider.setMinorTickSpacing(1);
		priorSlider.setPaintTicks(true);

		defSlider = new JSlider(JSlider.HORIZONTAL,1, 8, 5);
		defSlider.setMajorTickSpacing(2);
		defSlider.setMinorTickSpacing(1);
		defSlider.setPaintTicks(true);

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
		if(totalThreads>=maxThreads)
			addThreadButton.setEnabled(false);

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
				listOfThreads.add(new MyThread(i+1, randomColor, 5, (i+1)));
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


		JLabel l1 = new JLabel("Priority:");
		JLabel l2 = new JLabel("Cycles:");
		JPanel controlos = new JPanel();
		controlos.setPreferredSize(new Dimension(500,75));
		controlos.setLayout(new GridLayout(2,2,2,2));

		add(selectionPanel);
		add(container);
		add(threatButton);
		add(addThreadButton);
		controlos.add(l2);
		controlos.add(defSlider);
		controlos.add(l1);
		controlos.add(priorSlider);
		add(controlos);
		setVisible(true);
	}


	public void startThreading()
	{
		if(startNext)
		{
			GUIFrame g1 = new GUIFrame(650,725,2);
			g1.setVisible(true);
			this.dispose();
			System.out.println("#############################################");
			s1.suspend();
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
			listOfThreads.add(new MyThread(totalThreads+1, randomColor, defSlider.getValue(), priorSlider.getValue()));
			container.add(listOfThreads.get(totalThreads).getGUI());
			container.revalidate();
			container.repaint();
			listOfThreads.get(totalThreads).setDelay();

			totalThreads++;

			System.out.println("Thread " +totalThreads+" added.");

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
			private List<MyThread> theseThreads; //list of all threads
			private int activeThread=0;	//index of current active thread
			private int totalThreads; //total number of threads
			private int x; 									//selected choice of scheduling algorithm
			private int SRTactiveThread = -1; //active thread of the shortest remaining time
			private int QUANTUM = 1000;  //quantum in milliseconds
			private int robinQueue=0;
			private int[] queues = {25,75,150,500}; //queue times for multiple queues
			private int[] queueMap; //keeps track of which threads queue

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
					{
							System.out.println("Thread "+(activeThread)+" stopped.");
							theseThreads.get(activeThread).start();
							System.out.println("Thread "+(activeThread+1)+" started.");
					}
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
							{
								theseThreads.get(SRTactiveThread).suspend();
								System.out.println("Thread "+(SRTactiveThread+1)+" stopped.");
							}
						SRTactiveThread = smallest;
						if(theseThreads.get(SRTactiveThread).isAlive())
						{
							theseThreads.get(SRTactiveThread).resume();
							System.out.println("Thread "+(SRTactiveThread+1)+" started.");
						}
						else
						{
							theseThreads.get(SRTactiveThread).start();
							System.out.println("Thread "+(SRTactiveThread+1)+" started.");
						}
					}

					try
					{
						Thread.sleep(40);
					}
					catch(Exception e){}

			}

			public void RoundRobin()
			{

				boolean initAllDone = false;
				int oldActive=0;

				if(robinQueue!=theseThreads.size())
				{
					initAllDone = true;

					for (int x= 0;x<robinQueue; x++)
					{
							if(!theseThreads.get(x).isAlive())
								initAllDone = false;
					}

					if(initAllDone)
					{
						oldActive = activeThread;
						activeThread = robinQueue;
						if(activeThread>=theseThreads.size())
							activeThread = 0;
					}

					System.out.println("New thread takes priority over threads that have started.");
					robinQueue++;
				}

				if(theseThreads.get(activeThread).hasStopped())
					{
						activeThread++;
						if(activeThread>=theseThreads.size())
							activeThread = 0;

						return;
					}

				if(theseThreads.get(activeThread).isAlive())
					{
						theseThreads.get(activeThread).resume();
						System.out.println("Thread "+(activeThread+1)+" started with quantum: "+QUANTUM+"ms");
					}
				else
				{
					theseThreads.get(activeThread).start();
					System.out.println("Thread "+(activeThread+1)+" started with quantum: "+QUANTUM+"ms");
				}

				boolean fullQ = true;
				for (int x=0; x<QUANTUM; x++ )
				{
					try
					{
						Thread.sleep(1);
					}
					catch(Exception e){}

					if(theseThreads.get(activeThread).hasStopped())
					{
						System.out.println("Thread "+(activeThread+1)+" used quantum: "+x+"ms");
						x=QUANTUM+1;
						fullQ = false;
					}
				}

				if(fullQ)
					System.out.println("Thread "+(activeThread+1)+" used quantum: "+QUANTUM+"ms");

				theseThreads.get(activeThread).suspend();
				System.out.println("Thread "+(activeThread+1)+" stopped.");


				if(initAllDone)
					activeThread = oldActive;
				else
					activeThread++;

				if(activeThread>=theseThreads.size() || activeThread < 0)
					activeThread = 0;


			}


			public void MultipleQueue()
			{

				for(int i=0; i<theseThreads.size(); i++)
				{
					if(queueMap[i]<0)
						queueMap[i]=0;
				}

				if(theseThreads.get(activeThread).hasStopped())
					{
						activeThread++;
						if(activeThread>=theseThreads.size())
							activeThread = 0;

						return;
					}

				if(theseThreads.get(activeThread).isAlive())
					{
						theseThreads.get(activeThread).resume();
						System.out.println("Thread "+(activeThread+1)+" started.");
					}
				else
				{
					theseThreads.get(activeThread).start();
					System.out.println("Thread "+(activeThread+1)+" started.");
				}


				int tempQuant = getQueue(activeThread);
				int qUsed=tempQuant;
				for (int x=0; x<tempQuant; x++ )
				{
					try
					{
						Thread.sleep(1);
					}
					catch(Exception e){}

					if(theseThreads.get(activeThread).hasStopped())
					{
						qUsed=x;
						x=tempQuant+1;
					}
				}

				System.out.println("Thread "+(activeThread+1)+" used "+qUsed+"ms of CPU time.");

				theseThreads.get(activeThread).suspend();
				System.out.println("Thread "+(activeThread+1)+" stopped.");
				activeThread++;
				System.out.println("------------------------------------");
				if(activeThread>=theseThreads.size())
					activeThread = 0;



			}

			private int getQueue(int x)
			{
				queueMap[x]+=1;
				if(queueMap[x]>=queues.length)
					queueMap[x]=0;

				System.out.println("Thread " +(x+1)+" allocated "+queues[queueMap[activeThread]] + "ms of CPU time.");
				return queues[queueMap[activeThread]];
			}


			public void priority()
			{
				int highestPrio=0;

				for(int i=0; i<theseThreads.size(); i++)
				{
						if((theseThreads.get(highestPrio).myPriority+theseThreads.get(highestPrio).getBarProgressValue()) < (theseThreads.get(i).myPriority+theseThreads.get(i).getBarProgressValue()) && !theseThreads.get(i).hasStopped())
							highestPrio = i;
				}

				if(theseThreads.get(highestPrio).hasStopped())
					return;


				if(activeThread != highestPrio)
				{
					theseThreads.get(activeThread).suspend();
					System.out.println("Thread "+(activeThread+1)+" stopped.");
					activeThread = highestPrio;
					if(theseThreads.get(activeThread).isAlive())
						theseThreads.get(activeThread).resume();
					else
						theseThreads.get(activeThread).start();
					System.out.println("Thread "+(activeThread+1)+" started with priority " +theseThreads.get(activeThread).myPriority);
				}

			}


			public void run()
			{
				if(x==5)
				{
					queueMap = new int[8];
					for(int i=0; i<theseThreads.size(); i++)
					{
						queueMap[i]=0;
					}
					for(int i = theseThreads.size(); i<queueMap.length; i++)
					{
						queueMap[i] = -1;
					}

				}

				robinQueue = theseThreads.size();

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
					else if(x==4)
						priority();
					else if(x==5)
						MultipleQueue();
				}
			}


}


}
