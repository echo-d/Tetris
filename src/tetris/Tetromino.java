package tetris;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * 4�񷽿� 
 */
public class Tetromino {
	
	public static final int CELL_SIZE = 26;
	//cells���飬����4������
	protected Cell[] cells = new Cell[4];
	/** ������ת���������λ��״̬ */
	protected State[] states;

	public static Image I;
	public static Image J;
	public static Image L;
	public static Image S;
	public static Image Z;
	public static Image O;
	public static Image T;
	
	static{
		try{
			T=ImageIO.read(Tetromino.class.getResource("T.png"));
			I=ImageIO.read(Tetromino.class.getResource("I.png"));
			S=ImageIO.read(Tetromino.class.getResource("S.png"));
			Z=ImageIO.read(Tetromino.class.getResource("Z.png"));
			L=ImageIO.read(Tetromino.class.getResource("L.png"));
			J=ImageIO.read(Tetromino.class.getResource("J.png"));
			O=ImageIO.read(Tetromino.class.getResource("O.png"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	/** ������� 4�񷽿�, ʹ�ü򵥹�������ģʽ! 
	 * randomTetromino �������һ���ĸ񷽿� 
	 * �������ķ���ֵ�Ƕ�̬��!
	 * @return 
	 * */
	
	public int color;
	
	
	public static Tetromino randomTetromino(int type){
//		Random r = new Random();
//		int type = r.nextInt(7);
		switch(type){
		case 0: return new T();
		case 1: return new I();
		case 2: return new J();
		case 3: return new L();
		case 4: return new O();
		case 5: return new S();
		case 6: return new Z();
		}
		return null;
	}
	
	

	public void draw(Graphics g,int pos_x,int pos_y){		
		for(int i=0;i<4;i++){
			int x = cells[i].getCol();
			int y = cells[i].getRow();
			g.drawImage(cells[i].getImage(), pos_x+x*CELL_SIZE, pos_y+(y-1)*CELL_SIZE, null);
		}
	}	

	

	
	public Cell[] getCells() {
		return cells;
	}

	/** ���� */
	public void softDrop(){
		for(int i=0; i<cells.length; i++){
			cells[i].moveDown();
		}
	}
	
	
	public void moveRight(){
		for(int i=0; i<cells.length; i++){
			this.cells[i].moveRight();
		}
	} 
	
	public void moveLeft(){
		for(int i=0; i<cells.length; i++){
			cells[i].moveLeft();
		}
	}
	
	private int index = 100000;
	/** �� Tetromino ����ӷ���  */
	public void rotateRight() {
		index++;//index = 10001
		// index % states.length = 10001 % 4 = 1
		State s = states[index%states.length];//s1
		// [0] + s1 = [1]
		Cell o = cells[0];//��ȡ��ǰ����
		//�������λ�õĺ���Ϊ��ת�Ժ�ĸ���λ��
		cells[1].setRow(o.getRow()+s.row1);
		cells[1].setCol(o.getCol()+s.col1);
		cells[2].setRow(o.getRow()+s.row2);
		cells[2].setCol(o.getCol()+s.col2);
		cells[3].setRow(o.getRow()+s.row3);
		cells[3].setCol(o.getCol()+s.col3);
	}
	
	/** �� Tetromino ����ӷ���  */
	public void rotateLeft() {
		index--;//index = 10001
		// index % states.length = 10001 % 4 = 1
		State s = states[index%states.length];//s1
		// [0] + s1 = [1]
		Cell o = cells[0];//��ȡ��ǰ����
		cells[1].setRow(o.getRow()+s.row1);
		cells[1].setCol(o.getCol()+s.col1);
		cells[2].setRow(o.getRow()+s.row2);
		cells[2].setCol(o.getCol()+s.col2);
		cells[3].setRow(o.getRow()+s.row3);
		cells[3].setCol(o.getCol()+s.col3);
	}
	
	@Override
	public String toString() {
		return Arrays.toString(cells); 
	}
	
	/** Tetromino ������ӵ� �ڲ��� ���ڼ�¼��ת״̬ */
	protected class State{
		int row0,col0,row1,col1,row2,col2,row3,col3;

		public State(int row0, int col0, int row1, int col1,
				int row2, int col2,
				int row3, int col3) {
			this.row0 = row0;
			this.col0 = col0;
			this.row1 = row1;
			this.col1 = col1;
			this.row2 = row2;
			this.col2 = col2;
			this.row3 = row3;
			this.col3 = col3;
		}	  
	}
	
	
}//Tetromino ��Ľ���



class T extends Tetromino{
	public T() {
		this.cells[0] = new Cell(0, 4, Tetromino.T);
		this.cells[1] = new Cell(0, 3, Tetromino.T);       //������
		this.cells[2] = new Cell(0, 5, Tetromino.T);
		this.cells[3] = new Cell(1, 4, Tetromino.T);
		states = new State[]{
				new State(0,0, 0,-1, 0,1, 1, 0),
				new State(0,0, -1,0, 1,0, 0,-1),
				new State(0,0, 0,1,  0,-1, -1,0),
				new State(0,0, 1,0, -1,0, 0,1)};
	}
}

class I extends Tetromino{
	public I() {
		this.cells[0] = new Cell(0, 4, Tetromino.I);
		this.cells[1] = new Cell(0, 3, Tetromino.I);
		this.cells[2] = new Cell(0, 5, Tetromino.I);
		this.cells[3] = new Cell(0, 6, Tetromino.I);
		states = new State[]{
				new State(0,0, 0,1, 0,-1, 0,-2),
				new State(0,0, -1,0, 1,0,2,0)};
	}
}

class L extends Tetromino {
	public L() {
		this.cells[0] = new Cell(0, 4, Tetromino.L);
		this.cells[1] = new Cell(0, 3, Tetromino.L);
		this.cells[2] = new Cell(0, 5, Tetromino.L);
		this.cells[3] = new Cell(1, 3, Tetromino.L);
		states = new State[]{
				new State(0,0, 0,-1, 0,1, 1,-1 ),
				new State(0,0, -1,0, 1,0, -1,-1),
				new State(0,0, 0,1, 0,-1, -1,1),
				new State(0,0, 1,0, -1,0, 1,1)};	
	}
}

class J extends Tetromino {
	public J() {
		this.cells[0] = new Cell(0, 4, Tetromino.J);
		this.cells[1] = new Cell(0, 3, Tetromino.J);
		this.cells[2] = new Cell(0, 5, Tetromino.J);
		this.cells[3] = new Cell(1, 5, Tetromino.J);
		states = new State[]{
				new State(0,0, 0,-1, 0,1, 1,1),
				new State(0,0, -1,0, 1,0, 1,-1),
				new State(0,0, 0,1, 0,-1, -1,-1),
				new State(0,0, 1,0, -1,0, -1,1 )};
	}
}

class S extends Tetromino {
	public S() {
		this.cells[0] = new Cell(0, 4, Tetromino.S);
		this.cells[1] = new Cell(0, 5, Tetromino.S);
		this.cells[2] = new Cell(1, 3, Tetromino.S);
		this.cells[3] = new Cell(1, 4, Tetromino.S);
		states = new State[]{
			new State(0,0, 0,1, 1,-1, 1,0 ),
			new State(0,0, -1,0, 1,1, 0,1 )};
	}
}

class Z extends Tetromino {
	public Z() {
		this.cells[0] = new Cell(1, 4, Tetromino.Z);
		this.cells[1] = new Cell(0, 3, Tetromino.Z);
		this.cells[2] = new Cell(0, 4, Tetromino.Z);
		this.cells[3] = new Cell(1, 5, Tetromino.Z);
		states = new State[]{
				new State(0,0, -1,-1, -1,0, 0,1 ),
				new State(0,0, -1,1, 0,1, 1,0 )};
	}
}

class O extends Tetromino {
	public O() {
		this.cells[0] = new Cell(0, 4, Tetromino.O);
		this.cells[1] = new Cell(0, 5, Tetromino.O);
		this.cells[2] = new Cell(1, 4, Tetromino.O);
		this.cells[3] = new Cell(1, 5, Tetromino.O);
		states = new State[]{
				new State(0,0, 0,1, 1,0, 1,1 ),
				new State(0,0, 0,1, 1,0, 1,1 )};
	}
}











