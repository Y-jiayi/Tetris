package shape;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

//随机产生不同颜色
public class Shape extends JPanel {

    public Color color = randomColor();

    public static Color randomColor(){
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return new Color(r, g, b);
    }
}
