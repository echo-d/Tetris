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
		
	
		this.setTitle("����˹����");
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
				frame.setUndecorated(false);//trueȥ�����ڿ�
				frame.setTitle("����˹���鵥����");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLocationRelativeTo(null);//ʹ��ǰ���ھ���
				frame.setVisible(true);
				tetris.action();
			break;
	
		case 1:
			String port=JOptionPane.showInputDialog("�����뷿���:");
			Server.Init(Integer.parseInt(port));
			System.out.println("���ӳɹ�");
			
			TetrisDuel terisduel=new TetrisDuel();
			Self.self=new Self(Server.getExchangeThread(),terisduel);
			Opponent.opponent=new Opponent(terisduel);
			terisduel.setLocalController(Self.self);
			terisduel.setRemoteController(Opponent.opponent);
			this.setContentPane(terisduel);
			this.addKeyListener(new KeyController(Self.self));
			// ����ˢ��һ�½���ſ���,
			this.setTitle("����˹����������ս��");
			this.setSize(1070,590);
			Self.self.gameStart();
			break;
		case 2:
			String port2=JOptionPane.showInputDialog("�����뷿���:");
			Client.Init(Integer.parseInt(port2));
			System.out.println("���ӳɹ�");

			TetrisDuel terisduel2=new TetrisDuel();
			Self.self=new Self(Client.getExchangeThread(),terisduel2);
			Opponent.opponent=new Opponent(terisduel2);
			terisduel2.setLocalController(Self.self);
			terisduel2.setRemoteController(Opponent.opponent);
			this.setContentPane(terisduel2);
			this.addKeyListener(new KeyController(Self.self));
			this.setTitle("����˹����������ս��");
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
	
		MyButton btnOffline = new MyButton("����ģʽ",123,50);	
		btnOffline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainFrame.chooseMode(0);
			}
		});
		
		btnOffline.setBounds(340, 231, 123, 28); //λ�ã���С
		add(btnOffline);
		
		MyButton btnMode1 = new MyButton("��������",123,50);
		btnMode1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainFrame.chooseMode(1);
			}
		});
		btnMode1.setBounds(340, 269, 123, 28);
		add(btnMode1);
		
		MyButton btnMode2 = new MyButton("���뷿��",123,50);
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
                
        setFocusPainted(false);  // �����ƽ���
        
        setContentAreaFilled(false);// ������������

        setText(text);
        setHorizontalTextPosition(CENTER);
        setVerticalTextPosition(CENTER);
        setFont(new Font("����", Font.BOLD, 16));
        setForeground(Color.WHITE);
        
        // ���ý������
        setFocusable(true);

        // ���ð�ť�߿���߿�����֮���������
        setMargin(new Insets(0, 0, 0, 0));
	}

}

