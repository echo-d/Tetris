package controller;

import javax.sound.sampled.*;
import java.io.*;  
  

public class MusicController {
	 static volatile boolean running =false;
	    static volatile boolean turnOn =true;
	    private static String bgmUrl="music\\1757.wav";
	    private static String rotateUrl="music\\rotate.wav";
	    private static String deleteUrl="music\\delete.wav";
	    
static MusicController bgm = new MusicController(bgmUrl); 
	    public static void bgmPlay(){
	    	if(!turnOn)
	    		return;
	    	  //�������ֲ�����            
	    	 bgm.start(true);  
	    	 running =true;
	    }
	
	    
	    public static void bgmStop(){
	    	bgm.stop();
	    	 running =false;
	    }
	    
	    
	    public static void deletePlay(){
	    	if(!turnOn)
	    		return;
	    	 MusicController delete = new MusicController(deleteUrl);   //�������ֲ�����            
	    	 delete.start(false);  
	    }
	    
	    
	    public static void actionPlay(){
	    	if(!turnOn)
	    		return;
	    	 MusicController rotate = new MusicController(rotateUrl);   //�������ֲ�����            
	    	 rotate.start(false);          
	    	
	    }
	    
	    
	    private String musicPath; //��Ƶ�ļ�  
	    private volatile boolean run = true;  //��¼��Ƶ�Ƿ񲥷�  
	    private Thread mainThread;   //������Ƶ�������߳�  
	      
	    private AudioInputStream audioStream;  
	    private AudioFormat audioFormat;  
	    private SourceDataLine sourceDataLine;  
	      
	    public MusicController(String musicPath) {  
	        this.musicPath = musicPath;  
	        prefetch();  
	    }  
	      
	    //����׼��  
	    private void prefetch(){  
	        try{  
	        //��ȡ��Ƶ������  
	        audioStream = AudioSystem.getAudioInputStream(new File(musicPath));  
	        //��ȡ��Ƶ�ı������  
	        audioFormat = audioStream.getFormat();  
	        //��װ��Ƶ��Ϣ  
	        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class,  
	                audioFormat,AudioSystem.NOT_SPECIFIED);  
	        //ʹ�ð�װ��Ƶ��Ϣ���Info�ഴ��Դ�����У��䵱��Ƶ����Դ  
	        sourceDataLine = (SourceDataLine)AudioSystem.getLine(dataLineInfo);  
	          
	        sourceDataLine.open(audioFormat);  
	        sourceDataLine.start();  
	          
	        }catch(UnsupportedAudioFileException ex){  
	            ex.printStackTrace();  
	        }catch(LineUnavailableException ex){  
	            ex.printStackTrace();  
	        }catch(IOException ex){  
	            ex.printStackTrace();  
	        }  
	          
	    }  
	    //��������:�ر���Ƶ��ȡ����������  
	    protected void finalize() throws Throwable{  
	        super.finalize();  
	        sourceDataLine.drain();  
	        sourceDataLine.close();  
	        audioStream.close();  
	    }  
	      
	    //������Ƶ:ͨ��loop���������Ƿ�ѭ������  
	    private void playMusic(boolean loop)throws InterruptedException {  
	        try{  
	                if(loop){  
	                    while(true){  
	                        playMusic();  
	                    }  
	                }else{  
	                    playMusic();  
	                    //��������в��ر�  
	                    sourceDataLine.drain();  
	                    sourceDataLine.close();  
	                    audioStream.close();  
	                }  
	              
	        }catch(IOException ex){  
	            ex.printStackTrace();  
	        }  
	          
	          
	    }  
	    private void playMusic(){  
	        try{  
	            synchronized(this){  
	                run = true;  
	            }  
	            //ͨ�������ж�ȡ��Ƶ�����������͵�������;  
	            //������������̣�AudioInputStream -> SourceDataLine;  
	            audioStream = AudioSystem.getAudioInputStream(new File(musicPath));  
	            int count;  
	            byte tempBuff[] = new byte[1024];  
	              
	                while((count = audioStream.read(tempBuff,0,tempBuff.length)) != -1){  
	                    synchronized(this){  
	                    while(!run)  
	                        wait();  
	                    }  
	                    sourceDataLine.write(tempBuff,0,count);  
	                              
	            }  
	  
	        }catch(UnsupportedAudioFileException ex){  
	            ex.printStackTrace();  
	        }catch(IOException ex){  
	            ex.printStackTrace();  
	        }catch(InterruptedException ex){  
	            ex.printStackTrace();  
	        }  
	          
	    }  
	      
	      
	    //��ͣ������Ƶ  
	    private void stopMusic(){  
	        synchronized(this){  
	            run = false;  
	            notifyAll();  
	        }  
	    }  
	    //������������  
	    private void continueMusic(){  
	        synchronized(this){  
	             run = true;  
	             notifyAll();  
	        }  
	    }  
	      
	      
	    //�ⲿ���ÿ��Ʒ���:������Ƶ���̣߳�  
	    public void start(boolean loop){  
	        mainThread = new Thread(new Runnable(){  
	            public void run(){  
	                try {  
	                    playMusic(loop);  
	                } catch (InterruptedException e) {  
	                    e.printStackTrace();  
	                }  
	            }  
	        });  
	        mainThread.start();  
	    }  
	      
	    //�ⲿ���ÿ��Ʒ�������ͣ��Ƶ�߳�  
	    public void stop(){  
	        new Thread(new Runnable(){  
	            public void run(){  
	                stopMusic();  
	                  
	            }  
	        }).start();  
	    }  
	    //�ⲿ���ÿ��Ʒ�����������Ƶ�߳�  
	    public void continues(){  
	        new Thread(new Runnable(){  
	            public void run(){  
	                continueMusic();  
	            }  
	        }).start();  
	    } 
	    
	    
	    public static boolean isRunning(){
	        return running;
	    }
	  
	    public static boolean isturnOn(){
	    	return turnOn;
	    }
	 
	    public static void setturnOn(Boolean turn){
	    	turnOn=turn;
	    }
}
