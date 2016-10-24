package game;

import javax.swing.*;
import java.awt.*;

/**
 * Created by scott on 10/23/16.
 */
class GameButton extends JButton {

    @Override
    public void setText(String str) {
        super.setText(str);
        if (str.equals(""))
            setBackground(null);
        else
            switch (Integer.parseInt(str)) {
                case 2:
                    setBackground(new Color(238, 228, 218));
                    break;
                case 4:
                    setBackground(new Color(237, 224, 200));
                    break;
                case 8:
                    setBackground(new Color(242, 177, 121));
                    break;
                case 16:
                    setBackground(new Color(245, 149, 99));
                    break;
                case 32:
                    setBackground(new Color(246, 124, 95));
                    break;
                case 64:
                    setBackground(new Color(246, 94, 59));
                    break;
                case 128:
                    setBackground(new Color(237, 207, 114));
                    break;
                case 256:
                    setBackground(new Color(237, 204, 97));
                    break;
                case 512:
                    setBackground(new Color(237, 200, 80));
                    break;
                case 1024:
                    setBackground(new Color(237, 197, 63));
                    break;
                case 2048:
                    setBackground(new Color(255, 255, 0));
                    break;
                case 4096:
                    setBackground(new Color(94, 218, 146));
                    break;
                default:
                    setBackground(new Color(255, 255, 0));
            }
    }
}
