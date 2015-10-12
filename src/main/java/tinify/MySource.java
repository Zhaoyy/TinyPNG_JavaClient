package tinify;

import com.squareup.okhttp.Response;
import com.tinify.Client;
import com.tinify.Options;
import com.tinify.Tinify;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import utils.StringUtil;

/**
 * MySource
 *
 * @author Mislead
 *         DATE: 2015/10/10
 *         DESC:
 **/
public class MySource {

  private static final String TAG = "MySource";

  /**
   * image compress method:
   *
   * scale: Scales the image down proportionally. You must provide either a target width or a
   * target height, but not both. The scaled image will have exactly the provided width or height.
   *
   * fit: Scales the image down proportionally so that it fits within the given dimensions. You
   * must
   * provide both a width and a height. The scaled image will not exceed either of these
   * dimensions.
   *
   * cover: Scales the image proportionally and crops it if necessary so that the result has
   * exactly
   * the given dimensions. You must provide both a width and a height. Which parts of the image are
   * cropped away is determined automatically. An intelligent algorithm determines the most
   * important areas and leaves these intact
   */

  public static final String RESIZE_METHOD_SCALE = "scale";
  public static final String RESIZE_METHOD_FIT = "fit";
  public static final String RESIZE_METHOD_COVER = "cover";

  private boolean selected = true;
  private String sourcePath;
  private String outPath;

  private String url;
  private Options options = null;
  private int status = 0; // 0-准备好，1-上传完成，2-下载完成, -1-非法文件

  public MySource(String sourcePath, String outPath) {
    this.sourcePath = sourcePath;
    this.outPath = outPath;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public String getSourcePath() {
    return sourcePath;
  }

  public void setSourcePath(String sourcePath) {
    this.sourcePath = sourcePath;
  }

  public String getOutPath() {
    return outPath;
  }

  public void setOutPath(String outPath) {
    this.outPath = outPath;
  }

  public int getStatus() {
    return status;
  }

  public boolean isAllowed() {
    return sourcePath.endsWith(".png")
        || sourcePath.endsWith(".jpg")
        || (getFileSize(sourcePath) % 1024 % 1024) < 3;
  }

  public long getFileSize(String path) {
    File file = new File(path);
    if (!file.exists() || file.isDirectory()) {
      return 0;
    }

    return file.length();
  }

  public String update2GetDownloadUrl() throws IOException {

    if (!isAllowed()) {
      status = -1;
      url = null;
    } else {
      Response response = Tinify.client()
          .request(Client.Method.POST, "/shrink", Files.readAllBytes(Paths.get(sourcePath)));
      url = response.header("Location");
      status = 1;
    }

    return url;
  }

  public void setOutImageSize(String method, int width, int height) {
    if (options == null) {
      options = new Options();
    }
    Options command =
        new Options().with("method", method).with("width", width).with("height", height);
    options.with("resize", command);
  }

  public void getOutFileByUrl() throws IOException {
    if (StringUtil.isNullOrEmpty(url)) {
      throw new NullPointerException("url is empty or null!");
    }

    Response response;
    if (options == null) {
      response = Tinify.client().request(Client.Method.GET, url);
    } else {
      response = Tinify.client().request(Client.Method.POST, url, options);
    }
    Files.write(Paths.get(outPath), response.body().bytes());
    status = 2;
  }
}
