package view;


import javax.swing.*;

import java.io.File;

import java.io.FileReader;

//线程二：选择loadgame时调出文档选择器并调用线程三
public class Thread2 extends Thread{
    public JFileChooser jfc = new JFileChooser("out/saving");
    public FileReader reader;
    public static File file;
     public Thread2(){

     }
     public void run() {
        jfc.showDialog(new JLabel(),"Choose");
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        file=jfc.getSelectedFile();
        Thread3 thread3 = new Thread3();
        thread3.start();
     }
}

