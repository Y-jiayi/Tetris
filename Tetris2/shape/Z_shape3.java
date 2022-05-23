package shape;

import java.awt.*;

public class Z_shape3 extends ShapeType{
    public Z_shape3(){
        super(new Color(220,35, 35), 3, 3, 2, new boolean[][] {
                {
                        true,	true,	false,
                        false,	true,	true,
                        false,	false,	false,
                },
                {
                        false,	false,	true,
                        false,	true,	true,
                        false,	true,	false,
                },
                {
                        false,	false,	false,
                        true,	true,	false,
                        false,	true,	true,
                },
                {
                        false,	true,	false,
                        true,	true,	false,
                        true,	false,	false,
                }
        });
    }
}
