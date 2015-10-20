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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import tinify.MySource;
import utils.SwingUtil;

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
  public static String WORK_DIR = "d:" + File.separator + "out" + File.separator;
  private JButton btnSelectDir;
  private JPanel root;
  private JTextField tfPath;
  private JButton btnCompress;
  private JButton btnClear;
  private JTable table;
  private JCheckBox chbAll;
  private JScrollPane jScrollPane;
  private DefaultTableModel model;

  private JFileChooser jFileChooser;
  private Vector<Object> headers = new Vector<Object>();

  private Vector<Vector<Object>> data = new Vector<Vector<Object>>();

  private JFrame jFrame;
  private List<MySource> mySources = new ArrayList<MySource>();

  private ExecutorService service = Executors.newCachedThreadPool();

  private void bindActionListener() {
    btnSelectDir.addActionListener(this);
    btnClear.addActionListener(this);
    btnCompress.addActionListener(this);
    new DropTarget(jScrollPane, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {
      @Override public void drop(DropTargetDropEvent dtde) {
        try {
          //如果拖入的文件格式受支持
          if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);//接收拖拽来的数据
            List<File> list = (List<File>) (dtde.getTransferable()
                .getTransferData(DataFlavor.javaFileListFlavor));
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
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }, true);

    chbAll.addItemListener(new ItemListener() {
      @Override public void itemStateChanged(ItemEvent e) {
        if (chbAll == e.getSource()) {

          setAllCheckedOrNot(chbAll.isSelected());
        }
      }
    });

    MySource.setListenner(new MySource.StateUpdateListenner() {
      @Override public void update(int index) {
        updateItem(index, mySources.get(index));
      }
    });
  }

  public void invokeJFram() {

    initHeader();
    jFrame = new JFrame("TinyPNG Client");
    jFrame.setContentPane(root);
    addMenu2Frame();
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

            service.shutdown();
            System.exit(0);
          }
        });
        jFrame.setVisible(true);
      }
    });
  }

  private void initHeader() {
    headers.add("Select");
    headers.add("FileName");
    headers.add("Status");
  }

  private void addMenu2Frame() {
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
    model = new DefaultTableModel(data, headers) {
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
    table.getColumnModel()
        .getColumn(0)
        .setMaxWidth(SwingUtil.getStringWidth(headers.get(0).toString()) + 10);
    table.setRowHeight(30);
    //table.getTableHeader().addMouseListener(new MouseAdapter() {
    //  @Override public void mouseClicked(MouseEvent e) {
    //    if (table.getColumnModel().getColumnIndexAtX(e.getX()) == 0) {//如果点击的是第0列，即checkbox这一列
    //      boolean b = !check.isSelected();
    //      check.setSelected(b);
    //      table.getTableHeader().repaint();
    //      for (int i = 0; i < table.getRowCount(); i++) {
    //        table.getModel().setValueAt(b, i, 0);//把这一列都设成和表头一样
    //      }
    //    }
    //  }
    //});
  }

  private void setAllCheckedOrNot(boolean check) {
    table.getTableHeader().repaint();

    for (int i = 0; i < table.getRowCount(); i++) {
      mySources.get(i).setSelected(check);
      table.getModel().setValueAt(check, i, 0);
    }
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
    } else if (btnClear == e.getSource()) {
      clearAllItem();
    } else if (btnCompress == e.getSource()) {
      for (MySource mySource : mySources) {

        if (mySource.isSelected() && mySource.getStatus() == 0) {
          try {
            service.execute(mySource.getCompressThread());
          } catch (IOException e1) {
            e1.printStackTrace();
          }
        }
      }
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

    int index = mySources.size();
    int lastCount = index;
    for (File file : files) {
      String filePath = file.getAbsolutePath();
      if (filePath.endsWith(".png") || filePath.endsWith(".jpg")) {
        MySource mySource = new MySource(index, filePath, WORK_DIR);
        mySources.add(mySource);
        index++;
      }
    }

    addItems(lastCount, mySources.size());
  }

  private void addItems(int lastCount, int nowCount) {
    for (int i = lastCount; i < nowCount; i++) {
      MySource mySource = mySources.get(i);
      Vector<Object> item = new Vector<Object>();
      item.add(mySource.isSelected());
      item.add(mySource.getFileName());
      item.add(getStatus(mySource));

      data.add(item);
    }

    bindData2JTable(lastCount, nowCount);
  }

  private void updateItem(int index, MySource mySource) {

    Vector<Object> item = data.get(index);
    item.setElementAt(mySource.isSelected(), 0); // selected
    item.setElementAt(getStatus(mySource), 2); // status

    bindData2JTable(data.size(), data.size());
  }

  private void clearAllItem() {
    boolean allDone = true;
    for (MySource mySource : mySources) {
      // waiting for download
      if (mySource.getStatus() == 1) {
        allDone = false;
        break;
      }
    }

    if (allDone) {
      data.clear();
      bindData2JTable(mySources.size(), data.size());
      mySources.clear();
    } else {
      JOptionPane.showMessageDialog(jFrame, "请等待已经开始的任务完成！");
    }
  }

  private synchronized void bindData2JTable(int lastCount, int nowCount) {

    model.setDataVector(data, headers);
    fireTableRowsChanged(lastCount, nowCount);
    table.getColumnModel()
        .getColumn(0)
        .setMaxWidth(SwingUtil.getStringWidth(headers.get(0).toString()) + 10);
  }

  private String getStatus(MySource mySource) {
    switch (mySource.getStatus()) {
      case -1:
        return "无效文件";
      case 0:
        return "等待压缩";
      case 1:
        return "准备上传";
      case 2:
        return "上传完成";
      case 3:
        return "下载完成 " + mySource.getCompressPercent();

      default:
        return "";
    }
  }

  public void fireTableRowsChanged(int oldCount, int newCount) {
    if (newCount != 0 || oldCount != 0) {
      if (newCount == 0 && oldCount > 0) {
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
  }

  class MyCheckBoxRenderer extends JCheckBox implements TableCellRenderer {

    public MyCheckBoxRenderer() {
      this.setBorderPainted(true);
      setSelected(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int column) {
      return this;
    }
  }
}
