package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import tetris.Self;


public class KeyController extends KeyAdapter{

	private Self localController;
	public KeyController(Self local){
		this.localController = local;
	}
	@Override
	public void keyPressed(KeyEvent e) {
		
		if(MusicController.isturnOn())
			MusicController.actionPlay();	
		
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
		
		if(e.getKeyCode()==KeyEvent.VK_C) {
			this.localController.keyResume();
			if(!MusicController.isRunning()){
				MusicController.bgmPlay();
			}
			return;
		}
		
	
		if(localController.isRunning()) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					this.localController.keyUp();
					break;
				case KeyEvent.VK_DOWN:
					this.localController.keyDown();
					break;
				case KeyEvent.VK_LEFT:
					this.localController.keyLeft();
					break;
				case KeyEvent.VK_RIGHT:
					this.localController.keyRight();
					break;
				case KeyEvent.VK_SPACE:
					this.localController.keyHardDrop();
					break;
				case KeyEvent.VK_P:
					this.localController.keyPause();				
					break;				
				case KeyEvent.VK_Q:
					System.exit(0);
					break;
				default:
					break;
			}
		}



	}
	
}
