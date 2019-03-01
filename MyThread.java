
import java.util.concurrent.TimeUnit;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class MyThread extends Thread
{
  private boolean stopped = false;
  public int delay;
  private JPanel panel1;
  private JProgressBar bar1;
	private JSlider slider1;
  public int myNumber;
  private Color myBarColor;
  private JLabel caption1;
  public int barProgress=0;



  public MyThread(int myNum, Color myCol)
  {
    myNumber = myNum;
    myBarColor = myCol;
    generateGUI();

  }


  public void setDelay()
  {
    delay = slider1.getValue();
  }

  public void generateGUI()
  {
    bar1 = new JProgressBar();
		bar1.setMinimum(0);
		bar1.setMaximum(2500);
		bar1.setPreferredSize( new Dimension (200,35));
		bar1.setStringPainted(true);
		bar1.setForeground(myBarColor);
		bar1.setString("0%");

    bar1.addChangeListener(new ChangeListener()
    {
      public void stateChanged(ChangeEvent evt)
      {
        setDelay();
      }
    });


		caption1 = new JLabel("Thread "+myNumber+":");
		caption1.setForeground(Color.white);
		slider1 = new JSlider(JSlider.HORIZONTAL,1, 10, 5);
		slider1.setMajorTickSpacing(2);
		slider1.setMinorTickSpacing(1);
		slider1.setPaintTicks(true);

		panel1 = new JPanel();
		panel1.setLayout(new GridLayout(3,1,4,4));
		panel1.add(caption1);
		panel1.add(bar1);
		panel1.add(slider1);
		panel1.setBackground(Color.darkGray);
    panel1.setBorder(BorderFactory.createLineBorder(Color.black));
    panel1.setPreferredSize(new Dimension(280, 110));

		slider1.setBackground(Color.darkGray);

    bar1.setValue(1);
  }

  public JPanel getGUI()
  {
    return panel1;
  }


  public boolean hasStopped()
  {
    return stopped;
  }





  public void run()
  {
    while(!stopped)
    {

        bar1.setValue(bar1.getValue()+(delay));
        barProgress = bar1.getValue();
        bar1.setString((bar1.getValue()/25)+"%");
        caption1.setForeground(Color.red);

        try
        {
          Thread.sleep(5);
        }
        catch(Exception e){}

        if(bar1.getValue()>=2500)
        {
          bar1.setString("DONE!");
          stopped =true;
        }

        caption1.setForeground(Color.white);

    }
  }


}
