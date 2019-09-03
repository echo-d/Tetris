//����
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
 * ����˹���鵥�� 
 *
 */


public class Tetris extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Tetromino tetromino;

	private Tetromino nextOne;
	/** ���� */
	public static final int ROWS = 20;
	/** ���� */
	public static final int COLS = 10;
	/** ǽ   ��ά���� */
	private Cell[][] wall = new Cell[ROWS][COLS]; 
	/** ���������� */
//	public int lines = 0;
	/** ���� */
	public int score = 0;
	
	public static final int CELL_SIZE = 26;
	
	private static Image background;//����ͼƬ
	
	static{
		try{
			background = ImageIO.read(Tetris.class.getResource("tetris.png"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void action(){
		startAction();
		repaint();  //�ظ�����paint()
		KeyAdapter l = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(MusicController.isturnOn())
					MusicController.actionPlay();
				
				int key = e.getKeyCode();
				if(key == KeyEvent.VK_Q){
					System.exit(0);//�˳���ǰ��Java����
				}
				if(gameOver){
					if(key==KeyEvent.VK_S){
						MusicController.setturnOn(true);
						MusicController.bgmPlay();
						startAction();
					}
					return;
				}
				//�����ͣ���Ұ�����[C]�ͼ�������
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
				//��������������
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
		g.translate(15, 15);//��һ��ͼ����һ��������ʾ��ͼ������Ͻ�λ�ڣ�15,15�����
		paintTetromino(g);//������������ķ���
		paintWall(g);//��ǽ
		paintNextOne(g);
		paintScore(g);
	}
	
	private JPanel panel;
	public static final int FONT_COLOR = 0x667799;
	public static final int FONT_SIZE = 0x20;
	private void paintScore(Graphics g) {
		Font f = getFont();//��ȡ��ǰ�� ���Ĭ������
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
	
	
	//�� Tetris �� ����� ���� paintWall
		public void paintWall(Graphics g){
			//wall.length ��ά���������
			for(int row=0; row<wall.length; row++){
				//wall[row].length��ʾ��Ӧ�еĳ���
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
			//wall.length ��ά���������
			for(int row=0; row<wall.length; row++){
				//wall[row].length��ʾ��Ӧ�еĳ���
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
		 * �� Tetris(����˹����) �������ӷ���
		 * ��������Ĺ����ǣ�������Ķ��� ��������
		 * ��ɹ��ܣ�����ܹ���������䣬�������½��ǽ�ϣ�
		 *   ���µķ�����ֲ���ʼ���¡�
		 */
		public void softDropAction(){
			if(tetrominoCanDrop()){
				tetromino.softDrop();
			}else{
				tetrominoLandToWall();//wall[row][col]=cell
				destroyLines();//�ƻ�������
				checkGameOver();
				tetromino = nextOne;
				Random r = new Random();
				int type = r.nextInt(7);
				nextOne = Tetromino.randomTetromino(type);
			}
		}
		
		//��������
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
		 * �Ƿ�ɹ���ȥһ��
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
				//�����͵ȼ�����
				score +=lines*lines;
				if(score > (2+2*level*level))
					level++;
			}
			return iscancel;
		}
		
		
	
		/** �����Ѿ������У����ҼƷ�
		 * 1������ÿһ�� 
		 * 2���������飩ĳ�����Ǹ����� ���������� 
		 **/
		public void destroyLines(){
			
			int lines = 0;
			for(int row = 0; row<wall.length; row++){
				if(fullCells(row)){
					deleteRow(row);
					lines++;
					if(lines != 0){
						//�����͵ȼ�����
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
				if(line[i]==null){//����пո�ʽ�Ͳ�������
					return false;
				}
			}
			return true;
		}
		
		public void deleteRow(int row){
			if(MusicController.isturnOn())
				MusicController.deletePlay();
			for(int i=row; i>=1; i--){
				//���� [i-1] -> [i] 
				System.arraycopy(wall[i-1], 0, wall[i], 0, COLS);
			}
			Arrays.fill(wall[0], null);
		}
		
		
		
		/** ��鵱ǰ��4�񷽿��ܷ�������� */
		public boolean tetrominoCanDrop(){
			Cell[] cells = tetromino.getCells();
			for(int i = 0; i<cells.length; i++){
				int row = cells[i].getRow(); 
				if(row == ROWS-1){
					return false;
					}//���׾Ͳ����½���
			}
			for(int i = 0; i<cells.length; i++){
				int row = cells[i].getRow(); 
				int col = cells[i].getCol();
				if(wall[row+1][col] != null){
					return false;//�·�ǽ���з���Ͳ����½���
				}
			}
			return true;
		}
		
		
		public boolean isleftside(Tetromino tetro){		
			Cell[] cells = tetro.getCells();
			for(int i=0;i<4;i++){
				//�����ǽ���߷��鶼����
				if(cells[i].getCol()==0|| (wall[cells[i].getRow()][cells[i].getCol()-1]!=null))		
					return true;
			}
			return false;
		}
		
		public boolean isrightside(Tetromino tetro){	
			Cell[] cells = tetro.getCells();
			for(int i=0;i<4;i++){
				//�ұ���ǽ���߷��鶼����
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
					}//���׾Ͳ����½���
			      }
			
			for(int i = 0; i<cells.length; i++){
				int row = cells[i].getRow(); 
				int col = cells[i].getCol();
				if(wall[row+1][col] != null){								   			
					isput=true;
					//�·�ǽ���з���Ͳ����½���
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
		
		/** 4�񷽿���½��ǽ�� */
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
					return true;//������
				}
			}
			return false;
		}		
	
		public boolean coincide(Tetromino tetro){
			Cell[] cells = tetro.getCells();
			//for each ѭ��������������"���������д"
			for(Cell cell: cells){//Java 5 �Ժ��ṩ��ǿ��forѭ��
				int row = cell.getRow();
				int col = cell.getCol();
				if(row<0 || row>=ROWS || col<0 || col>=COLS || 
						wall[row][col]!=null){
					return true; //ǽ���и��Ӷ��󣬷����غ�
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
					return true;//������
				}
			}
			return false;
		}
		
		private boolean coincide(){
			Cell[] cells = tetromino.getCells();
			//for each ѭ��������������"���������д"
			for(Cell cell: cells){//Java 5 �Ժ��ṩ��ǿ��forѭ��
				int row = cell.getRow();
				int col = cell.getCol();
				if(row<0 || row>=ROWS || col<0 || col>=COLS || 
						wall[row][col]!=null){
					return true; //ǽ���и��Ӷ��󣬷����غ�
				}
			}
			return false;
		} 		
		
		/** ������ת���� */
		public void rotateRightAction(){
			tetromino.rotateRight();
			if(outOfBound() || coincide()){
				tetromino.rotateLeft();
			}
		}
		
		/** Tetris ������ӵķ��� */
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
					repaint();  //���Զ�����ʱ��ͼ��
				}
			}, 700, 700);
		}
		
		private void clearWall(){
			//��ǽ��ÿһ�е�ÿ����������Ϊnull
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
