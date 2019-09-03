package socket;


import java.io.*;
import java.net.Socket;

import tetris.Opponent;
import tetris.Self;


public class ExchangeThread implements Runnable {
    private Socket socket;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;

    public static boolean isNum(String str){
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    public ExchangeThread(Socket socket) {
        this.socket = socket;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch (Exception e){
            e.printStackTrace();
        }
        new Thread(this).start();
    }
    public void run() {
        try {
            while(true) {
                String mess = bufferedReader.readLine();
                if(isNum(mess)) {
                    int color=Integer.parseInt(mess);
                    if(Opponent.opponent.getCurTetromino()==null){
                    	Opponent.opponent.setcurTetromino(color);
                    }else{
                    	Opponent.opponent.setcurTetromino(Opponent.opponent.getNextTetromino().color);
                        Opponent.opponent.setNextTetromino(color);
                    }
                }
                switch (mess){
                    case "up":
                    	Opponent.opponent.TetrominoUp();
                        break;
                    case "down":
                    	Opponent.opponent.TetrominoDown();
                        break;
                    case "left":
                    	Opponent.opponent.TetrominoLeft();
                        break;
                    case "right":
                    	Opponent.opponent.TetrominoRight();
                        break;
                    case "isput":
                    	Opponent.opponent.isPut();
                        break;
                    case "ispop":
                    	Opponent.opponent.ispop();
                        break;
                    case "gameover":
                    	Opponent.opponent.gameover();
                        break;                
                    case "hardDrop":
                    	Opponent.opponent.hardDrop();
                        break;                     
                    case "keyPause":
                    	Self.self.pause();
                        break;
                    case "keyResume":
                    	Self.self.resume();
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("服务器 run 异常: " + e.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    socket = null;
                    System.out.println("服务端 finally 异常:" + e.getMessage());
                }
            }
        }
    }

    public void sendMessage(String str){
        try {
            bufferedWriter.write(str);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}