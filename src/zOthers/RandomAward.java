import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class RandomAward extends JFrame {
    private static final long serialVersionUID = 7510608124693852355L;
    private JButton b_start = new JButton("开始");
    private JButton b_stop = new JButton("停止");

    private ArrayList<String> printIdentityList = new ArrayList<>();
    ArrayList<String> identityNumber = new ArrayList<>();
    ArrayList<String> removedNumber = new ArrayList<>();
    private JTextField mIdentityNumberTitle = new JTextField();
    JTextField mIdentityNumberRoll = new JTextField();
    private JTextArea mPrizeResult = new JTextArea();
    private JLabel l_information = new JLabel();
    private JFileChooser fileChooser = new JFileChooser();

    private RandomThread awardThread = null;
    private final JComboBox mComboBox;
    private String mStoragePath;

    private RandomAward() {
        super("随机抽奖系统");
        mIdentityNumberTitle.setEditable(false);
        mIdentityNumberRoll.setEditable(false);
        mPrizeResult.setEditable(false);
        Container contentPane = getContentPane();
        Font setting = new Font("null", Font.PLAIN, 20);
        JMenu fileMenu = new JMenu("文件");
        fileMenu.setFont(setting);
        JMenuItem[] fileItems = { new JMenuItem("导入名单"), new JMenuItem("保存"), new JMenuItem("退出") };
        fileItems[0].setFont(setting);
        fileItems[1].setFont(setting);
        fileItems[2].setFont(setting);
        //注册加载事件的监听器
        fileItems[0].addActionListener(this::loadMemberList);
        //注册打印事件的监听器
        fileItems[1].addActionListener(this::savePickedMember);
        //注册退出事件的监听器
        fileItems[2].addActionListener(e -> System.exit(0));
        //将菜单子项加入菜单中
        for (JMenuItem fileI : fileItems) {
            fileMenu.add(fileI);
            fileMenu.addSeparator();
        }
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
//        setJMenuBar(menuBar);

        //注册开始事件的监听器
        b_start.addActionListener(this::startPick);
        //注册停止事件的监听器
        b_stop.addActionListener(this::stopPick);

        addWindowListener(new WindowAdapter() {  //注册应用关闭事件的监听器
            public void windowClosing (WindowEvent e) {
                System.exit(0);
            }});

        //状态栏
        JPanel p_south = new JPanel();
        p_south.setLayout(new FlowLayout(FlowLayout.LEFT));
        l_information.setForeground(Color.blue);
        l_information.setFont(setting);
        JLabel l_sysInformation = new JLabel("系统信息:");
        l_sysInformation.setFont(setting);
        p_south.add(l_sysInformation);
        p_south.add(l_information);
        contentPane.add(p_south, BorderLayout.SOUTH);

        Font prizeResult = new Font("null", Font.PLAIN, 20);
        mPrizeResult.setFont(prizeResult);
        mPrizeResult.setText("中奖记录：\r\n");
        JScrollPane jsp = new JScrollPane(mPrizeResult);
        new DropTarget(mPrizeResult, DnDConstants.ACTION_COPY_OR_MOVE, mDropTargetAdapter);
        contentPane.add(jsp, BorderLayout.WEST);

        //输出内容显示部分
        Font identityTitle = new Font("null", Font.PLAIN, 40);
        mIdentityNumberTitle.setFont(identityTitle);
        mIdentityNumberTitle.setHorizontalAlignment(SwingConstants.CENTER);
        mIdentityNumberTitle.setText("幸运大奖得主");
        Font identityResult = new Font("null", Font.BOLD, 60);
        mIdentityNumberRoll.setFont(identityResult);
        mIdentityNumberRoll.setForeground(Color.red);
        mIdentityNumberRoll.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel p_center = new JPanel();
        p_center.setLayout(new GridLayout(2, 1, 10, 10));
        p_center.add(mIdentityNumberTitle);
        p_center.add(mIdentityNumberRoll);
        new DropTarget(mIdentityNumberTitle, DnDConstants.ACTION_COPY_OR_MOVE, mDropTargetAdapter);
        new DropTarget(mIdentityNumberRoll, DnDConstants.ACTION_COPY_OR_MOVE, mDropTargetAdapter);
        contentPane.add(p_center, BorderLayout.CENTER);

        //启动与停止
        Font comboBoxOption = new Font("null", Font.PLAIN, 20);
        String[] prizeLevel = new String[] {"一等奖", "二等奖", "三等奖", "四等奖", "参与奖"};
        mComboBox = new JComboBox(prizeLevel);
        mComboBox.setFont(comboBoxOption);
        mComboBox.setMaximumRowCount(5);
        Font button = new Font("null", Font.PLAIN, 40);
        b_start.setFont(button);
        b_start.setForeground(Color.MAGENTA);
        b_stop.setFont(button);
        b_stop.setForeground(Color.ORANGE);
        JPanel p_east = new JPanel();
        p_east.setLayout(new GridLayout(3, 1, 10, 10));
        p_east.add(mComboBox);
        p_east.add(b_start);
        p_east.add(b_stop);
        contentPane.add(p_east, BorderLayout.EAST);
    }

    private DropTargetAdapter mDropTargetAdapter = new DropTargetAdapter() {
        @Override
        public void drop(DropTargetDropEvent dtde) {
            try {
                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                    @SuppressWarnings("unchecked")
                    List<File> list = (List<File>) (dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
                    File f = list.get(0);
                    try {
                        l_information.setText("数据加载中，请稍等...");
                        identityNumber.clear();
                        Scanner scanner = new Scanner(f);
                        while (scanner.hasNextLine()) {
                            String line = scanner.nextLine().trim();
                            if (line!=null && line.length()!=0) {
                                identityNumber.add(line);
                            }
                        }
                        identityNumber.removeAll(removedNumber);
                        l_information.setText("数据加载完成！");
                    } catch(Exception ex) {
                        l_information.setText("数据格式不正确，请重新加载！");
                        ex.printStackTrace();
                    }
                    dtde.dropComplete(true);
                } else {
                    dtde.rejectDrop();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /*
    * 加载按钮事件方法
    */
    private void loadMemberList(ActionEvent e) {
        //从字符输入流中读取文本，缓冲各个字符，从而提供字符、数组和行的高效读取
        BufferedReader reader = null;
        //此方法会返回一个int值
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        System.out.println("path1:" + path);
        try {
            path = java.net.URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        System.out.println("path2:" + path);
        int index = path.lastIndexOf("/");
        fileChooser.setCurrentDirectory(new File(path.substring(0, index)));
        int i = fileChooser.showOpenDialog(null); // 显示打开文件对话框
        if (i == JFileChooser.APPROVE_OPTION) { // 点击对话框中打开选项
            File f = fileChooser.getSelectedFile(); // 得到所选择的文件
            mStoragePath = f.getParent() + "/";
            try {
                l_information.setText("数据加载中，请稍等...");
                Scanner scanner = new Scanner(f);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (line!=null && line.length()!=0) {
                        identityNumber.add(line);
                    }
                }
                l_information.setText("数据加载完成！");
            } catch(Exception ex) {
                l_information.setText("数据格式不正确，请重新加载！");
                ex.printStackTrace();
            }
        }
    }

    /*
    * 启动按钮的事件
    */
    private void startPick(ActionEvent e) {
        //判断存储两个标记的向量中是否为空
        if (identityNumber.size() <= 0) {
            l_information.setText("数据没有加载,请加载数据!");
        } else {
            awardThread = new RandomThread(this);
            awardThread.changeflag_start();
            l_information.setText("将产生本次抽奖的大奖得主");
            mIdentityNumberTitle.setText("选取中...");
            b_start.setEnabled(false);
            b_stop.setEnabled(true);
            mComboBox.setEnabled(false);
        }
    }

    /*
    * 暂停按钮的事件
    */
    private void stopPick(ActionEvent e) {
        //将跳转的数字置于停止状态
        awardThread.changeflag_stop();
        String selected_number = "";

        for (int k = 0; k < identityNumber.size(); k++) {
            if ((mIdentityNumberRoll.getText()).equals(identityNumber.get(k))) {
                selected_number = identityNumber.get(k);
                identityNumber.remove(k);
                removedNumber.add(selected_number);
                break;
            }
        }
        String awardMessage = String.format("本次<%s>抽奖得主：%s\r\n", mComboBox.getSelectedItem().toString(), selected_number);
        mIdentityNumberTitle.setText(awardMessage);
        b_start.setText("继续");
        mPrizeResult.append(String.format("<%s>:%s\r\n", mComboBox.getSelectedItem().toString(), selected_number));
        printIdentityList.add(awardMessage);
        b_start.setEnabled(true);
        b_stop.setEnabled(false);
        mComboBox.setEnabled(true);
    }

    /*
    * 输出按钮的事件
    */
    private void savePickedMember(ActionEvent e) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            String saveFileName = String.format("result-%s.txt", sdf.format(new Date(System.currentTimeMillis())));
            Writer outTxt = new OutputStreamWriter(new FileOutputStream(new File("C:\\" + saveFileName),true), "UTF-8");
            for (String aPrintIdentityList : printIdentityList) {
                outTxt.write(aPrintIdentityList);
            }
            outTxt.close();
            l_information.setText("文件输出成功！保存在当前目录下..");
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }

    }

    /*
    * 程序的入口
    */
    public static void main(String[] args) {
        //根据经验，这条语句只能在第一行
//        JFrame.setDefaultLookAndFeelDecorated(true);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.out.println("Use System UI");
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
            JFrame.setDefaultLookAndFeelDecorated(true);
            System.out.println("Use Java UI");
        }
        RandomAward award = new RandomAward();
        award.setSize(1366, 768);
        award.setLocationRelativeTo(null);//居中显示
        award.setVisible(true);
//        award.setAlwaysOnTop(true);//置顶
        award.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

/*
 * 定义的线程类,该线呈是循环的显示号码
 */
class RandomThread extends Thread {
    private boolean runFlag = true;//决定此线程是否运行的标记
    //需要该对象来读取文本框字段，不用创建它，申明下就好
    private RandomAward chooseAward = null;
    //创建一个新的随机数生成器
    private Random randomNumber = new Random();
    public RandomThread(Object obj) {
        start();
        chooseAward = (RandomAward) obj;
    }

    public void start() {
        runFlag = false;
        super.start();
    }

    void changeflag_start() {
        runFlag = true;
    }

    void changeflag_stop() {
        runFlag = false;
    }

    public void run() {
        while (runFlag) {
            int num = randomNumber.nextInt(chooseAward.identityNumber.size());
            chooseAward.mIdentityNumberRoll.setText(chooseAward.identityNumber.get(num));
            try {
                sleep(50);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
