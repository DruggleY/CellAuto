package main;

import main.presource.Point;
import main.presource.Sources;

import java.awt.Color;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Timer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

//界面单元
class NButton extends JButton {
    public Bool status;

    public NButton(String text) {
        super(text);
    }
}

class Bool {
    public boolean value = false;

    public Bool() {
        value = false;
    }
}

public class Cells extends JFrame {
    //功能实现
    private static NButton[][] buttongroup;//界面单元组
    private static Bool[][] now;//目前单元
    private static boolean[][] next;//下一时刻单元值
    private static JButton start;//开始推进按钮
    private static JComboBox ysmx,tkys,fgdx,jsgz;//四个选择设置
    private static Timer timer;//主计时器

    //重要参数
    private static int Bamount;//一行方格数
    private static int Bsize = 10;//方格大小
    private static int cachesize = 20;//边界缓冲
    private static int changespeed = 500;//推进速度
    private static Color nowcolor = Color.RED;//当前颜色
    private static final Map<String, Color> colormap;//颜色对照表
    static {
        colormap = new HashMap<String, Color>();
        colormap.put("红色", Color.RED);
        colormap.put("黄色", Color.YELLOW);
        colormap.put("蓝色", Color.BLUE);
        colormap.put("黑色", Color.BLACK);
    }

    private static String nowrule = "B3/S23";//规则字符串
    private static Set<Integer> RuleB;//规则B部分
    private static Set<Integer> RuleS;//规则S部分
    private static Sources sources = null;//预设资源


    //状态判断
    private static boolean running = false;//是否正在自动推进
    private static boolean isdown1 = false;//左键是否按下
    private static boolean isdown2 = false;//右键是否摁下
    //开始主计时器
    private static void starttimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < Bamount + cachesize; i++)
                    for (int j = 0; j < Bamount + cachesize; j++) {
                        // 3*3
                        int amount = 0;
                        for (int p = Math.max(0, i - 1); p <= Math.min(i + 1, Bamount + cachesize - 1); p++)
                            for (int q = Math.max(0, j - 1); q <= Math.min(j + 1, Bamount + cachesize - 1); q++) {
                                if (p == i && q == j)
                                    continue;
                                if (now[p][q].value) {
                                    amount++;
                                }
                            }
                        next[i][j] = now[i][j].value?RuleS.contains(amount):RuleB.contains(amount);
                    }
                for (int i = 0; i < Bamount + cachesize; i++)
                    for (int j = 0; j < Bamount + cachesize; j++) {
                        if (i >= cachesize / 2 && i < Bamount + cachesize / 2 && j >= cachesize / 2 && j < Bamount + cachesize / 2 && now[i][j].value != next[i][j]) {
                            if (next[i][j])
                                buttongroup[i - cachesize / 2][j - cachesize / 2].setBackground(nowcolor);
                            else
                                buttongroup[i - cachesize / 2][j - cachesize / 2].setBackground(Color.white);
                        }
                        now[i][j].value = next[i][j];
                    }
            }

        }, 0, changespeed);
        running = true;
        start.setText("停止推进");
    }


    //初始化主界面
    private static void init_panel_object(JPanel panel) {
        Bamount = (int) 750 / Bsize;
        buttongroup = new NButton[Bamount][Bamount];
        now = new Bool[Bamount + cachesize][Bamount + cachesize];
        next = new boolean[Bamount + cachesize][Bamount + cachesize];
        for (int i = 0; i < Bamount + cachesize; i++)
            for (int j = 0; j < Bamount + cachesize; j++) {
                now[i][j] = new Bool();
                next[i][j] = false;
            }
        for (int i = 0; i < Bamount; i++)
            for (int j = 0; j < Bamount; j++) {
                buttongroup[i][j] = new NButton("");
                buttongroup[i][j].setBorderPainted(false);
                //buttongroup[i][j].setBounds(j * (Bsize + 1) - 1 + 10, i * (Bsize + 1) - 1 + 10, Bsize, Bsize);
                buttongroup[i][j].setBounds(j * Bsize + 10, i * Bsize + 10, Bsize, Bsize);
                buttongroup[i][j].setBackground(Color.white);
                buttongroup[i][j].status = now[i + cachesize / 2][j + cachesize / 2];
                buttongroup[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        if (!running) {
                            NButton target = (NButton) arg0.getSource();
                            if (target.getBackground() == Color.white)
                                target.setBackground(nowcolor);
                            else
                                target.setBackground(Color.white);
                            target.status.value = !target.status.value;
                        }
                        isdown1 = false;
                    }
                });
                buttongroup[i][j].addMouseListener(new MouseListener() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1)
                            isdown1 = false;
                        else if (e.getButton() == MouseEvent.BUTTON3)
                            isdown2 = false;
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if ((isdown1 || isdown2) && !running) {
                            NButton target = (NButton) e.getSource();
                            if (isdown1) {
                                target.setBackground(nowcolor);
                                target.status.value = true;
                            } else {
                                target.setBackground(Color.white);
                                target.status.value = false;
                            }
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {}

                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1)
                            isdown1 = true;
                        else if (e.getButton() == MouseEvent.BUTTON3)
                            isdown2 = true;

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1)
                            isdown1 = false;
                        else if (e.getButton() == MouseEvent.BUTTON3)
                            isdown2 = false;
                    }

                });
                panel.add(buttongroup[i][j]);
            }
    }

    public static void main(String[] args) {
        try {
            Cells frame = new Cells();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the frame.
     */
    public Cells() {
        //初始化各功能组件
        this.setTitle("生命游戏");

        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 810);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        JPanel panel = new JPanel();
        panel.addMouseListener(new MouseListener() {
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseClicked(MouseEvent arg0) {}
            @Override
            public void mouseEntered(MouseEvent arg0) {}
            @Override
            public void mouseExited(MouseEvent arg0) {
                isdown1 = false;
                isdown2 = false;
            }

            @Override
            public void mouseReleased(MouseEvent arg0) {
                if (arg0.getButton() == MouseEvent.BUTTON1)
                    isdown1 = false;
                else if (arg0.getButton() == MouseEvent.BUTTON3)
                    isdown2 = false;
            }

        });
        panel.setBounds(10, 10, 750, 750);
        contentPane.add(panel);
        panel.setLayout(null);
        init_panel_object(panel);


        //参数初始化
        RuleB = new HashSet<Integer>();
        RuleB.add(3);
        RuleS = new HashSet<Integer>();
        RuleS.add(2);
        RuleS.add(3);

        sources = new Sources();


        //初始化小组件
        start = new JButton("\u5F00\u59CB\u63A8\u8FDB");
        start.setBounds(820, 119, 113, 27);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (running) {
                    timer.cancel();
                    running = false;
                    ((JButton) arg0.getSource()).setText("开始推进");
                } else {
                    starttimer();
                }

            }
        });
        contentPane.add(start);


        JButton clear = new JButton("\u6E05\u7A7A\u9875\u9762");
        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (running) {
                    timer.cancel();
                    running = false;
                    start.setText("开始推进");
                }
                for (int i = 0; i < Bamount + cachesize; i++) {
                    for (int j = 0; j < Bamount + cachesize; j++) {
                        now[i][j].value = false;
                    }
                }
                for (int i = 0; i < Bamount; i++)
                    for (int j = 0; j < Bamount; j++)
                        buttongroup[i][j].setBackground(Color.white);

            }
        });
        clear.setBounds(820, 159, 113, 27);
        contentPane.add(clear);
        //导入按钮及其功能实现
        JButton importm = new JButton("导入");
        importm.setBounds(820, 199, 113, 27);
        contentPane.add(importm);
        importm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser fc = new JFileChooser(".");
                int returnVal = fc.showOpenDialog(importm);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try{
                        JOptionPane.showMessageDialog(null,"导入中,请等待....","Please Wait",JOptionPane.INFORMATION_MESSAGE);
                        File file = fc.getSelectedFile();
                        FileReader reader = new FileReader(file);
                        BufferedReader br = new BufferedReader(reader);
                        String line = null;
                        int flag=0;
                        if (running) {
                            timer.cancel();
                            running = false;
                            start.setText("开始推进");
                        }
                        ysmx.setSelectedIndex(0);
                        while((line = br.readLine()) != null)
                        {
                            if(line.charAt(0)=='#')
                                continue;
                            if(flag==0)
                            {
                                flag++;
                                Bsize = Integer.parseInt(line.substring(0,2));
                                fgdx.setSelectedItem(String.valueOf(Bsize)+"px");
                                String colorstr=line.substring(2,line.length()-2);
                                switch (colorstr)
                                {
                                    case "RED":
                                        nowcolor=Color.RED;tkys.setSelectedItem("红色");break;
                                    case "BLACK":
                                        nowcolor=Color.BLACK;tkys.setSelectedItem("黑色");break;
                                    case "BLUE":
                                        nowcolor=Color.BLUE;tkys.setSelectedItem("蓝色");break;
                                    case "YELLOW":
                                        nowcolor=Color.YELLOW;tkys.setSelectedItem("黄色");break;
                                }

                                cachesize = Integer.parseInt(line.substring(line.length()-2,line.length()));
                                panel.removeAll();
                                init_panel_object(panel);
                                panel.repaint();
                            }else if(flag==1)
                            {
                                flag++;
                                String rule=line.replace("\n","");
                                if (!nowrule.equals(rule)) {
                                    nowrule = rule;
                                    String[] temp = nowrule.split("/");
                                    RuleB = new HashSet<Integer>();
                                    RuleS = new HashSet<Integer>();
                                    for (int i = 1; i < temp[0].length(); i++) {
                                        RuleB.add(Integer.valueOf(temp[0].substring(i, i + 1)));
                                    }
                                    for (int i = 1; i < temp[1].length(); i++) {
                                        RuleS.add(Integer.valueOf(temp[1].substring(i, i + 1)));
                                    }
                                }
                                jsgz.setSelectedItem(nowrule);

                            }else
                            {
                                int i=0,j=0;
                                for(int k=0;k<line.length();k++)
                                {
                                    int num;
                                    switch (line.charAt(k))
                                    {
                                        case 'F':num=15;break;
                                        case 'E':num=14;break;
                                        case 'D':num=13;break;
                                        case 'C':num=12;break;
                                        case 'B':num=11;break;
                                        case 'A':num=10;break;
                                        case '9':num=9;break;
                                        case '8':num=8;break;
                                        case '7':num=7;break;
                                        case '6':num=6;break;
                                        case '5':num=5;break;
                                        case '4':num=4;break;
                                        case '3':num=3;break;
                                        case '2':num=2;break;
                                        case '1':num=1;break;
                                        case '0':num=0;break;
                                        default:num=0;
                                    }
                                    if(num>=8)
                                    {
                                        num-=8;
                                        now[i][j].value=true;
                                        if(i >= cachesize / 2 && i < Bamount + cachesize / 2 && j >= cachesize / 2 && j < Bamount + cachesize / 2 )
                                        {
                                            buttongroup[i-cachesize/2][j-cachesize/2].setBackground(nowcolor);
                                        }
                                    }
                                    j+=1;
                                    if(j==Bamount+cachesize)
                                    {
                                        j=0;
                                        i+=1;
                                    }
                                    if(num>=4)
                                    {
                                        num-=4;
                                        now[i][j].value=true;
                                        if(i >= cachesize / 2 && i < Bamount + cachesize / 2 && j >= cachesize / 2 && j < Bamount + cachesize / 2 )
                                        {
                                            buttongroup[i-cachesize/2][j-cachesize/2].setBackground(nowcolor);
                                        }
                                    }
                                    j+=1;
                                    if(j==Bamount+cachesize)
                                    {
                                        j=0;
                                        i+=1;
                                    }
                                    if(num>=2)
                                    {
                                        num-=2;
                                        now[i][j].value=true;
                                        if(i >= cachesize / 2 && i < Bamount + cachesize / 2 && j >= cachesize / 2 && j < Bamount + cachesize / 2 )
                                        {
                                            buttongroup[i-cachesize/2][j-cachesize/2].setBackground(nowcolor);
                                        }
                                    }
                                    j+=1;
                                    if(j==Bamount+cachesize)
                                    {
                                        j=0;
                                        i+=1;
                                    }
                                    if(num>=1)
                                    {
                                        now[i][j].value=true;
                                        if(i >= cachesize / 2 && i < Bamount + cachesize / 2 && j >= cachesize / 2 && j < Bamount + cachesize / 2 )
                                        {
                                            buttongroup[i-cachesize/2][j-cachesize/2].setBackground(nowcolor);
                                        }
                                    }
                                    j+=1;
                                    if(j==Bamount+cachesize)
                                    {
                                        j=0;
                                        i+=1;
                                    }

                                }
                                break;
                            }

                        }
                        reader.close();
                        JOptionPane.showMessageDialog(null,"导入成功:","SUCCESS",JOptionPane.INFORMATION_MESSAGE);
                    }catch (Exception err)
                    {
                        JOptionPane.showMessageDialog(null,"导入时遇到错误:"+err.toString(),"ERROR",JOptionPane.ERROR_MESSAGE);
                    }


                }
            }
        });
        //导出按钮及其功能
        JButton exportm = new JButton("导出");
        exportm.setBounds(820, 239, 113, 27);
        contentPane.add(exportm);
        exportm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser fc = new JFileChooser(".");
                int returnVal = fc.showSaveDialog(exportm);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try{
                        File file = fc.getSelectedFile();
                        FileWriter out = new FileWriter(file);
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        out.write("#");
                        out.write(df.format(new Date()));
                        out.write("\n");
                        if(Bsize==5)
                        {
                            out.write("05");
                        }
                        else
                        {
                            out.write(String.valueOf(Bsize));
                        }
                        if(nowcolor==Color.RED)
                        {
                            out.write("RED");
                        }else if(nowcolor==Color.BLACK)
                        {
                            out.write("BLACK");
                        }else if(nowcolor==Color.BLUE)
                        {
                            out.write("BLUE");
                        }else if(nowcolor==Color.YELLOW)
                        {
                            out.write("YELLOW");
                        }
                        if(cachesize<10)
                            out.write("0");
                        out.write(String.valueOf(cachesize));
                        out.write("\n");
                        out.write(nowrule);
                        out.write("\n");
                        int flag=8;
                        int sum=0;
                        for(int i=0;i<Bamount+cachesize;i++)
                            for(int j=0;j<Bamount+cachesize;j++)
                            {
                                sum+=flag*(now[i][j].value?1:0);
                                if(flag==1)
                                {
                                    switch (sum)
                                    {
                                        case 15:
                                            out.write("F");break;
                                        case 14:
                                            out.write("E");break;
                                        case 13:
                                            out.write("D");break;
                                        case 12:
                                            out.write("C");break;
                                        case 11:
                                            out.write("B");break;
                                        case 10:
                                            out.write("A");break;
                                        case 9:
                                        case 8:
                                        case 7:
                                        case 6:
                                        case 5:
                                        case 4:
                                        case 3:
                                        case 2:
                                        case 1:
                                        case 0:
                                            out.write(String.valueOf(sum));
                                    }
                                    sum=0;
                                    flag=8;
                                }else
                                    flag/=2;
                            }
                        if(sum!=0)
                        {
                            switch (sum)
                            {
                                case 15:
                                    out.write("F");break;
                                case 14:
                                    out.write("E");break;
                                case 13:
                                    out.write("D");break;
                                case 12:
                                    out.write("C");break;
                                case 11:
                                    out.write("B");break;
                                case 10:
                                    out.write("A");break;
                                case 9:
                                case 8:
                                case 7:
                                case 6:
                                case 5:
                                case 4:
                                case 3:
                                case 2:
                                case 1:
                                case 0:
                                    out.write(String.valueOf(sum));
                            }
                        }
                        out.close();
                        JOptionPane.showMessageDialog(null,"导出成功！","SUCCESS",JOptionPane.INFORMATION_MESSAGE);
                    }catch (Exception err)
                    {
                        JOptionPane.showMessageDialog(null,"导出时遇到错误:"+err.toString(),"ERROR",JOptionPane.ERROR_MESSAGE);
                    }

                }


            }
        });
        //单步推进
        JButton donext = new JButton("\u4E0B\u4E00\u4EE3");
        donext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (running)
                    return;
                for (int i = 0; i < Bamount + cachesize; i++)
                {
                    for (int j = 0; j < Bamount + cachesize; j++) {
                        // 3*3
                        int amount = 0;
                        for (int p = Math.max(0, i - 1); p <= Math.min(i + 1, Bamount + cachesize - 1); p++)
                            for (int q = Math.max(0, j - 1); q <= Math.min(j + 1, Bamount + cachesize - 1); q++) {
                                if (p == i && q == j)
                                    continue;
                                if (now[p][q].value)
                                    amount++;
                            }
                        next[i][j] = now[i][j].value?RuleS.contains(amount):RuleB.contains(amount);
                    }
                }
                for (int i = 0; i < Bamount + cachesize; i++)
                    for (int j = 0; j < Bamount + cachesize; j++) {
                        if (i >= cachesize / 2 && i < Bamount + cachesize / 2 && j >= cachesize / 2 && j < Bamount + cachesize / 2 && now[i][j].value != next[i][j]) {
                            if (next[i][j])
                                buttongroup[i - cachesize / 2][j - cachesize / 2].setBackground(nowcolor);
                            else
                                buttongroup[i - cachesize / 2][j - cachesize / 2].setBackground(Color.white);
                        }
                        now[i][j].value = next[i][j];
                    }
            }
        });
        donext.setBounds(820, 79, 113, 27);
        contentPane.add(donext);

        JFormattedTextField tjsd = new JFormattedTextField(NumberFormat.getIntegerInstance());
        tjsd.setBounds(852, 682, 85, 24);
        contentPane.add(tjsd);
        tjsd.setValue(changespeed);

        JLabel label_tjsd = new JLabel("\u63A8\u8FDB\u901F\u5EA6:");
        label_tjsd.setBounds(782, 682, 72, 18);
        contentPane.add(label_tjsd);

        JLabel label_fgdx = new JLabel("\u65B9\u683C\u5927\u5C0F:");
        label_fgdx.setBounds(782, 602, 68, 18);
        contentPane.add(label_fgdx);

        fgdx = new JComboBox();
        fgdx.setBounds(852, 600, 85, 24);
        contentPane.add(fgdx);
        fgdx.addItem("5px");
        fgdx.addItem("10px");
        fgdx.addItem("15px");
        fgdx.addItem("25px");
        fgdx.setSelectedIndex(1);

        JLabel label_2 = new JLabel("\u6BEB\u79D2");
        label_2.setBounds(940, 682, 30, 18);
        contentPane.add(label_2);

        JLabel label_tkys = new JLabel("\u56FE\u5757\u989C\u8272:");
        label_tkys.setBounds(782, 642, 68, 18);
        contentPane.add(label_tkys);

        tkys = new JComboBox();
        tkys.setBounds(852, 640, 85, 27);
        contentPane.add(tkys);
        tkys.addItem("红色");
        tkys.addItem("黄色");
        tkys.addItem("蓝色");
        tkys.addItem("黑色");
        tkys.setSelectedIndex(0);

        JLabel label_jsgz = new JLabel("\u8BA1\u7B97\u89C4\u5219:");
        label_jsgz.setBounds(782, 562, 68, 18);
        contentPane.add(label_jsgz);

        jsgz = new JComboBox();
        jsgz.setBounds(852, 560, 85, 24);
        contentPane.add(jsgz);
        jsgz.addItem("B3/S23");
        jsgz.addItem("B2/S7");
        jsgz.addItem("B36/S23");
        jsgz.addItem("B3678/S34678");
        jsgz.addItem("B35678/S5678");
        jsgz.setSelectedIndex(0);


        JLabel label_ysmx = new JLabel("\u9884\u8BBE\u68CB\u578B:");
        label_ysmx.setBounds(782, 525, 68, 18);
        contentPane.add(label_ysmx);

        ysmx = new JComboBox();
        ysmx.setBounds(852, 520, 85, 24);
        contentPane.add(ysmx);
        ysmx.addItem("无");
        for (int i = 0; i < sources.amount; i++) {
            ysmx.addItem(sources.list[i].name);
        }
        ysmx.setSelectedIndex(0);
        ysmx.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jsgz.removeAllItems();
                fgdx.removeAllItems();
                String str = (String) ((JComboBox) e.getSource()).getSelectedItem();
                if (str.equals("无")) {
                    jsgz.addItem("B3/S23");
                    jsgz.addItem("B2/S7");
                    jsgz.addItem("B36/S23");
                    jsgz.addItem("B3678/S34678");
                    jsgz.addItem("B35678/S5678");
                    jsgz.setSelectedItem(nowrule);
                    fgdx.addItem("5px");
                    fgdx.addItem("10px");
                    fgdx.addItem("15px");
                    fgdx.addItem("25px");
                    fgdx.setSelectedItem(String.valueOf(Bsize) + "px");
                    return;
                }
                for (int i = 0; i < sources.amount; i++) {
                    if (sources.list[i].name.equals(str)) {
                        jsgz.addItem(sources.list[i].rule);
                        for (String px : sources.list[0].suitpx.keySet()) {
                            fgdx.addItem(px);
                        }
                        return;
                    }
                }
            }
        });

        //应用设置
        JButton apply = new JButton("\u5E94\u7528\u8BBE\u7F6E");
        apply.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (((Number) tjsd.getValue()).intValue() < 50 || ((Number) tjsd.getValue()).intValue() > 10000)//检查参数有效性
                {
                    JOptionPane.showMessageDialog(null, "推进速度必须在50ms-10000ms之间", "异常的推进速度", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (Integer.parseInt(((String) fgdx.getSelectedItem()).substring(0, ((String) fgdx.getSelectedItem()).length() - 2)) != Bsize)//px
                {
                    System.out.println("Repaint");
                    JOptionPane.showMessageDialog(null, "界面尺寸变化，请等待", "提示", JOptionPane.INFORMATION_MESSAGE);
                    changespeed = ((Number) tjsd.getValue()).intValue();
                    Bsize = Integer.parseInt(((String) fgdx.getSelectedItem()).substring(0, ((String) fgdx.getSelectedItem()).length() - 2));
                    nowcolor = colormap.get(tkys.getSelectedItem());
                    if (!nowrule.equals((String) jsgz.getSelectedItem())) {
                        nowrule = (String) jsgz.getSelectedItem();
                        String[] temp = nowrule.split("/");
                        RuleB = new HashSet<Integer>();
                        RuleS = new HashSet<Integer>();
                        for (int i = 1; i < temp[0].length(); i++) {
                            RuleB.add(Integer.valueOf(temp[0].substring(i, i + 1)));
                        }
                        for (int i = 1; i < temp[1].length(); i++) {
                            RuleS.add(Integer.valueOf(temp[1].substring(i, i + 1)));
                        }
                    }
                    if (running) {
                        timer.cancel();
                        running = false;
                        start.setText("开始推进");
                    }
                    panel.removeAll();
                    init_panel_object(panel);
                    panel.repaint();
                    JOptionPane.showMessageDialog(null, "完成", "提示", JOptionPane.INFORMATION_MESSAGE);
                } else if (colormap.get(tkys.getSelectedItem()) != nowcolor) {//颜色
                    if (running)
                        timer.cancel();
                    nowcolor = colormap.get(tkys.getSelectedItem());
                    for (int i = 0; i < Bamount; i++)
                        for (int j = 0; j < Bamount; j++) {
                            if (buttongroup[i][j].status.value) {
                                buttongroup[i][j].setBackground(nowcolor);
                            }
                        }
                    changespeed = ((Number) tjsd.getValue()).intValue();
                    if (!nowrule.equals((String) jsgz.getSelectedItem())) {
                        nowrule = (String) jsgz.getSelectedItem();
                        String[] temp = nowrule.split("/");
                        RuleB = new HashSet<Integer>();
                        RuleS = new HashSet<Integer>();
                        for (int i = 1; i < temp[0].length(); i++) {
                            RuleB.add(Integer.valueOf(temp[0].substring(i, i + 1)));
                        }
                        for (int i = 1; i < temp[1].length(); i++) {
                            RuleS.add(Integer.valueOf(temp[1].substring(i, i + 1)));
                        }
                    }
                    if (running) {
                        starttimer();
                    }
                } else if (!((String) jsgz.getSelectedItem()).equals(nowrule))//规则
                {
                    if (running)
                        timer.cancel();
                    changespeed = ((Number) tjsd.getValue()).intValue();
                    nowrule = (String) jsgz.getSelectedItem();
                    String[] temp = nowrule.split("/");
                    RuleB = new HashSet<Integer>();
                    RuleS = new HashSet<Integer>();
                    for (int i = 1; i < temp[0].length(); i++) {
                        RuleB.add(Integer.valueOf(temp[0].substring(i, i + 1)));
                        //System.out.println("B"+temp[0].substring(i, i + 1));
                    }
                    for (int i = 1; i < temp[1].length(); i++) {
                        RuleS.add(Integer.valueOf(temp[1].substring(i, i + 1)));
                        //System.out.println("S"+temp[1].substring(i, i + 1));
                    }
                    if (running) {
                        starttimer();
                    }
                } else if (((Number) tjsd.getValue()).intValue() != changespeed) {
                    changespeed = ((Number) tjsd.getValue()).intValue();
                    if (running) {
                        timer.cancel();
                        starttimer();
                    }
                }
                if (!((String) ysmx.getSelectedItem()).equals("无")) {
                    if (running) {
                        timer.cancel();
                        running = false;
                        start.setText("开始推进");
                    }
                    String tstr = (String) ysmx.getSelectedItem();
                    for (int i = 0; i < sources.amount; i++) {
                        if (tstr.equals(sources.list[i].name)) {
                            for (int j = 0; j < Bamount + cachesize; j++) {
                                for (int k = 0; k < Bamount + cachesize; k++) {
                                    if (now[j][k].value) {
                                        if (j >= cachesize / 2 && j < Bamount + cachesize / 2 && k >= cachesize / 2 && k < Bamount + cachesize / 2)
                                            buttongroup[j - cachesize / 2][k - cachesize / 2].setBackground(Color.white);
                                        now[j][k].value = false;
                                    }
                                }
                            }
                            Point p = sources.list[i].suitpx.get(String.valueOf(Bsize) + "px");
                            for (int j = 0; j < sources.list[i].data.length; j++) {
                                for (int k = 0; k < sources.list[i].data[j].length(); k++) {
                                    if (sources.list[i].data[j].charAt(k) == '1') {

                                        buttongroup[p.i + j][p.j + k].setBackground(nowcolor);
                                        now[p.i + j + cachesize / 2][p.j + k + cachesize / 2].value = true;
                                    }
                                }
                            }
                            break;
                        }

                    }
                }
            }
        });
        apply.setBounds(820, 716, 113, 27);
        contentPane.add(apply);
    }
}
