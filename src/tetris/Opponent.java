package tetris;

import javax.swing.*;



public class Opponent  {

    public static Opponent opponent;

    private TetrisDuel panel;
    private Tetris tetris=new Tetris();

    public Tetris getTetris() {
        return tetris;
    }

    private Tetromino curTetromino;
    private Tetromino nextTetromino;

    public void setcurTetromino(int color){
        curTetromino = Tetromino.randomTetromino(color);
        curTetromino.color=color;
    }
    
    public void setNextTetromino(int color){
        nextTetromino =Tetromino.randomTetromino(color);
        nextTetromino.color=color;
    }
    
    public Tetromino getCurTetromino() {
        return curTetromino;
    }

    public Tetromino getNextTetromino() {
        return nextTetromino;
    }

    public Opponent(TetrisDuel panel) {
        this.panel=panel;
        curTetromino =Tetromino.randomTetromino(0);
        curTetromino.color=0;
        nextTetromino =Tetromino.randomTetromino(1);
        nextTetromino.color=1;
    }

    public void TetrominoUp() {
        curTetromino.rotateRight();
        panel.repaint();
    }
    
    public void TetrominoDown() {
        curTetromino.softDrop();
        panel.repaint();
    }

    public void hardDrop() {
    	while(!tetris.tetrominoCanDrop(curTetromino)){				
			curTetromino.softDrop();
			}
    	panel.repaint();
    }
    
    public void TetrominoLeft() {
        curTetromino.moveLeft();
        panel.repaint();

    }

    public void TetrominoRight() {
        curTetromino.moveRight();
        panel.repaint();
    }

    public void isPut(){
    	tetris.tetrominoCanDrop(curTetromino);
    }
    public void ispop(){
    	tetris.ispop();
    }
    public void gameover(){
      
        int opponentScore=Self.self.gameover();
        int myScore=tetris.score;
        String str=Integer.toString(myScore)+"比"+Integer.toString(opponentScore)+",";

        if(myScore<opponentScore){
       
            JOptionPane.showMessageDialog(panel, str + "你赢了");
        }else if(myScore>opponentScore){
         
            JOptionPane.showMessageDialog(panel,str+"你输了");
        }else{
            JOptionPane.showMessageDialog(panel,str+"平局");
        }
    }


}
