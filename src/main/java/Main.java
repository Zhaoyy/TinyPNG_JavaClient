import com.tinify.Tinify;
import swing.MainForm;

/**
 * Main
 *
 * @author Mislead
 *         DATE: 2015/10/9
 *         DESC:
 **/
public class Main {

  private static String TAG = "Main";

  public static void main(String args[]) {
    Tinify.setKey("test");
    new MainForm().invokeJFram();
  }
}
