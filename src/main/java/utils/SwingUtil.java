package utils;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

/**
 * SwingUtil
 *
 * @author Mislead
 *         DATE: 2015/10/19
 *         DESC:
 **/
public class SwingUtil {

  private static final String TAG = "SwingUtil";

  public static FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);

  public static Font font = new Font("新宋体", Font.PLAIN, 14);

  public static int getStringWidth(String string) {
    if (StringUtil.isNullOrEmpty(string) || font == null) {
      return 0;
    }
    return (int) font.getStringBounds(string, frc).getWidth();
  }
}
