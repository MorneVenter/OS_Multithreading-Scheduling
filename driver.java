
import javax.swing.JOptionPane;


public class driver
{

  public static void main(String[] args)
  {
    int threads=2;
    boolean validString = false;

    while(!validString)
    {
        String input = JOptionPane.showInputDialog("Number of threads (MAX:8): ");
        try
        {
          threads = Integer.parseInt(input);
          if(threads<2 || threads>8)
            JOptionPane.showMessageDialog(null, "Please enter valid integer number.");
          else
            validString = true;
        }
        catch(Exception e)
        {
          JOptionPane.showMessageDialog(null, "Please enter valid integer number.");
        }
    }


    GUIFrame thisFrame = new GUIFrame(650, 725, threads);

  }

}
