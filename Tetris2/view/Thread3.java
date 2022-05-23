package view;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static view.MainFrame.*;
//线程三：读取文件信息，开始读档游戏
public class Thread3 extends Thread{
    FileReader reader;
    public Thread3(){}
    public void run(){
        tetris = new MainFrame(" ");
        tetris.setVisible(true);
//读入文档信息
        try {
            reader = new FileReader(Thread2.file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            tetris.gameSpeed = Double.parseDouble(bufferedReader.readLine());
            int loadScore;
            loadScore = Integer.parseInt(bufferedReader.readLine());
            tetris.score = loadScore;
            String a;
//加入颜色、坐标信息
            while ((a = bufferedReader.readLine() )!= null) {
//逐行读入信息，再拆开字符串
                tetris.board.load.add(a);
                String[] split= a.split(" ");
//将信息加入数组
                tetris.board.colors.add(Color.decode(split[0]));
                tetris.board.rows.add(Integer.parseInt(split[1]));
                tetris.board.columns.add(Integer.parseInt(split[2]));

            }
        }catch (NullPointerException | IOException e){
        }
       try {reader.close();
        } catch (NullPointerException | IOException e) {
        }
//调用读档游戏方法
        tetris.loadGame();
        tetris.setVisible(true);
    }
}
