package view;

import java.io.IOException;

import static view.MainFrame.*;
//线程一：创建新frame并打开游戏面板
public class Thread1 extends Thread{

    public Thread1(){

    }
    public void run() {
        mainFrame.setVisible(false);
        tetris = new MainFrame(" ");
        tetris.setVisible(true);
        tetris.startGame();

    }
}
