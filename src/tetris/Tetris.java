//界面
package tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.MusicController;


/**
 * 俄罗斯方块单机 
 *
 */


public class Tetris extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Tetromino tetromino;

	private Tetromino nextOne;
	/** 行数 */
	public static final int ROWS = 20;
	/** 列数 */
	public static final int COLS = 10;
	/** 墙   二维数组 */
	private Cell[][] wall = new Cell[ROWS][COLS]; 
	/** 消掉的行数 */
//	public int lines = 0;
	/** 分数 */
	public int score = 0;
	
	public static final int CELL_SIZE = 26;
	
	private static Image background;//背景图片
	
	static{
		try{
			background = ImageIO.read(Tetris.class.getResource("tetris.png"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void action(){
		startAction();
		repaint();  //重复调用paint()
		KeyAdapter l = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(MusicController.isturnOn())
					MusicController.actionPlay();
				
				int key = e.getKeyCode();
				if(key == KeyEvent.VK_Q){
					System.exit(0);//退出当前的Java进程
				}
				if(gameOver){
					if(key==KeyEvent.VK_S){
						MusicController.setturnOn(true);
						MusicController.bgmPlay();
						startAction();
					}
					return;
				}
				//如果暂停并且按键是[C]就继续动作
				if(pause){//pause = false
					if(key==KeyEvent.VK_C){	
						continueAction();	
						if(!MusicController.isRunning()){
							MusicController.bgmPlay();
						}	
					}
					return;
				}
				
				if(e.getKeyCode()==KeyEvent.VK_M) {
					if(MusicController.isturnOn()){
						MusicController.setturnOn(false);
						MusicController.bgmStop();
					}else{
						MusicController.setturnOn(true);
						MusicController.bgmPlay();
					}
					return;
				}
				//否则处理其它按键
				switch(key){
				case KeyEvent.VK_RIGHT:
					moveRightAction(); 
					break;
				case KeyEvent.VK_LEFT:
					moveLeftAction(); 
					break;
				case KeyEvent.VK_DOWN:
					softDropAction() ; 
					break;
				case KeyEvent.VK_UP:
					rotateRightAction() ; 
					break;
				case KeyEvent.VK_Z:
					rotateLeftAction() ; 
					break;
				case KeyEvent.VK_SPACE:
					hardDropAction() ; 
					break;
				case KeyEvent.VK_P:
					pauseAction();
					if(MusicController.isRunning()){
						MusicController.bgmStop();
					}
					break;
				}
				repaint();
			}
		};
		this.requestFocus();
		this.addKeyListener(l);
	}
	
	public void paint(Graphics g){
		g.drawImage(background, 0, 0, null);
		g.translate(15, 15);//把一个图像在一个区域显示，图像的左上角位于（15,15）这点
		paintTetromino(g);//绘制正在下落的方块
		paintWall(g);//画墙
		paintNextOne(g);
		paintScore(g);
	}
	
	private JPanel panel;
	public static final int FONT_COLOR = 0x667799;
	public static final int FONT_SIZE = 0x20;
	private void paintScore(Graphics g) {
		Font f = getFont();//获取当前的 面板默认字体
		Font font = new Font(f.getName(), Font.BOLD, FONT_SIZE);
		int x = 290;
		int y = 162-14;
		g.setColor(new Color(FONT_COLOR));
		g.setFont(font);
		String str = "SCORE:"+this.score;
		g.drawString(str, x, y);
		y+=55;
		str = "level:"+this.level;
		g.drawString(str, x, y);
		y+=55;
		str = "[P]Pause";
		if(pause){str = "[C]Continue";}
		if(gameOver){	
			if(MusicController.isturnOn()){
				MusicController.setturnOn(false);
				MusicController.bgmStop();
			}
			JOptionPane.showMessageDialog(panel, "GAMEOVER.\n"+
			"YOUR SCORE:"+Integer.toString(this.score)+"\nPRESS S TO RESTART!");
			str = "[S]Start!";}
		g.drawString(str, x, y);				   
		y+=55;
		String str1="[M]Music";
		g.drawString(str1, x, y);
	}

	public void paintNextOne(Graphics g) {
		Cell[] cells = nextOne.getCells();
		for(int i=0; i<cells.length; i++){
			int x = (cells[i].getCol()+10) * CELL_SIZE-1;
			int y = (cells[i].getRow()+1) * CELL_SIZE-1;
			g.drawImage(cells[i].getImage(), x, y, null);
		}	
	}
	
	public void paintTetromino(Graphics g) {
		Cell[] cells = tetromino.getCells();
		for(int i=0; i<cells.length; i++){
			int x = cells[i].getCol() * CELL_SIZE-1;
			int y = cells[i].getRow() * CELL_SIZE-1;
			g.drawImage(cells[i].getImage(), x, y, null);
		}		
	}
	
	
	//在 Tetris 类 中添加 方法 paintWall
		public void paintWall(Graphics g){
			//wall.length 二维数组的行数
			for(int row=0; row<wall.length; row++){
				//wall[row].length表示对应行的长度
				for(int col=0; col<wall[row].length; col++){
					int x = col*CELL_SIZE; 
					int y = row*CELL_SIZE;
					if(wall[row][col]==null){
						
					}else{
						g.drawImage(wall[row][col].getImage(), x-1, y-1, null);
					}
				}
			}
		}
	
		public void paintWallOnline(Graphics g,int mode){
			//wall.length 二维数组的行数
			for(int row=0; row<wall.length; row++){
				//wall[row].length表示对应行的长度
				for(int col=0; col<wall[row].length; col++){
					int x = col*CELL_SIZE; 
					int y = row*CELL_SIZE;
					if(wall[row][col]!=null){
						if(mode==1){
							g.drawImage(wall[row][col].getImage(), x+777, y+15, null);
						}else {
							g.drawImage(wall[row][col].getImage(), x+14, y+15, null);
						}
					}
				}
			}
		}
		
	
		/**
		 * 在 Tetris(俄罗斯方块) 类中增加方法
		 * 这个方法的功能是：软下落的动作 控制流程
		 * 完成功能：如果能够下落就下落，否则就着陆到墙上，
		 *   而新的方块出现并开始落下。
		 */
		public void softDropAction(){
			if(tetrominoCanDrop()){
				tetromino.softDrop();
			}else{
				tetrominoLandToWall();//wall[row][col]=cell
				destroyLines();//破坏满的行
				checkGameOver();
				tetromino = nextOne;
				Random r = new Random();
				int type = r.nextInt(7);
				nextOne = Tetromino.randomTetromino(type);
			}
		}
		
		//快速下落
		public void hardDropAction(){
			while(tetrominoCanDrop()){
				tetromino.softDrop();
			}
			tetrominoLandToWall();
			destroyLines();
			checkGameOver();
			tetromino = nextOne;
			Random r = new Random();
			int type = r.nextInt(7);
			nextOne = Tetromino.randomTetromino(type);
		}
		
		public int level=0;
		/**
		 * 是否成功消去一行
		 * @return
		 */
		public boolean ispop(){			
			boolean iscancel=false;
			int lines = 0;
			for(int row = 0; row<wall.length; row++){
				if(fullCells(row)){
					deleteRow(row);
					lines++;
					iscancel=true;
				}
			}					
			if(lines != 0){
				//分数和等级计算
				score +=lines*lines;
				if(score > (2+2*level*level))
					level++;
			}
			return iscancel;
		}
		
		
	
		/** 销毁已经满的行，并且计分
		 * 1）迭代每一行 
		 * 2）如果（检查）某行满是格子了 就销毁这行 
		 **/
		public void destroyLines(){
			
			int lines = 0;
			for(int row = 0; row<wall.length; row++){
				if(fullCells(row)){
					deleteRow(row);
					lines++;
					if(lines != 0){
						//分数和等级计算
						this.score +=lines*lines;
						if(this.score > (2+2*level*level))
							this.level++;
					}
				}
			}
		
		}
		
		
		public boolean fullCells(int row){
			Cell[] line = wall[row];
			for(int i=0; i<line.length; i++){
				if(line[i]==null){//如果有空格式就不是满行
					return false;
				}
			}
			return true;
		}
		
		public void deleteRow(int row){
			if(MusicController.isturnOn())
				MusicController.deletePlay();
			for(int i=row; i>=1; i--){
				//复制 [i-1] -> [i] 
				System.arraycopy(wall[i-1], 0, wall[i], 0, COLS);
			}
			Arrays.fill(wall[0], null);
		}
		
		
		
		/** 检查当前的4格方块能否继续下落 */
		public boolean tetrominoCanDrop(){
			Cell[] cells = tetromino.getCells();
			for(int i = 0; i<cells.length; i++){
				int row = cells[i].getRow(); 
				if(row == ROWS-1){
					return false;
					}//到底就不能下降了
			}
			for(int i = 0; i<cells.length; i++){
				int row = cells[i].getRow(); 
				int col = cells[i].getCol();
				if(wall[row+1][col] != null){
					return false;//下方墙上有方块就不能下降了
				}
			}
			return true;
		}
		
		
		public boolean isleftside(Tetromino tetro){		
			Cell[] cells = tetro.getCells();
			for(int i=0;i<4;i++){
				//左边有墙或者方块都不行
				if(cells[i].getCol()==0|| (wall[cells[i].getRow()][cells[i].getCol()-1]!=null))		
					return true;
			}
			return false;
		}
		
		public boolean isrightside(Tetromino tetro){	
			Cell[] cells = tetro.getCells();
			for(int i=0;i<4;i++){
				//右边有墙或者方块都不行
				if(cells[i].getCol()==9|| (wall[cells[i].getRow()][cells[i].getCol()+1]!=null))
					return true;
			}
			return false;
		}
		
		
		public boolean tetrominoCanDrop(Tetromino tetro){
			boolean isput = false;			
			Cell[] cells = tetro.getCells();
			for(int i = 0; i<cells.length; i++){
				int row = cells[i].getRow(); 
				if(row == ROWS-1){
					Cell[] cells1 = tetro.getCells();
					for(int i1=0; i1<cells1.length; i1++){			
						int row1 = cells1[i1].getRow();
						int col1 = cells1[i1].getCol();
						wall[row1][col1] = cells1[i1];
					   }
					return true;
					}//到底就不能下降了
			      }
			
			for(int i = 0; i<cells.length; i++){
				int row = cells[i].getRow(); 
				int col = cells[i].getCol();
				if(wall[row+1][col] != null){								   			
					isput=true;
					//下方墙上有方块就不能下降了
				}
			}						
			if(isput) {
				Cell[] cells1 = tetro.getCells();
				for(int i1=0; i1<cells1.length; i1++){			
					int row1 = cells1[i1].getRow();
					int col = cells1[i1].getCol();
					wall[row1][col] = cells1[i1];
				   }			
			}
			return isput;
		}
		
		public boolean gameover(){			
			//boolean game=false;
			for(int col=0;col<10;col++){
				if(wall[0][col] != null)
					return true;
			}
			return false;
			
		}
		
		/** 4格方块着陆到墙上 */
		public void tetrominoLandToWall(){
			Cell[] cells = tetromino.getCells();
			for(int i=0; i<cells.length; i++){			
				int row = cells[i].getRow();
				int col = cells[i].getCol();
				wall[row][col] = cells[i];
			}
		}
		
				
		public void moveRightAction(){
			tetromino.moveRight();
			if(outOfBound() || coincide()){
				tetromino.moveLeft();
			}
		}
	
		public void moveLeftAction(){
			tetromino.moveLeft();
			if(outOfBound() || coincide()){
				tetromino.moveRight();
			}
		}
	
		public boolean outOfBound(Tetromino tetro){
			Cell[] cells = tetro.getCells();
			for(int i=0; i<cells.length; i++){
				Cell cell = cells[i];
				int col = cell.getCol();
				if(col<0 || col>=COLS){
					return true;//出界了
				}
			}
			return false;
		}		
	
		public boolean coincide(Tetromino tetro){
			Cell[] cells = tetro.getCells();
			//for each 循环、迭代，简化了"数组迭代书写"
			for(Cell cell: cells){//Java 5 以后提供增强版for循环
				int row = cell.getRow();
				int col = cell.getCol();
				if(row<0 || row>=ROWS || col<0 || col>=COLS || 
						wall[row][col]!=null){
					return true; //墙上有格子对象，发生重合
				}
			}
			return false;
		} 
		
		public boolean outOfBound(){
			Cell[] cells = tetromino.getCells();
			for(int i=0; i<cells.length; i++){
				Cell cell = cells[i];
				int col = cell.getCol();
				if(col<0 || col>=COLS){
					return true;//出界了
				}
			}
			return false;
		}
		
		private boolean coincide(){
			Cell[] cells = tetromino.getCells();
			//for each 循环、迭代，简化了"数组迭代书写"
			for(Cell cell: cells){//Java 5 以后提供增强版for循环
				int row = cell.getRow();
				int col = cell.getCol();
				if(row<0 || row>=ROWS || col<0 || col>=COLS || 
						wall[row][col]!=null){
					return true; //墙上有格子对象，发生重合
				}
			}
			return false;
		} 		
		
		/** 向右旋转动作 */
		public void rotateRightAction(){
			tetromino.rotateRight();
			if(outOfBound() || coincide()){
				tetromino.rotateLeft();
			}
		}
		
		/** Tetris 类中添加的方法 */
		public void rotateLeftAction(){
			tetromino.rotateLeft();
			if(outOfBound() || coincide()){
				tetromino.rotateRight();
			}
		}
		
		public boolean pause;
		public boolean gameOver;
		
		
		
		private Timer timer;
		
		
		public void startAction(){
			clearWall();
			Random r = new Random();
			int type = r.nextInt(7);
			tetromino = Tetromino.randomTetromino(type);
			nextOne = Tetromino.randomTetromino(type);
			level = 0; score = 0;	pause=false; gameOver=false;
			timer = new Timer();
			timer.schedule(new TimerTask(){
				public void run() {
					softDropAction();
					repaint();  //画自动下落时的图形
				}
			}, 700, 700);
		}
		
		private void clearWall(){
			//将墙的每一行的每个格子清理为null
			for(int row=0; row<ROWS; row++){
				Arrays.fill(wall[row], null);
			}
		}
		
	
		public void pauseAction(){
			timer.cancel();
			pause = true;
			repaint();
		}
		public void continueAction(){
			timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run() {
					softDropAction();
					repaint();
				}
			}, 700, 700);
			pause = false;
			repaint();
		}
	
		public void checkGameOver(){
			if(wall[0][4]==null){
				return;
			}
			gameOver = true;
			timer.cancel();
			repaint();
		}

		
	

	

}
