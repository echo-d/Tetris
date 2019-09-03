package tetris;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.MusicController;
import socket.ExchangeThread;

import tetris.Tetris;
import tetris.TetrisDuel;
import tetris.Tetromino;


//
////游戏进程控制器，比如碰撞检测之类的
//
public class Self {


public static Self self;
	private Tetris tetris;
	
	private Tetromino curTetromino;
	private Tetromino nextTetromino;


private JPanel panel;

private Timer timer;

private boolean isRunning =false;



public Tetris getTetris() {
	return tetris;
}

public Tetromino getCurTetromino() {
	return curTetromino;
}

public Tetromino getNextTetromino() {
	return nextTetromino;
}


// 远程通信用的线程
private ExchangeThread exchangeThread;

private class GameTask extends TimerTask {
	private int speed = 10;
    public void run() {
		if(!isRunning){
			return ;
		       }
		// speed来控制时间间隔。。
    	if(speed <= 0){
			if(tetris.tetrominoCanDrop(curTetromino)){
				if(tetris.gameover()) {
					isRunning = false;
					if(MusicController.isturnOn()){
						MusicController.setturnOn(false);
						MusicController.bgmStop();
					}
					if(exchangeThread!=null){
						exchangeThread.sendMessage("gameover");
						int myScore = tetris.score;
						int opponentScore = Opponent.opponent.getTetris().score;

						String str = Integer.toString(myScore) + "比" + Integer.toString(opponentScore) + ",";
						if (myScore > opponentScore) {
							// WIN
							JOptionPane.showMessageDialog(panel, str + "你赢了");
						} else if (myScore < opponentScore) {
							// LOSE
							JOptionPane.showMessageDialog(panel, str + "你输了");
						} else {
							// pingju
							JOptionPane.showMessageDialog(panel, str + "这是一场平局");
						}
					}else{
						
					}
					return;
			     	}
				
				
				
				Random random = new Random();
				// 已经放下来了的意思
				if(exchangeThread!=null){
					exchangeThread.sendMessage("isput");
				   }
				int temp=random.nextInt(7);
				curTetromino = nextTetromino;					
				nextTetromino = Tetromino.randomTetromino(temp);
				nextTetromino.color=temp;
				if(exchangeThread!=null){
					// 更新rect的命令,只发送下一个的
					exchangeThread.sendMessage(Integer.toString(temp));
				    }
				if(tetris.ispop()){
					// 消去一行
					if(exchangeThread!=null){
						exchangeThread.sendMessage("ispop");
					}
				}
            }
			else{
				curTetromino.softDrop();
				if(exchangeThread!=null)
					exchangeThread.sendMessage("down");
			}
			// 如果没有放下来，就down
            panel.repaint();
            speed=10-tetris.level;
    	}
    	else{
			speed--;
		}
    }
}

public Self(ExchangeThread thread,TetrisDuel panel) {
	this.exchangeThread=thread;
	this.panel=(TetrisDuel)panel;

}

/**
 * 启动游戏
 */
public void gameStart(){
	tetris = new Tetris();
	tetris.pause=false; 
	// 随机产生方块
	this.curTetromino = Tetromino.randomTetromino(0);
	curTetromino.color=0;
	this.nextTetromino = Tetromino.randomTetromino(1);
	nextTetromino.color=1;
	isRunning =true;
	timer = new Timer();
	timer.schedule(new GameTask(),10,30);
}

public void keyUp() {
	if(!isRunning) return;			
	curTetromino.rotateRight();
	if(tetris.outOfBound(curTetromino) || tetris.coincide(curTetromino)){
		curTetromino.rotateLeft();
		return;
	}
	if(exchangeThread!=null)
		exchangeThread.sendMessage("up");
	panel.repaint();		
}


public void keyDown() {
	if(!isRunning) return;
	if(tetris.tetrominoCanDrop(curTetromino)) return;
	
	curTetromino.softDrop();
	if(exchangeThread!=null)
		exchangeThread.sendMessage("down");
	panel.repaint();

}

public void keyLeft() {
	if(!isRunning) return;
	if(tetris.isleftside(curTetromino))	return;	
	curTetromino.moveLeft();
	if(exchangeThread!=null)
		exchangeThread.sendMessage("left");
	panel.repaint();
			
}

public void keyRight() {
	if(!isRunning) return;
	if(tetris.isrightside(curTetromino))	return;		
	curTetromino.moveRight();
	if(exchangeThread!=null)
		exchangeThread.sendMessage("right");
	panel.repaint();
			
}


public void keyHardDrop()  {
	if(!isRunning) return;
	while(!tetris.tetrominoCanDrop(curTetromino)){				
		curTetromino.softDrop();
		}
	if(exchangeThread!=null)
		exchangeThread.sendMessage("hardDrop");
	panel.repaint();
	
}

//向远程发送消息
public void keyPause()  {
	tetris.pause=true; 
	panel.repaint();
	if(MusicController.isRunning()){
		MusicController.bgmStop();
	}
	isRunning =false;	
	if(exchangeThread!=null)
		exchangeThread.sendMessage("keyPause");		
}
//向远程发送恢复命令
public void keyResume() {
	isRunning =true;
	tetris.pause=false; 
	
	if(exchangeThread!=null)
		exchangeThread.sendMessage("keyResume");
}


//接收远程的暂停指令	
public void pause()  {
	tetris.pause=true; 
	panel.repaint();
	if(MusicController.isRunning()){
		MusicController.bgmStop();
	}
	isRunning =false;
	
}

//接收远程的恢复指令
public void resume() {
	isRunning =true;
	tetris.pause=false; 
}


public int gameover(){
	System.out.print("gameover");
	isRunning=false;
	if(MusicController.isturnOn()){
		MusicController.setturnOn(false);
		MusicController.bgmStop();
	}
	return tetris.score;
}


public boolean isRunning() {
	return isRunning;
}



}

