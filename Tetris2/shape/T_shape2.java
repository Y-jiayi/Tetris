package shape;

import java.awt.*;

public class T_shape2 extends ShapeType{
    public T_shape2(){
        super(new Color(128,220, 128), 3, 3, 2, new boolean[][] {
                {
                        false,	true,	false,
                        true,	true,	true,
                        false,	false,	false,
                },
                {
                        false,	true,	false,
                        false,	true,	true,
                        false,	true,	false,
                },
                {
                        false,	false,	false,
                        true,	true,	true,
                        false,	true,	false,
                },
                {
                        false,	true,	false,
                        true,	true,	false,
                        false,	true,	false,
                }
        });
    }
}
