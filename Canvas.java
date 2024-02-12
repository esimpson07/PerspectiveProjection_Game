
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
    ArrayList<Point3D[]> polygons;
    Point mouse_point = new Point(0, 0);
    Point3D origin = new Point3D(Frame.screenSize.getWidth() / 2, Frame.screenSize.getHeight() / 2, 0);
    Point3D projected_2D = new Point3D(0, 0, 0);
    ArrayList<Point3D[]> projectedPolygons;
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
    double movementSpeed = 10;
    
    private double vertLook = 0, horLook = 0, horRotSpeed = .09, vertRotSpeed = .22;
    Double ppoint_x[] = new Double[10];
    Double ppoint_y[] = new Double[10];
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
            camera.setZ(camera.getZ() - movementSpeed);
        }
        if(e.getKeyCode() == KeyEvent.VK_S) {
            camera.setZ(camera.getZ() + movementSpeed);
        } 
        if(e.getKeyCode() == KeyEvent.VK_A) {
            camera.setX(camera.getX() - movementSpeed);
        }
        if(e.getKeyCode() == KeyEvent.VK_D) {
            camera.setX(camera.getX() + movementSpeed);
        }
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }
    
    public void keyReleased(KeyEvent e) {}

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
        angleY += difY  / 3600;
        angleX -= difX / 3600;
        double r = 1; // Math.sqrt(1 - (angleY * angleY));
        facing.setX(angleX);
        facing.setY(angleY);
        facing.setZ(0);
        //facing.setZ();
    }
    
    /*
    private void mouseMovement(double newMouseX, double newMouseY) {        
        double  difY = (newMouseX - Frame.screenSize.getWidth() / 2);  
        double  difX = (newMouseY - Frame.screenSize.getHeight() / 2);
        angleY += difY  / 3600;
        angleX -= difX / 3600;
        double r = 1; // Math.sqrt(1 - (angleY * angleY));
        facing.setY(Math.cos(angleX));
        facing.setX(Math.sin(angleX));
        facing.setZ(angleY);
        //facing.setZ();
    }
    
    private void mouseMovement() {  
        mXDist = mXOG - e.getX();
        mYDist = mYOG - e.getY();
        angleY += dpi * 180 * (2 * (double)(mXDist) / (double)(width));
        angleX -= dpi * 180 * (2 * (double)(mYDist) / (double)(width));
        angleY = angleY % 360;
        angleX = clamp(angleX,-70,70);
        mXOG = e.getX();
        mYOG = e.getY();
        setPlayerAngle(angleX,angleY,0);
    }
    
    private void mouseMovement(double newMouseX, double newMouseY) {        
        double difX = (newMouseX - Frame.screenSize.getWidth()/2);
        double difY = (newMouseY - Frame.screenSize.getHeight()/2);
        difY *= 6 - Math.abs(vertLook) * 5;
        vertLook -= difY  / vertRotSpeed;
        horLook += difX / horRotSpeed;

        if(vertLook > 0.99999) {
            vertLook = 0.99999;
        } else if(vertLook < -0.99999) {
            vertLook = -0.99999;
        }

        updateView();
    }

    private void updateView() {
        //double r = Math.sqrt(1 - (vertLook * vertLook));
        //facing.setX(facing.getX() + r * Math.cos(horLook));
        //facing.setY(facing.getY() + r * Math.sin(horLook));        
        //facing.setZ(facing.getZ() + vertLook);
        facing.setX(horLook);
        facing.setY(vertLook);
    }*/

    private void init() {
        polygons = new ArrayList<Point3D[]>();
        projectedPolygons = new ArrayList<Point3D[]>();
        drawPolygons = new ArrayList<Polygon>();
        Point3D[] poly1 = {new Point3D(400, -400, -1200), new Point3D(-400, -400, -1200), new Point3D(400, 400, -1200)};
        Point3D[] poly2 = {new Point3D(-400, -400, -1200), new Point3D(-1200, -400, -1200), new Point3D(-800, 400, -1200)};
        Point3D[] poly3 = {new Point3D(1200, -600, 0), new Point3D(1200, -600, 400), new Point3D(800, 400, 0)};
        polygons.add(poly1);
        polygons.add(poly2);
        polygons.add(poly3);
        drawPolygons.add(new Polygon());

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

    private void connect_points(int k, int l, Double x[], Double y[], Graphics g) {
        g.setColor(Color.black);
        for(int i = 0; i < polygons.size(); i ++) {
            if(x[k + (i * 3)] != null && y[k + (i * 3)] != null && x[l + (i * 3)] != null && y[l + (i * 3)] != null) {
                g.drawLine((int) (origin.x + x[k + (i * 3)].intValue()), (int) origin.y + y[k + (i * 3)].intValue(),
                    (int) origin.x + x[l + (i * 3)].intValue(), (int) origin.y + y[l + (i * 3)].intValue());
            }
        }
    }

    private void draw_poly(Graphics g) {
        g.clearRect(0, 0, (int)Frame.screenSize.getWidth(), (int)Frame.screenSize.getHeight());
        g.setColor(Color.white);
        g.fillRect(0, 0, (int)Frame.screenSize.getWidth(), (int)Frame.screenSize.getHeight());
        int i = 0;
        for(Point3D[] singlePoly : polygons) {
            for(Point3D singlePoint : singlePoly) {
                double[] originalMatrix = singlePoint.getMatris();
                double[] playerMatrix = camera.getMatris();
                double[] adjustedMatrix = {originalMatrix[0] - playerMatrix[0], originalMatrix[1] - playerMatrix[1], originalMatrix[2] - playerMatrix[2]};
                System.out.println("facingX = " + facing.getX());
                System.out.println("facingY = " + facing.getY());
                System.out.println("facingZ = " + facing.getZ());
                rotated.setMatris(Matrix.multiply(rotationY(facing.getY()), adjustedMatrix));
                rotated.setMatris(Matrix.multiply(rotationX(facing.getX()), rotated.getMatris()));
                rotated.setMatris(Matrix.multiply(rotationZ(facing.getZ()), rotated.getMatris()));
                double z = (fov / (fov * distance - rotated.z / 4));
                projected_2D.setMatris(Matrix.multiply(projection(z), rotated.getMatris()));
                if(z >= 0) {
                    ppoint_x[i] = projected_2D.x;
                    ppoint_y[i] = projected_2D.y;
                } else {
                    ppoint_x[i] = null;
                    ppoint_y[i] = null;
                }
                i++;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        bi.getGraphics().setColor(Color.BLACK);

        draw_poly(bi.getGraphics());
        connect_points(0, 1, ppoint_x, ppoint_y, bi.getGraphics());
        connect_points(1, 2, ppoint_x, ppoint_y, bi.getGraphics());
        connect_points(2, 0, ppoint_x, ppoint_y, bi.getGraphics());
        g.drawImage(bi, 0, 0, null);
    }
}