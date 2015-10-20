import com.tinify.Tinify;
import java.io.File;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import tinify.MySource;

import static org.junit.Assert.assertEquals;

/**
 * ControlTest
 *
 * @author Mislead
 *         DATE: 2015/10/12
 *         DESC:
 **/
public class ControlTest {

  private static final String TAG = "ControlTest";
  private static final String testDir = "d:" + File.separator;
  private static final String FileName = "logo.png";
  private MySource mySource;

  @Before public void setup() {
    Tinify.setKey("vPhKEx0a6_UZN1Aylky_Lz59m3uUDH38");
    mySource = new MySource(0, testDir + FileName);
  }

  @Test public void testUrl() {
    try {
      mySource.update2GetDownloadUrl();
      assertEquals(1, mySource.getStatus());
      mySource.getOutFileByUrl();
      assertEquals(2, mySource.getStatus());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
