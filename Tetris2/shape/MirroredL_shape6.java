package shape;

import java.awt.*;

public class MirroredL_shape6 extends ShapeType{
    public MirroredL_shape6(){
        super(new Color(35, 35, 220), 3, 3, 2, new boolean[][] {
                {
                        true,	false,	false,
                        true,	true,	true,
                        false,	false,	false,
                },
                {
                        false,	true,	true,
                        false,	true,	false,
                        false,	true,	false,
                },
                {
                        false,	false,	false,
                        true,	true,	true,
                        false,	false,	true,
                },
                {
                        false,	true,	false,
                        false,	true,	false,
                        true,	true,	false,
                }
        });
    }
}
