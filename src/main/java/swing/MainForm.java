package swing;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * MainForm
 *
 * @author Mislead
 *         DATE: 2015/10/9
 *         DESC:
 **/
public class MainForm implements ActionListener {

  private static String TAG = "MainForm";
  private static final int FRAME_WIDTH = 600;
  private static final int FRAME_HEIGHT = 400;
  private JButton btnSelectDir;
  private JPanel root;
  private JTextField tfPath;
  private JButton btnCompress;
  private JButton btnClear;
  private JLabel lblStatus;
  private JTable table;
  private JCheckBox chbAll;

  private JFileChooser jFileChooser;

  private JFrame jFrame;

  private void bindActionListener() {
    btnSelectDir.addActionListener(this);
    new DropTarget(table, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {
      @Override public void drop(DropTargetDropEvent dtde) {
        try {
          if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor))//如果拖入的文件格式受支持
          {
            dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);//接收拖拽来的数据
            List<File> list = (List<File>) (dtde.getTransferable()
                .getTransferData(DataFlavor.javaFileListFlavor));
            for (File file : list) {
              if (file.isDirectory()) {
                selectDirs(file);
              } else {
                setlectFiles(file);
              }
            }
            dtde.dropComplete(true);//指示拖拽操作已完成
          } else {
            dtde.rejectDrop();//否则拒绝拖拽来的数据
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }, true);
  }

  public void invokeJFram() {

    jFrame = new JFrame("test");
    jFrame.setContentPane(root);
    addMenu2Fram();
    bindActionListener();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        // center in screen
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
    JMenu setting = new JMenu("设置");

    setting.setMargin(new Insets(8, 8, 8, 8));
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

  private JFileChooser getJFileChooser() {
    if (jFileChooser == null) {
      jFileChooser = new JFileChooser();
    }
    return jFileChooser;
  }

  @Override public void actionPerformed(ActionEvent e) {
    if (btnSelectDir == e.getSource()) {
      JFileChooser fileChooser = getJFileChooser();
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      fileChooser.setMultiSelectionEnabled(true);
      int result = fileChooser.showOpenDialog(jFrame);

      switch (result) {
        case JFileChooser.APPROVE_OPTION:
          File[] dirs = fileChooser.getSelectedFiles();
          selectDirs(dirs);
          break;
        default:
          break;
      }
    }
  }

  private void selectDirs(File... dirs) {
    StringBuilder sb = new StringBuilder("  ");
    for (File dir : dirs) {
      sb.append(dir.getName() + File.separator + ";");
    }
    tfPath.setText(sb.toString());
  }

  private void setlectFiles(File... files) {
    StringBuilder sb = new StringBuilder("  ");
    for (File dir : files) {
      sb.append(dir.getName() + ";");
    }
    tfPath.setText(sb.toString());
  }
}
