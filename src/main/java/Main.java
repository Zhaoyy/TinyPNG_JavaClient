import com.tinify.Tinify;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;
import swing.MainForm;
import utils.PropertiesUtil;

/**
 * Main
 *
 * @author Mislead
 *         DATE: 2015/10/9
 *         DESC:
 **/
public class Main {

  private static String TAG = "Main";
  private static String WORK_DIR = "d:" + File.separator;

  public static void main(String args[]) {
    Tinify.setKey("vPhKEx0a6_UZN1Aylky_Lz59m3uUDH38");
    try {
      PropertiesUtil.writePropertiesVal("key", Tinify.key());
    } catch (IOException e) {
      e.printStackTrace();
    }
    setWindowGlobalSetting();
    new MainForm().invokeJFram();
    //try {
    //  Source source = Tinify.fromFile(WORK_DIR + "logo.png");
    //  source.toFile(WORK_DIR + "out.png");
    //} catch (IOException e) {
    //  e.printStackTrace();
    //}
  }

  private static void setWindowGlobalSetting() {
    try {

      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      FontUIResource fontUIResource = new FontUIResource(new Font("新宋体", Font.PLAIN, 14));
      for (Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
        Object key = keys.nextElement();
        Object value = UIManager.get(key);
        if (value instanceof FontUIResource) {
          UIManager.put(key, fontUIResource);
        }
      }
    } catch (ClassNotFoundException ex) {
      ex.printStackTrace();
    } catch (InstantiationException ex) {
      ex.printStackTrace();
    } catch (IllegalAccessException ex) {
      ex.printStackTrace();
    } catch (UnsupportedLookAndFeelException ex) {
      ex.printStackTrace();
    }
  }
}
