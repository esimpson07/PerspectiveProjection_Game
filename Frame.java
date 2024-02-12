import java.awt.Color;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;

/**
 * @author esimpson07
 *
 */
public class Frame extends JFrame {
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Insets p;

    Frame() {
        Canvas canvas = new Canvas();
        canvas.setBackground(Color.white);
        init(canvas);
        Timer myTimer = new Timer(); 
        TimerTask gorev = new TimerTask() {
                @Override
                public void run() {
                    canvas.repaint(); 
                } 
            };
        myTimer.schedule(gorev, 1, 1);
    }

    public void init(Canvas canvas) { 
        this.add(canvas);
        this.setUndecorated(true);
        this.setSize(screenSize);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        p = this.getInsets();
    }
}