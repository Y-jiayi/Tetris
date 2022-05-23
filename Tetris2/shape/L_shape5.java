package shape;

import java.awt.*;

public class L_shape5 extends ShapeType{
    public L_shape5(){
        super(new Color(220, 127, 35), 3, 3, 2, new boolean[][] {
                {
                        false,	false,	true,
                        true,	true,	true,
                        false,	false,	false,
                },
                {
                        false,	true,	false,
                        false,	true,	false,
                        false,	true,	true,
                },
                {
                        false,	false,	false,
                        true,	true,	true,
                        true,	false,	false,
                },
                {
                        true,	true,	false,
                        false,	true,	false,
                        false,	true,	false,
                }
        });
    }
}
