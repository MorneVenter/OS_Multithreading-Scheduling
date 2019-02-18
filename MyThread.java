
import java.util.concurrent.TimeUnit;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;


class MyThread extends Thread
{
  private boolean stopped = false;
  private int delay;
  private JPanel panel1;
  private JProgressBar bar1;
	private JSlider slider1;
  private int myNumber;
  private Color myBarColor;
  private JLabel caption1;

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
		bar1.setMaximum(100);
		bar1.setPreferredSize( new Dimension (200,35));
		bar1.setStringPainted(true);
		bar1.setForeground(myBarColor);
		bar1.setString("0%");

		caption1 = new JLabel("Thread "+myNumber+":");
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
    panel1.setBorder(BorderFactory.createLineBorder(Color.black));
    panel1.setPreferredSize(new Dimension(280, 110));

		slider1.setBackground(Color.darkGray);

    bar1.setValue(1);
  }

  public JPanel getGUI()
  {
    return panel1;
  }

  public void stopThread()
  {
    stopped = true;
  }

  public void run()
  {
    while(!stopped)
    {
        int count = 1;
        while(count<=100 && !stopped)
        {


          bar1.setValue(count);
          bar1.setString(count+"%");
          count++;
          caption1.setForeground(Color.red);
          try
          {
            Thread.sleep(delay);
          }
          catch(Exception e){}

          if(count>=100)
          {
            bar1.setString("DONE!");
            stopped =true;
          }

          caption1.setForeground(Color.white);
        }
    }
  }


}
