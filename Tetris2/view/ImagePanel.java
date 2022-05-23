package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//有背景图片的面板
public class ImagePanel extends JPanel {
// 用File类型的变量表达图片的路径。

    private File backgroundImageFile;

    public ImagePanel(String backgroundImageFileStr) {
        backgroundImageFile = new File(backgroundImageFileStr);
    }

    @Override
    public void paintComponent(Graphics graphics) {
// 调用父类构造器，在屏幕展示出所有添加进来的元素
        super.paintComponent(graphics);
// 添加一张背景图片
        BufferedImage image = null;
        try {
            image = ImageIO.read(backgroundImageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
//背景图片大小可随窗口大小改变
        graphics.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
    }
}



