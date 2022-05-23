package shape;

import java.awt.*;

public class S_shape1 extends ShapeType{

    public S_shape1() {
        super(new Color(35, 220, 35), 3, 3, 2, new boolean[][] {
                {
                        false,	true,	true,
                        true,	true,	false,
                        false,	false,	false,
                },
                {
                        false,	true,	false,
                        false,	true,	true,
                        false,	false,	true,
                },
                {
                        false,	false,	false,
                        false,	true,	true,
                        true,	true,	false,
                },
                {
                        true,	false,	false,
                        true,	true,	false,
                        false,	true,	false,
                }
        });
    }
}
