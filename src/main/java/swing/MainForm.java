package swing;

import java.awt.Component;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import tinify.MySource;

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
  private DefaultTableModel model;
  private int oldDataCount = 0;

  private JFileChooser jFileChooser;
  private Object[] headers = new Object[] {
      "select", "Name", "status"
  };

  private Object[][] content = new Object[0][3];

  private JFrame jFrame;
  private List<MySource> mySources = new ArrayList<MySource>();

  private void bindActionListener() {
    btnSelectDir.addActionListener(this);
    btnClear.addActionListener(this);
    new DropTarget(table, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {
      @Override public void drop(DropTargetDropEvent dtde) {
        try {
          if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor))//如果拖入的文件格式受支持
          {
            dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);//接收拖拽来的数据
            List<File> list = (List<File>) (dtde.getTransferable()
                .getTransferData(DataFlavor.javaFileListFlavor));
            oldDataCount = mySources.size();
            for (File file : list) {
              if (file.isDirectory()) {
                setlectFiles(file.listFiles());
              } else {
                setlectFiles(file);
              }
            }
            dtde.dropComplete(true);//指示拖拽操作已完成
          } else {
            dtde.rejectDrop();//否则拒绝拖拽来的数据
          }
          bindData2JTable();
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
    initUIView();
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

  private void initUIView() {
    model = new DefaultTableModel(content, headers) {
      @Override public Class getColumnClass(int c) {
        Object value = getValueAt(0, c);
        if (value != null) {
          return value.getClass();
        } else {
          return super.getClass();
        }
      }

      @Override public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    table.setModel(model);
    final MyCheckBoxRenderer check = new MyCheckBoxRenderer();
    table.getTableHeader().getColumnModel().getColumn(0).setCellRenderer(check);
    table.setRowHeight(30);
    table.getTableHeader().addMouseListener(new MouseAdapter() {
      @Override public void mouseClicked(MouseEvent e) {
        if (table.getColumnModel().getColumnIndexAtX(e.getX()) == 0) {//如果点击的是第0列，即checkbox这一列
          boolean b = !check.isSelected();
          check.setSelected(b);
          table.getTableHeader().repaint();
          for (int i = 0; i < table.getRowCount(); i++) {
            table.getModel().setValueAt(b, i, 0);//把这一列都设成和表头一样
          }
        }
      }
    });
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
          oldDataCount = mySources.size();
          selectDirs(dirs);
          bindData2JTable();
          break;
        default:
          break;
      }
    } else if (btnClear == e.getSource()) {
      mySources.clear();
      bindData2JTable();
    }
  }

  private void selectDirs(File... dirs) {
    StringBuilder sb = new StringBuilder("  ");
    for (File dir : dirs) {
      sb.append(dir.getName() + File.separator + ";");
      setlectFiles(dir.listFiles());
    }
    tfPath.setText(sb.toString());
  }

  private void setlectFiles(File... files) {
    for (File file : files) {
      String filePath = file.getAbsolutePath();
      if (filePath.endsWith(".png") || filePath.endsWith(".jpg")) {
        MySource mySource = new MySource(filePath, filePath);
        mySources.add(mySource);
      }
    }
  }

  private void bindData2JTable() {
    content = new Object[mySources.size()][3];

    for (int i = 0; i < mySources.size(); i++) {
      MySource source = mySources.get(i);
      Object[] c = new Object[] { true, source.getSourcePath(), getStatus(source.getStatus()) };
      content[i] = c;
    }

    model.setDataVector(content, headers);
    fireTableRowsChanged(oldDataCount, content.length);
  }

  private String getStatus(int status) {
    switch (status) {
      case -1:
        return "无效文件";
      case 0:
        return "准备压缩";
      case 1:
        return "上传完成";
      case 2:
        return "下载完成";

      default:
        return "";
    }
  }

  public void fireTableRowsChanged(int oldCount, int newCount) {
    if (newCount == 0 && oldCount == 0) {
    } else if (newCount == 0 && oldCount > 0) {
      model.fireTableRowsDeleted(0, oldCount - 1);
    } else if (newCount > 0 && oldCount == 0) {
      model.fireTableRowsInserted(0, newCount - 1);
    } else if (newCount == oldCount) {
      model.fireTableRowsUpdated(0, newCount - 1);
    } else if (newCount > oldCount) {
      model.fireTableRowsInserted(oldCount, newCount - 1);
      model.fireTableRowsUpdated(0, oldCount - 1);
    } else {
      model.fireTableRowsDeleted(newCount, oldCount - 1);
      model.fireTableRowsUpdated(0, newCount - 1);
    }
  }

  class MyCheckBoxRenderer extends JCheckBox implements TableCellRenderer {

    public MyCheckBoxRenderer() {
      this.setBorderPainted(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int column) {
      return this;
    }
  }
}
