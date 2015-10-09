package utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * PropertiesUtil
 * AUTHOR:Zhaoyy  2015/10/8
 * DESC:
 **/
public class PropertiesUtil {

  private static final String TAG = "PropertiesUtil";
  private static final String PROPERTY_FILE_NAME = "tiny.properties";
  private static File PROPERTY_FILE = null;

  private static void init() throws IOException {
    if (PROPERTY_FILE == null) {
      PROPERTY_FILE = new File(PROPERTY_FILE_NAME);
    }

    if (!PROPERTY_FILE.exists()) {
      PROPERTY_FILE.createNewFile();
    }
  }

  public static String getPropertiesValue(String key, String defaultVal) throws IOException {

    init();

    Properties pps = new Properties();
    InputStream in = null;
    try {
      in = new BufferedInputStream(new FileInputStream(PROPERTY_FILE));
      pps.load(in);

      defaultVal = pps.getProperty(key, defaultVal);
    } finally {
      if (in != null) {
        in.close();
      }
    }

    return defaultVal;
  }

  public static Map<String, String> getAllPropertiesValue() throws IOException {
    init();
    Map<String, String> result = new HashMap<String, String>();
    Properties pps = new Properties();
    InputStream in = null;
    try {
      in = new BufferedInputStream(new FileInputStream(PROPERTY_FILE));
      pps.load(in);

      Enumeration enumeration = pps.propertyNames();

      while (enumeration.hasMoreElements()) {
        String key = (String) enumeration.nextElement();
        String val = pps.getProperty(key);
        result.put(key, val);
      }
    } finally {
      if (in != null) {
        in.close();
      }
    }

    return result;
  }

  public static void writePropertiesVal(String key, String val) throws IOException {
    writePropertiesVal(key, val, null);
  }

  public static void writePropertiesVal(String key, String val, String comments)
      throws IOException {
    init();
    Properties pps = new Properties();
    InputStream in = null;
    OutputStream out = null;
    try {
      in = new BufferedInputStream(new FileInputStream(PROPERTY_FILE));
      pps.load(in);
      out = new FileOutputStream(PROPERTY_FILE);
      pps.setProperty(key, val);
      pps.store(out, comments);
    } finally {
      if (in != null) {
        in.close();
      }

      if (out != null) {
        out.close();
      }
    }
  }
}
