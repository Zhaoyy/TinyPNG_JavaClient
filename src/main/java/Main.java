import com.tinify.Tinify;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;
import swing.MainForm;
import utils.PropertiesUtil;
import utils.SwingUtil;

/**
 * Main
 *
 * @author Mislead
 *         DATE: 2015/10/9
 *         DESC:
 **/
public class Main {

  private static String TAG = "Main";
  public static String WORK_DIR = "d:" + File.separator + "out" + File.separator;

  public static void main(String args[]) {

    File outDir = new File(WORK_DIR);

    if (!outDir.exists()) {
      outDir.mkdirs();
    }

    Tinify.setKey("vPhKEx0a6_UZN1Aylky_Lz59m3uUDH38");
    try {
      PropertiesUtil.writePropertiesVal("key", Tinify.key());
    } catch (IOException e) {
      e.printStackTrace();
    }
    setWindowGlobalSetting();
    new MainForm().invokeJFram();
  }

  private static void setWindowGlobalSetting() {
    try {

      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      FontUIResource fontUIResource = new FontUIResource(SwingUtil.font);
      for (Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
        Object key = keys.nextElement();
        Object value = UIManager.get(key);
        if (value instanceof FontUIResource) {
          UIManager.put(key, fontUIResource);
          break;
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
