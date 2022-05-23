package shape;

import java.awt.*;

public class Line_shape4 extends ShapeType {

    public Line_shape4() {
        super(new Color(35, 220, 220), 4, 4, 1, new boolean[][]{
                {
                        false, false, false, false,
                        true, true, true, true,
                        false, false, false, false,
                        false, false, false, false,
                },
                {
                        false, false, true, false,
                        false, false, true, false,
                        false, false, true, false,
                        false, false, true, false,
                },
                {
                        false, false, false, false,
                        false, false, false, false,
                        true, true, true, true,
                        false, false, false, false,
                },
                {
                        false, true, false, false,
                        false, true, false, false,
                        false, true, false, false,
                        false, true, false, false,
                }
        });
    }
}
