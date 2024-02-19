import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Cursor;
import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.Polygon;

import javax.swing.JPanel;

/**
 * @author esimpson07
 *
 */

public class Canvas extends JPanel implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener{

    //Vector[] cube = new Vector[8];
    Point mouse_point = new Point(0, 0);

    BufferedImage bi;
    
    double angleX = 0;
    double angleY = 0;
    double angleZ = 0;
    
    double movementSpeed = 50;
    boolean keys[] = new boolean[]{false,false,false,false,false,false,false};
    long lastCheck = 0;
    int checks = 0;
    private double vertLook = 0, horLook = 0, horRotSpeed = .09, vertRotSpeed = .22;
    private double fps;
    private Robot robot;
    private Car car;

    Canvas() {

        init();
        setOpaque(true);
        this.addKeyListener(this);
        setFocusable(true);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
            
        try {
            robot = new Robot();
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Cursor invisibleCursor = toolkit.createCustomCursor(new BufferedImage(1, 1, BufferedImage.TRANSLUCENT), new Point(0,0), "InvisibleCursor");        
            setCursor(invisibleCursor);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
    
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_W) {
            keys[0] = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_A) {
            keys[1] = true;
        } 
        if(e.getKeyCode() == KeyEvent.VK_S) {
            keys[2] = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_D) {
            keys[3] = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            keys[4] = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
            keys[5] = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            keys[6] = true;
        }
    }
    
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_W) {
            keys[0] = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_A) {
            keys[1] = false;
        } 
        if(e.getKeyCode() == KeyEvent.VK_S) {
            keys[2] = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_D) {
            keys[3] = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            keys[4] = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
            keys[5] = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            keys[6] = false;
        }
    }

    public void keyTyped(KeyEvent e) {}

    public void mouseDragged(MouseEvent m) {
        mouseMovement(m.getX(), m.getY());
        centerMouse();
    }

    public void mouseMoved(MouseEvent m) {
        mouseMovement(m.getX(), m.getY());
        centerMouse();
    }

    public void mouseClicked(MouseEvent m) {}

    public void mouseEntered(MouseEvent m) {}

    public void mouseExited(MouseEvent m) {}

    public void mousePressed(MouseEvent m) {}

    public void mouseReleased(MouseEvent m) {}

    public void mouseWheelMoved(MouseWheelEvent m) {}

    private void centerMouse() {
        robot.mouseMove((int)(Frame.screenSize.getWidth() / 2),(int)(Frame.screenSize.getHeight() / 2));
    }
    
    private void mouseMovement(double newMouseX, double newMouseY) {        
        double  difY = (newMouseX - Frame.screenSize.getWidth() / 2);  
        double  difX = (newMouseY - Frame.screenSize.getHeight() / 2);
        angleY += difY  / 1800;
        angleX -= difX / 1800;
        Calculator.setFacingX(angleX);
        Calculator.setFacingY(angleY);
        Calculator.setFacingZ(0);
    }
    
    private void updateKeys() {
        if(keys[0]) {
            Calculator.setCameraZ(Calculator.getCameraZ() - (movementSpeed * Math.cos(angleY)));
            Calculator.setCameraX(Calculator.getCameraX() + (movementSpeed * Math.sin(angleY)));
        }
        if(keys[1]) {
            Calculator.setCameraZ(Calculator.getCameraZ() - (movementSpeed * Math.sin(angleY)));
            Calculator.setCameraX(Calculator.getCameraX() - (movementSpeed * Math.cos(angleY)));
        }
        if(keys[2]) {
            Calculator.setCameraZ(Calculator.getCameraZ() + (movementSpeed * Math.cos(angleY)));
            Calculator.setCameraX(Calculator.getCameraX() - (movementSpeed * Math.sin(angleY)));
        }
        if(keys[3]) {
            Calculator.setCameraZ(Calculator.getCameraZ() + (movementSpeed * Math.sin(angleY)));
            Calculator.setCameraX(Calculator.getCameraX() + (movementSpeed * Math.cos(angleY)));
        }
        if(keys[4]) {
            Calculator.setCameraY(Calculator.getCameraY() - movementSpeed);
        }
        if(keys[5]) {
            Calculator.setCameraY(Calculator.getCameraY() + movementSpeed);
        }
        if(keys[6]) {
            System.exit(0);
        }
    }

    private void init() {
        int n = 40;
        int tileSize = 1000;
        ArrayList<SpacePolygon> polygons = new ArrayList<SpacePolygon>();
        for(int x = -n; x <= n; x ++) {
            for(int y = -n; y <= n; y ++) {
                polygons.add(new SpacePolygon(new Vector(tileSize + (x * tileSize), 0, tileSize + (y * tileSize)), 
                    new Vector((x * tileSize), 0, tileSize + (y * tileSize)), new Vector((x * tileSize), 0, (y * tileSize))));
                polygons.add(new SpacePolygon(new Vector(tileSize + (x * tileSize), 0, tileSize + (y * tileSize)), 
                    new Vector(tileSize + (x * tileSize), 0, (y * tileSize)), new Vector((x * tileSize), 0, (y * tileSize))));
            }
        }
        Calculator.setPolygonArray(polygons);

        bi = new BufferedImage((int)Frame.screenSize.getWidth(), (int)Frame.screenSize.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics= bi.createGraphics();
        graphics.setPaint(Color.white);
    }

    private void drawScreen(Graphics g) {
        g.clearRect(0, 0, (int)Frame.screenSize.getWidth(), (int)Frame.screenSize.getHeight());
        g.setColor(Color.white);
        g.fillRect(0, 0, (int)Frame.screenSize.getWidth(), (int)Frame.screenSize.getHeight());
        //setOrder();
        Calculator.drawPoly1(g);
        Calculator.drawPoly2(g);
        checks ++;            
        if(checks >= 15) {
            g.setColor(Color.black);
            fps = checks / ((System.currentTimeMillis() - lastCheck) / 1000.0);
            lastCheck = System.currentTimeMillis();
            checks = 0;
        }
        g.drawString(new String("FPS:" + (int)fps), 50, 50);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        updateKeys();
        drawScreen(bi.getGraphics()); //RESETS THE SCREEN BEFORE DRAWING POLYGONS!!!
        g.drawImage(bi, 0, 0, null);
    }
}
