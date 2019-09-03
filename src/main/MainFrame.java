package main;


import java.awt.Color;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import tetris.Opponent;
import tetris.Self;
import tetris.Tetris;
import tetris.TetrisDuel;
import controller.KeyController;
import controller.MusicController;
import socket.Client;
import socket.Server;



public class MainFrame extends JFrame {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MusicController.bgmPlay();
		
	
		this.setTitle("俄罗斯方块");
		this.setSize(500,400);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(new LauncherJPanel(this));
	}	
	
	public void chooseMode(int mode){
		System.out.println("mode"+mode);
		switch (mode){
		case 0:			
				JFrame frame = new JFrame();				
				Tetris tetris = new Tetris();
				frame.add(tetris);
				frame.setSize(525, 590);
				frame.setUndecorated(false);//true去掉窗口框！
				frame.setTitle("俄罗斯方块单机版");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLocationRelativeTo(null);//使当前窗口居中
				frame.setVisible(true);
				tetris.action();
			break;
	
		case 1:
			String port=JOptionPane.showInputDialog("请输入房间号:");
			Server.Init(Integer.parseInt(port));
			System.out.println("连接成功");
			
			TetrisDuel terisduel=new TetrisDuel();
			Self.self=new Self(Server.getExchangeThread(),terisduel);
			Opponent.opponent=new Opponent(terisduel);
			terisduel.setLocalController(Self.self);
			terisduel.setRemoteController(Opponent.opponent);
			this.setContentPane(terisduel);
			this.addKeyListener(new KeyController(Self.self));
			// 必须刷新一下界面才可以,
			this.setTitle("俄罗斯方块联网对战版");
			this.setSize(1070,590);
			Self.self.gameStart();
			break;
		case 2:
			String port2=JOptionPane.showInputDialog("请输入房间号:");
			Client.Init(Integer.parseInt(port2));
			System.out.println("连接成功");

			TetrisDuel terisduel2=new TetrisDuel();
			Self.self=new Self(Client.getExchangeThread(),terisduel2);
			Opponent.opponent=new Opponent(terisduel2);
			terisduel2.setLocalController(Self.self);
			terisduel2.setRemoteController(Opponent.opponent);
			this.setContentPane(terisduel2);
			this.addKeyListener(new KeyController(Self.self));
			this.setTitle("俄罗斯方块联网对战版");
			this.setSize(1070,590);
			Self.self.gameStart();
			break;
		}
		requestFocus();
	}

}

class LauncherJPanel extends JPanel {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LauncherJPanel(MainFrame mainFrame) {
		setLayout(null);
	
		MyButton btnOffline = new MyButton("单人模式",123,50);	
		btnOffline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainFrame.chooseMode(0);
			}
		});
		
		btnOffline.setBounds(340, 231, 123, 28); //位置，大小
		add(btnOffline);
		
		MyButton btnMode1 = new MyButton("创建房间",123,50);
		btnMode1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainFrame.chooseMode(1);
			}
		});
		btnMode1.setBounds(340, 269, 123, 28);
		add(btnMode1);
		
		MyButton btnMode2 = new MyButton("进入房间",123,50);
		btnMode2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.chooseMode(2);
			}
		});
		btnMode2.setBounds(340, 307, 123, 33);
		add(btnMode2);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);		
		Image background = new ImageIcon("Graphics/background/background2.png").getImage();
		g.drawImage(background, 0, 0, null);
		
	}

}

class MyButton extends JButton{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int width,height;
	
	public MyButton(String text,int width,int height){
		this.width=width;
		this.height=height;
                
        setFocusPainted(false);  // 不绘制焦点
        
        setContentAreaFilled(false);// 不绘制内容区

        setText(text);
        setHorizontalTextPosition(CENTER);
        setVerticalTextPosition(CENTER);
        setFont(new Font("黑体", Font.BOLD, 16));
        setForeground(Color.WHITE);
        
        // 设置焦点控制
        setFocusable(true);

        // 设置按钮边框与边框内容之间的像素数
        setMargin(new Insets(0, 0, 0, 0));
	}

}

