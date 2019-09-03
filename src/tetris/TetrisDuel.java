package tetris;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.JLabel;

import javax.swing.JPanel;




/**
 * 双人模式下的界面
 *
 */
public class TetrisDuel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Self my;
	private Opponent oppo;
	
	private static Image background;//背景图片
	
	private GameWindow mainWindow;

	private GameWindow mainWindow2;

	static{
		try{
			background = ImageIO.read(TetrisDuel.class.getResource("tetrisDuel.png"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setLocalController(Self self){
		this.my=self;
	}
	
	public void setRemoteController(Opponent opponent){
		this.oppo=opponent;
	}
	
	
	public TetrisDuel() {
		setLayout(null);
				
		mainWindow = new GameWindow(13, 5, 270, 525);
		mainWindow.setBounds(10, 10, 265, 530);
		add(mainWindow);		

		mainWindow2 = new GameWindow(782, 5, 270, 525);
		mainWindow2.setBounds(780, 10, 265, 530);
		add(mainWindow2);
				
		mainWindow.setFocusable(false);
	
	}
	
	public static final int FONT_COLOR = 0x667799;
	public static final int FONT_SIZE = 0x20;	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, null);
		mainWindow.creatwindow(g);
		mainWindow2.creatwindow(g);
		try{
			Font f = getFont();//获取当前的 面板默认字体
			Font font = new Font(f.getName(), Font.BOLD, FONT_SIZE);
			int x = 290+15;
			int y = 162+15;
			g.setColor(new Color(FONT_COLOR));
			g.setFont(font);
			String str = "SCORE:"+my.getTetris().score;
			g.drawString(str, x, y);
			y+=56;
			str = "level:"+my.getTetris().level;
			g.drawString(str, x, y);
			y+=56;
			str = "[P]Pause";
			if(my.getTetris().pause){str = "[C]Continue";}
			g.drawString(str, x, y);
			
			
			int musicx=290+15+250;
			int musicy=162+15+56+56;
			String str1="[M]Music";
			g.drawString(str1, musicx, musicy);
			
			int x2 = 290+15+250;
			int y2 = 162+15;
			String str2 = "SCORE:"+oppo.getTetris().score;
			g.drawString(str2, x2, y2);
			y2+=56;
			str2 = "level:"+oppo.getTetris().level;
			g.drawString(str2, x2, y2);
					
			// 在这里控制下落跟下一个形状的位置
		
			my.getCurTetromino().draw(g, 15, 41);
			my.getNextTetromino().draw(g, 260, 65);			
			my.getTetris().paintWallOnline(g,0);
			
			// 在这里控制下落跟下一个形状的位置
		
			oppo.getCurTetromino().draw(g, 778, 41);
			oppo.getNextTetromino().draw(g, 520, 65);
			oppo.getTetris().paintWallOnline(g,1);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

 class GameWindow extends JLabel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int x;
	public int y;
	public int h;
	public int w;
	
	public GameWindow(int x, int y, int w, int h){
		this.x=x-5;
		this.y=y;
		this.w=w;
		this.h=h;
	}
	public void creatwindow(Graphics g){
	}
}
 
 