
import java.util.concurrent.TimeUnit;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;


public class GUIFrame extends JFrame
{
	private JProgressBar bar1, bar2, bar3;
	private JPanel container, panel1, panel2, panel3;
	private JButton threatButton;
	private myThreadObject x1;
	private myThreadObject x2;
	private myThreadObject x3;
	private JSlider slider1, slider2, slider3;


	public GUIFrame(int x, int y)
	{
		setTitle("Threading");
		setSize(x,y);
		setResizable(false);
		getContentPane().setBackground(Color.darkGray);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//setLayout(new GridLayout(1,0,20,20));
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
		container.setLayout(new GridLayout(4,1,25,25));
		container.setBackground(Color.darkGray);
		container.setPreferredSize(new Dimension(250, 420));

		bar1 = new JProgressBar();
		bar1.setMinimum(0);
		bar1.setMaximum(100);
		bar1.setPreferredSize( new Dimension (200,35));
		bar1.setStringPainted(true);
		bar1.setForeground(Color.pink);
		bar1.setString("0%");

		JLabel caption1 = new JLabel("Thread 1:");
		caption1.setForeground(Color.white);
		slider1 = new JSlider(JSlider.HORIZONTAL,50, 1000, 100);
		slider1.setMajorTickSpacing(150);
		slider1.setMinorTickSpacing(50);
		slider1.setPaintTicks(true);
		panel1 = new JPanel();
		panel1.setLayout(new GridLayout(3,1,4,4));
		panel1.add(caption1);
		panel1.add(bar1);
		panel1.add(slider1);
		panel1.setBackground(Color.darkGray);
		slider1.setBackground(Color.darkGray);

		container.add(panel1);

		bar2 = new JProgressBar();
		bar2.setMinimum(0);
		bar2.setMaximum(100);
		bar2.setPreferredSize( new Dimension (200,35));
		bar2.setStringPainted(true);
		bar2.setForeground(new Color(123,111,222));
		bar2.setString("0%");

		JLabel caption2 = new JLabel("Thread 2:");
		caption2.setForeground(Color.white);
		slider2 = new JSlider(JSlider.HORIZONTAL,50, 1000, 100);
		slider2.setMajorTickSpacing(150);
		slider2.setMinorTickSpacing(50);
		slider2.setPaintTicks(true);
		panel2 = new JPanel();
		panel2.setLayout(new GridLayout(3,1,4,4));
		panel2.add(caption2);
		panel2.add(bar2);
		panel2.add(slider2);
		panel2.setBackground(Color.darkGray);
		slider2.setBackground(Color.darkGray);

		container.add(panel2);

		bar3 = new JProgressBar();
		bar3.setMinimum(0);
		bar3.setMaximum(100);
		bar3.setPreferredSize( new Dimension (200,35));
		bar3.setStringPainted(true);
		bar3.setForeground(new Color(70, 198, 137));
		bar3.setString("0%");

		JLabel caption3 = new JLabel("Thread 3:");
		caption3.setForeground(Color.white);
		slider3 = new JSlider(JSlider.HORIZONTAL,50, 1000, 100);
		slider3.setMajorTickSpacing(150);
		slider3.setMinorTickSpacing(50);
		slider3.setPaintTicks(true);
		panel3 = new JPanel();
		panel3.setLayout(new GridLayout(3,1,4,4));
		panel3.add(caption3);
		panel3.add(bar3);
		panel3.add(slider3);
		panel3.setBackground(Color.darkGray);
		slider3.setBackground(Color.darkGray);

		container.add(panel3);


		add(container);
		add(threatButton);

		bar1.setValue(1);
		bar2.setValue(1);
		bar3.setValue(1);
		setVisible(true);
	}


	public void startThreading()
	{
		if(x1!=null)
			x1.stopped = true;
		if(x2!=null)
			x2.stopped = true;
		if(x3!=null)
			x3.stopped = true;

		x1 = new myThreadObject(bar1,slider1.getValue());
		x1.start();

		x2 = new myThreadObject(bar2, slider2.getValue());
		x2.start();

		x3 = new myThreadObject(bar3, slider3.getValue());
		x3.start();

	}


	class myThreadObject extends Thread
	{
		boolean stopped = false;
		int delay;
		JProgressBar thisBar;

		public myThreadObject(JProgressBar mybar, int sleepTimer)
		{
			thisBar = mybar;
			delay = sleepTimer;
		}

		public void run()
		{
			while(!stopped)
			{
					int count = 1;
					while(count<=100 && !stopped)
					{
						thisBar.setValue(count);
						thisBar.setString(count+"%");
						count++;
						try
						{
							Thread.sleep(delay);
						}
						catch(Exception e){}

						if(count>=100)
						{
							thisBar.setString("DONE!");
							stopped =true;
						}
						}
			}
		}

	}

}
