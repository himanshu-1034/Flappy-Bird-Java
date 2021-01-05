package flappyBird;

import javax.swing.*;
import java.awt.*;

public class ViewRenderer extends JPanel {

    @Override
    public void paint(Graphics g) {

        FlappyBirdMain.flappyBird.paint(g);
    }
}
