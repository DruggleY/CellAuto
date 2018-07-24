package main;

import main.presource.Sources;

import java.awt.Choice;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.desktop.SystemSleepEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;
import java.util.*;
import java.util.Timer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import main.presource.Source;

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
	//界面绘制
	private static JPanel contentPane;

	//功能实现
	private static NButton[][] buttongroup;
	private static Bool[][] now;
	private static boolean[][] next;
	private Timer timer;

	//重要参数
	private static int Bamount;//一行方格数
	private static int Bsize = 10;//方格大小
	private static int cachesize = 20;//边界缓冲
	private static int changespeed = 500;//推进速度
	private static Color nowcolor = Color.RED;//当前颜色
	private static final Map<String, Color> colormap;//颜色对照表
	static
	{
		colormap = new HashMap<String, Color>();
		colormap.put("红色", Color.RED);
		colormap.put("黄色", Color.YELLOW);
		colormap.put("蓝色", Color.BLUE);
		colormap.put("黑色",Color.BLACK);
	}
	private static String nowrule="B3/S23";
	private static Set<Integer> RuleB;
	private static Set<Integer> RuleS;
	private static Sources sources= null;


	//状态判断
	private static boolean running = false;
	private static boolean isdown1=false;
	private static boolean isdown2=false;


	//初始化主界面
	private static void init_panel_object(JPanel panel)
	{
		Bamount = (int)750/Bsize;
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
				buttongroup[i][j].setBounds(j * Bsize+ 10, i * Bsize + 10, Bsize, Bsize);
				buttongroup[i][j].setBackground(Color.white);
				buttongroup[i][j].status = now[i + cachesize/2][j + cachesize/2];
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
						isdown1=false;
					}
				});
				buttongroup[i][j].addMouseListener(new MouseListener() {

					@Override
					public void mouseClicked(MouseEvent e) {
						// TODO Auto-generated method stub
						if(e.getButton()==MouseEvent.BUTTON1)
							isdown1=false;
						else if(e.getButton()==MouseEvent.BUTTON3)
							isdown2=false;
					}

					@Override
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub
						if((isdown1||isdown2)&&running==false)
						{
							NButton target = (NButton) e.getSource();
							if (isdown1)
							{
								target.setBackground(nowcolor);
								target.status.value = true;
							}
							else
							{
								target.setBackground(Color.white);
								target.status.value = false;
							}
						}
					}

					@Override
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mousePressed(MouseEvent e) {
						// TODO Auto-generated method stub
						if(e.getButton()==MouseEvent.BUTTON1)
							isdown1=true;
						else if(e.getButton()==MouseEvent.BUTTON3)
							isdown2=true;
							
					}

					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub
						if(e.getButton()==MouseEvent.BUTTON1)
							isdown1=false;
						else if(e.getButton()==MouseEvent.BUTTON3)
							isdown2=false;
					}
					
				});
				panel.add(buttongroup[i][j]);
			}
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Cells frame = new Cells();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
			}
			}
		});
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
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JPanel panel = new JPanel();
		panel.addMouseListener(new MouseListener() {

			public void mousePressed(MouseEvent e)
			{
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				isdown1=false;
				isdown2=false;
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				if(arg0.getButton()==MouseEvent.BUTTON1)
					isdown1=false;
				else if(arg0.getButton()==MouseEvent.BUTTON3)
					isdown2=false;
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
		JButton start = new JButton("\u5F00\u59CB\u63A8\u8FDB");
		start.setBounds(820, 119, 113, 27);
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (running) {
					timer.cancel();
					running = false;
					((JButton)arg0.getSource()).setText("开始推进");
				} else {
					timer = new Timer();
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
							for (int i = 0; i < Bamount + cachesize; i++)
								for (int j = 0; j < Bamount + cachesize; j++) {
									// 3*3
									int amount = 0;
									for (int p = Math.max(0, i - 1); p <= Math.min(i + 1, Bamount + cachesize-1); p++)
										for (int q = Math.max(0, j - 1); q <= Math.min(j + 1, Bamount + cachesize -1); q++) {
											if (p == i && q == j)
												continue;
											if (now[p][q].value)
											{
												amount++;
											}
										}
									if (now[i][j].value) {
										if (RuleS.contains(amount))
											next[i][j] = true;
										else
											next[i][j] = false;
									} else {
										if (RuleB.contains(amount))
											next[i][j] = true;
										else
											next[i][j] = false;
									}
								}
							for (int i = 0; i < Bamount+ cachesize; i++)
								for (int j = 0; j < Bamount + cachesize; j++) {
									if (i>=cachesize/2&&i<Bamount+cachesize/2&&j>=cachesize/2&&j<Bamount+cachesize/2&&now[i][j].value != next[i][j]) {
										if (next[i][j])
											buttongroup[i - cachesize/2][j - cachesize/2].setBackground(nowcolor);
										else
											buttongroup[i - cachesize/2][j - cachesize/2].setBackground(Color.white);
									}
									now[i][j].value = next[i][j];
								}
						}

					}, 0, changespeed);
					running = true;
					((JButton)arg0.getSource()).setText("停止推进");
				}

			}
		});
		contentPane.add(start);

		
		JButton clear = new JButton("\u6E05\u7A7A\u9875\u9762");
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(running)
				{
					timer.cancel();
					running = false;
					start.setText("开始推进");
				}
				for(int i=0;i<Bamount+cachesize;i++)
				{
					for(int j=0;j<Bamount+cachesize;j++)
					{
						now[i][j].value=false;
					}
				}
				for(int i=0;i<Bamount;i++)
					for(int j=0;j<Bamount;j++)
						buttongroup[i][j].setBackground(Color.white);
						
			}
		});
		clear.setBounds(820, 159, 113, 27);
		contentPane.add(clear);
		
		JButton donext = new JButton("\u4E0B\u4E00\u4EE3");
		donext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(running)
					return;
				for (int i = 0; i < Bamount + cachesize; i++)
					for (int j = 0; j < Bamount + cachesize; j++) {
						// 3*3
						int amount = 0;
						for (int p = Math.max(0, i - 1); p <= Math.min(i + 1, Bamount + cachesize-1); p++)
							for (int q = Math.max(0, j - 1); q <= Math.min(j + 1, Bamount + cachesize -1); q++) {
								if (p == i && q == j)
									continue;
								if (now[p][q].value)
									amount++;
							}
						if (now[i][j].value) {
							if (RuleS.contains(amount))
								next[i][j] = true;
							else
								next[i][j] = false;
						} else {
							if (RuleB.contains(amount))
								next[i][j] = true;
							else
								next[i][j] = false;
						}

					}
				for (int i = 0; i < Bamount+ cachesize; i++)
					for (int j = 0; j < Bamount + cachesize; j++) {
						if (i>=cachesize/2&&i<Bamount+cachesize/2&&j>=cachesize/2&&j<Bamount+cachesize/2&&now[i][j].value != next[i][j]) {
							if (next[i][j])
								buttongroup[i - cachesize/2][j - cachesize/2].setBackground(nowcolor);
							else
								buttongroup[i - cachesize/2][j - cachesize/2].setBackground(Color.white);
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

        JComboBox fgdx = new JComboBox();
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

		JComboBox tkys = new JComboBox();
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

		JComboBox jsgz = new JComboBox();
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

		JComboBox ysmx = new JComboBox();
		ysmx.setBounds(852, 520, 85, 24);
		contentPane.add(ysmx);
		ysmx.addItem("无");
		for(int i=0;i<sources.amount;i++)
		{
			ysmx.addItem(sources.list[i].name);
		}
		ysmx.setSelectedIndex(0);
		ysmx.addActionListener (new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				jsgz.removeAllItems();
				fgdx.removeAllItems();
				String str = (String)((JComboBox)e.getSource()).getSelectedItem();
				if(str=="无")
				{
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
					fgdx.setSelectedItem(String.valueOf(Bsize)+"px");
					return;
				}
				for(int i=0;i<sources.amount;i++)
				{
					if(sources.list[i].name==str)
					{
						jsgz.addItem(sources.list[i].rule);
						for(String px:sources.list[0].suitpx.keySet())
						{
							fgdx.addItem(px);
						}
						return;
					}
				}
			}
		});
		
		
		JButton apply = new JButton("\u5E94\u7528\u8BBE\u7F6E");
		apply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(((Number)tjsd.getValue()).intValue()<50||((Number)tjsd.getValue()).intValue()>10000)//检查参数有效性
				{
					JOptionPane.showMessageDialog(null, "推进速度必须在50ms-10000ms之间", "异常的推进速度",  JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(Integer.parseInt(((String)fgdx.getSelectedItem()).substring(0, ((String)fgdx.getSelectedItem()).length()-2))!=Bsize)//px
				{
					System.out.println("Repaint");
					System.out.println(String.valueOf(Bsize));
					System.out.println(((String)fgdx.getSelectedItem()).substring(0, ((String)fgdx.getSelectedItem()).length()-2)!=String.valueOf(Bsize));
					Bsize = Integer.parseInt(((String)fgdx.getSelectedItem()).substring(0, ((String)fgdx.getSelectedItem()).length()-2));
					nowcolor = colormap.get(tkys.getSelectedItem());
					if(running)
					{
						timer.cancel();
						running = false;
						start.setText("开始推进");
					}
					panel.removeAll();
					init_panel_object(panel);
					panel.repaint();
				}else if(colormap.get(tkys.getSelectedItem())!=nowcolor) {//颜色
					if (running)
						timer.cancel();
					nowcolor = colormap.get(tkys.getSelectedItem());
					for (int i = 0; i < Bamount; i++)
						for (int j = 0; j < Bamount; j++) {
							if (buttongroup[i][j].status.value) {
								buttongroup[i][j].setBackground(nowcolor);
							}
						}
					if(nowrule!=(String)jsgz.getSelectedItem())
					{
						nowrule = (String)jsgz.getSelectedItem();
						String[] temp = nowrule.split("/");
						RuleB = new HashSet<Integer>();
						for(int i=1;i<temp[0].length();i++)
						{
							RuleB.add(Integer.valueOf(temp[0].substring(i,i+1)));
						}
						for(int i=1;i<temp[1].length();i++)
						{
							RuleS.add(Integer.valueOf(temp[1].substring(i,i+1)));
						}
					}
					changespeed = ((Number) tjsd.getValue()).intValue();
					if (nowrule!=(String)jsgz.getSelectedItem()) {
						nowrule = (String) jsgz.getSelectedItem();
						String[] temp = nowrule.split("/");
						RuleB = new HashSet<Integer>();
						for (int i = 1; i < temp[0].length(); i++) {
							RuleB.add(Integer.valueOf(temp[0].substring(i, i + 1)));
						}
						for (int i = 1; i < temp[1].length(); i++) {
							RuleS.add(Integer.valueOf(temp[1].substring(i, i + 1)));
						}
					}
					if (running) {
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
												if (now[p][q].value)
													amount++;
											}
										if (now[i][j].value) {
											if (RuleS.contains(amount))
												next[i][j] = true;
											else
												next[i][j] = false;
										} else {
											if (RuleB.contains(amount))
												next[i][j] = true;
											else
												next[i][j] = false;
										}

									}
								for (int i = 0; i < Bamount+ cachesize; i++)
									for (int j = 0; j < Bamount + cachesize; j++) {
										if (i>=cachesize/2&&i<Bamount+cachesize/2&&j>=cachesize/2&&j<Bamount+cachesize/2&&now[i][j].value != next[i][j]) {
											if (next[i][j])
												buttongroup[i - cachesize/2][j - cachesize/2].setBackground(nowcolor);
											else
												buttongroup[i - cachesize/2][j - cachesize/2].setBackground(Color.white);
										}
										now[i][j].value = next[i][j];
									}
							}

						}, 0, changespeed);
					}
				}else if((String)jsgz.getSelectedItem()!=nowrule)//规则
				{
					if(running)
						timer.cancel();
					changespeed=((Number)tjsd.getValue()).intValue();
					nowrule = (String)jsgz.getSelectedItem();
					String[] temp = nowrule.split("/");
					RuleB = new HashSet<Integer>();
					for(int i=1;i<temp[0].length();i++)
					{
						RuleB.add(Integer.valueOf(temp[0].substring(i,i+1)));
					}
					for(int i=1;i<temp[1].length();i++)
					{
						RuleS.add(Integer.valueOf(temp[1].substring(i,i+1)));
					}
					if(running)
					{
						timer = new Timer();
						timer.schedule(new TimerTask() {
							@Override
							public void run() {
								for (int i = 0; i < Bamount + cachesize; i++)
									for (int j = 0; j < Bamount + cachesize; j++) {
										// 3*3
										int amount = 0;
										for (int p = Math.max(0, i - 1); p <= Math.min(i + 1, Bamount + cachesize-1); p++)
											for (int q = Math.max(0, j - 1); q <= Math.min(j + 1, Bamount + cachesize -1); q++) {
												if (p == i && q == j)
													continue;
												if (now[p][q].value)
													amount++;
											}
										if (now[i][j].value) {
											if (RuleS.contains(amount))
												next[i][j] = true;
											else
												next[i][j] = false;
										} else {
											if (RuleB.contains(amount))
												next[i][j] = true;
											else
												next[i][j] = false;
										}

									}
								for (int i = 0; i < Bamount+ cachesize; i++)
									for (int j = 0; j < Bamount + cachesize; j++) {
										if (i>=cachesize/2&&i<Bamount+cachesize/2&&j>=cachesize/2&&j<Bamount+cachesize/2&&now[i][j].value != next[i][j]) {
											if (next[i][j])
												buttongroup[i - cachesize/2][j - cachesize/2].setBackground(nowcolor);
											else
												buttongroup[i - cachesize/2][j - cachesize/2].setBackground(Color.white);
										}
										now[i][j].value = next[i][j];
									}
							}

						}, 0, changespeed);
					}
				}else if(((Number)tjsd.getValue()).intValue()!=changespeed) {
					changespeed = ((Number) tjsd.getValue()).intValue();
					if (running) {
						timer.cancel();
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
												if (now[p][q].value)
													amount++;
											}
										if (now[i][j].value) {
											if (RuleS.contains(amount))
												next[i][j] = true;
											else
												next[i][j] = false;
										} else {
											if (RuleB.contains(amount))
												next[i][j] = true;
											else
												next[i][j] = false;
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

						}, 0, changespeed);
					}
				}
			}
		});
		apply.setBounds(820, 716, 113, 27);
		contentPane.add(apply);
	}
}
