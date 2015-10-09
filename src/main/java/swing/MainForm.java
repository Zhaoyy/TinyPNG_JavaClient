package swing;

import com.tinify.Tinify;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

/**
 * MainForm
 *
 * @author Mislead
 *         DATE: 2015/10/9
 *         DESC:
 **/
public class MainForm {

  private static String TAG = "MainForm";
  private static final int FRAME_WIDTH = 600;
  private static final int FRAME_HEIGHT = 400;
  private JButton btnSelect;
  private JPanel root;
  private JTextField tfPath;
  private JList list;
  private JButton btnCompress;
  private JButton btnClear;
  private JLabel lblStatus;

  private JFrame jFrame;

  public void invokeJFram() {
    setWindowStyle();
    jFrame = new JFrame("test");
    jFrame.setContentPane(root);
    tfPath.setText(Tinify.key());
    addMenu2Fram();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();

        jFrame.setLocation((dimension.width - FRAME_WIDTH) / 2,
            (dimension.height - FRAME_HEIGHT) / 2);
        jFrame.setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));

        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.pack();

        jFrame.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            System.exit(0);
          }
        });
        jFrame.setVisible(true);
      }
    });
  }

  private void addMenu2Fram() {
    JMenuBar menuBar = new JMenuBar();
    JMenu setting = new JMenu("Setting");
    setting.setMnemonic(KeyEvent.VK_S);
    JMenuItem addKey = new JMenuItem("Add key", KeyEvent.VK_K);
    setting.add(addKey);

    JMenu second = new JMenu("Second");
    //second.add(addKey);

    menuBar.add(setting);
    menuBar.add(second);
    addKey.addActionListener(new ActionListener() {
      @Override public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(jFrame, "add key clicked!");
      }
    });
    jFrame.setJMenuBar(menuBar);
  }

  private void setWindowStyle() {
    try {

      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); // Windows风格

      //UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel") ; // Mac风格

      //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel") ; // Java默认风格

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
