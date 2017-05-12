import com.tinify.Tinify;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;

import swing.MainForm;
import utils.PropertiesUtil;
import utils.StringUtil;
import utils.SwingUtil;

/**
 * Main
 *
 * @author Mislead
 *         DATE: 2015/10/9
 *         DESC:
 **/
public class AppMain {

    private static final String defaultKey = "vPhKEx0a6_UZN1Aylky_Lz59m3uUDH38";
    private static final String defaultOutDir = "out";
    private static final String KEY = "key";
    private static final String OUT_DIR = "outDir";

    public static void main(String args[]) {

        initProperties();

        try {
            Tinify.setKey(PropertiesUtil.getPropertiesValue(KEY, defaultKey));
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

    private static void initProperties() {
        try {
            String key = PropertiesUtil.getPropertiesValue(KEY, "");
            if (StringUtil.isNullOrEmpty(key)) {
                PropertiesUtil.writePropertiesVal(KEY, defaultKey);
            }
            File file = new File(defaultOutDir);
            if (!file.exists()) file.mkdirs();
            String outDir = PropertiesUtil.getPropertiesValue(OUT_DIR, "");
            if (StringUtil.isNullOrEmpty(outDir)) {
                PropertiesUtil.writePropertiesVal(OUT_DIR, file.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
