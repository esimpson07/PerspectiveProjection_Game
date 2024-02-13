
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

    //Point3D[] cube = new Point3D[8];
    ArrayList<SpacePolygon> polygons;
    Point mouse_point = new Point(0, 0);
    Point3D origin = new Point3D(Frame.screenSize.getWidth() / 2, Frame.screenSize.getHeight() / 2, 0);
    Point3D projected_2D = new Point3D(0, 0, 0);
    ArrayList<Polygon> drawPolygons;
    Point3D camera = new Point3D(0, 0, 0);
    Point3D rotated = new Point3D(0, 0, 0);
    Point3D facing = new Point3D(0, 0, 0);

    BufferedImage bi;
    
    double angleX = 0;
    double angleY = 0;
    double angleZ = 0;
    double distance = 0;
    double fov = 180;
    double movementSpeed = 50;
    int drawPolygonOrder[];
    boolean keys[] = new boolean[]{false,false,false,false,false,false,false};
    
    private double vertLook = 0, horLook = 0, horRotSpeed = .09, vertRotSpeed = .22;
    private Robot robot;

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
        facing.setX(angleX);
        facing.setY(angleY);
        facing.setZ(0);
    }
    
    private void updateKeys() {
        if(keys[0]) {
            camera.setZ(camera.getZ() - (movementSpeed * Math.cos(angleY)));
            camera.setX(camera.getX() + (movementSpeed * Math.sin(angleY)));
        }
        if(keys[1]) {
            camera.setZ(camera.getZ() - (movementSpeed * Math.sin(angleY)));
            camera.setX(camera.getX() - (movementSpeed * Math.cos(angleY)));
        }
        if(keys[2]) {
            camera.setZ(camera.getZ() + (movementSpeed * Math.cos(angleY)));
            camera.setX(camera.getX() - (movementSpeed * Math.sin(angleY)));
        }
        if(keys[3]) {
            camera.setZ(camera.getZ() + (movementSpeed * Math.sin(angleY)));
            camera.setX(camera.getX() + (movementSpeed * Math.cos(angleY)));
        }
        if(keys[4]) {
            camera.setY(camera.getY() - movementSpeed);
        }
        if(keys[5]) {
            camera.setY(camera.getY() + movementSpeed);
        }
        if(keys[6]) {
            System.exit(0);
        }
    }

    private void init() {
        polygons = new ArrayList<SpacePolygon>();
        drawPolygons = new ArrayList<Polygon>();
        car = new Car(0,0,0);
        //SpacePolygon poly1 = new SpacePolygon(new Point3D(400, -400, -1200), new Point3D(-400, -400, -1200), new Point3D(400, 400, -1200));
        //SpacePolygon poly2 = new SpacePolygon(new Point3D(-400, -400, -1200), new Point3D(-1200, -400, -1200), new Point3D(-800, 400, -1200));
        //SpacePolygon poly3 = new SpacePolygon(new Point3D(1200, -600, 0), new Point3D(1200, -600, 400), new Point3D(800, 400, 0));
        //polygons.add(poly1);
        //polygons.add(poly2);
        //polygons.add(poly3);
        drawPolygons = car.getPolygons();

        bi = new BufferedImage((int)Frame.screenSize.getWidth(), (int)Frame.screenSize.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics= bi.createGraphics();
        graphics.setPaint(Color.white);
    }

    private double[][] projection(double z) {
        double[][] k = { { z, 0, 0 }, { 0, z, 0 }, { 0, 0, 0 } };
        return k;
    }

    private double[][] rotationX(double angle) {
        double[][] k = { { 1, 0, 0 }, 
                { 0, Math.cos(angle), -Math.sin(angle) },
                { 0, Math.sin(angle), Math.cos(angle) } };
        return k;
    }

    private double[][] rotationY(double angle) {
        double[][] k = { { Math.cos(angle), 0, Math.sin(angle) }, { 0, 1, 0 },
                { -Math.sin(angle), 0, Math.cos(angle) } };
        return k;
    }

    private double[][] rotationZ (double angle) {
        double[][] k = { { Math.cos(angle), -Math.sin(angle), 0 }, 
                { Math.sin(angle), Math.cos(angle), 0 },
                { 0, 0, 1 } };
        return k;
    }

    private void connect_points(int k, int l, Integer x[], Integer y[], Graphics g) {
        g.setColor(Color.black);
        for(int i = 0; i < polygons.size(); i ++) {
            if(x[k + (i * 3)] != null && y[k + (i * 3)] != null && x[l + (i * 3)] != null && y[l + (i * 3)] != null) {
                g.drawLine((int) (origin.x + x[k + (i * 3)].intValue()), (int) origin.y + y[k + (i * 3)].intValue(),
                    (int) origin.x + x[l + (i * 3)].intValue(), (int) origin.y + y[l + (i * 3)].intValue());
            }
        }
    }
    
    private void setOrder() {
        double[] k = new double[polygons.size()];
        drawPolygonOrder = new int[polygons.size()];

        for(int i = 0; i < polygons.size(); i ++)
        {
            k[i] = polygons.get(i).getDistanceFrom(camera);
            drawPolygonOrder[i] = i;
        }

        double temp;
        int tempr;        
        for (int a = 0; a < k.length-1; a++) {
            for (int b = 0; b < k.length-1; b++) {
                if(k[b] < k[b + 1])
                {
                    temp = k[b];
                    tempr = drawPolygonOrder[b];
                    drawPolygonOrder[b] = drawPolygonOrder[b + 1];
                    k[b] = k[b + 1];

                    drawPolygonOrder[b + 1] = tempr;
                    k[b + 1] = temp;
                }
            }
        }
    }

    private void draw_poly(Graphics g) {
        g.clearRect(0, 0, (int)Frame.screenSize.getWidth(), (int)Frame.screenSize.getHeight());
        g.setColor(Color.white);
        g.fillRect(0, 0, (int)Frame.screenSize.getWidth(), (int)Frame.screenSize.getHeight());
        drawPolygons.clear();
        setOrder();
        for(int i = 0; i < polygons.size(); i ++) {
            SpacePolygon singlePoly = polygons.get(drawPolygonOrder[i]);
            PsuedoIntegerArray ppoint_x = new PsuedoIntegerArray();
            PsuedoIntegerArray ppoint_y = new PsuedoIntegerArray();
            Point3D[] singlePolyArray = singlePoly.getArray();
            for(Point3D singlePoint : singlePolyArray) {
                double[] originalMatrix = singlePoint.getMatris();
                double[] playerMatrix = camera.getMatris();
                double[] adjustedMatrix = {originalMatrix[0] - playerMatrix[0], originalMatrix[1] - playerMatrix[1], originalMatrix[2] - playerMatrix[2]};
                rotated.setMatris(Matrix.multiply(rotationY(facing.getY()), adjustedMatrix));
                rotated.setMatris(Matrix.multiply(rotationX(facing.getX()), rotated.getMatris()));
                rotated.setMatris(Matrix.multiply(rotationZ(facing.getZ()), rotated.getMatris()));
                double z = (fov / (fov * distance - rotated.z / 4));
                projected_2D.setMatris(Matrix.multiply(projection(z), rotated.getMatris()));
                if(z >= 0) {
                    ppoint_x.add((int)projected_2D.x + (int)Frame.screenSize.getWidth() / 2);
                    ppoint_y.add((int)projected_2D.y + (int)Frame.screenSize.getHeight() / 2);
                }
            }
            g.setColor(Color.BLACK);
            g.drawPolygon(ppoint_x.getArray(),ppoint_y.getArray(),ppoint_x.getArray().length);
            g.setColor(Color.RED);
            g.fillPolygon(ppoint_x.getArray(),ppoint_y.getArray(),ppoint_x.getArray().length);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        updateKeys();
        draw_poly(bi.getGraphics());
        
        g.drawImage(bi, 0, 0, null);
    }
}