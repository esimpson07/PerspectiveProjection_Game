import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.awt.Polygon;

public class Calculator extends Thread {
    public static Vector origin = new Vector(Frame.screenSize.getWidth() / 2, Frame.screenSize.getHeight() / 2, 0);
    public static Vector projected_2D = new Vector(0, 0, 0);
    public static ArrayList<SpacePolygon> polygons;
    public static Vector camera = new Vector(0, 0, 0);
    public static Vector rotated = new Vector(0, 0, 0);
    public static Vector facing = new Vector(0, 0, 0);
    public static int drawPolygonOrder[];
    public static double distance = 0;
    public static double fov = 110;
    
    public static double[][] projection(double z) {
        double[][] k = { { z, 0, 0 }, { 0, z, 0 }, { 0, 0, 0 } };
        return k;
    }

    public static double[][] rotationX(double angle) {
        double[][] k = { { 1, 0, 0 }, 
                { 0, Math.cos(angle), -Math.sin(angle) },
                { 0, Math.sin(angle), Math.cos(angle) } };
        return k;
    }

    public static double[][] rotationY(double angle) {
        double[][] k = { { Math.cos(angle), 0, Math.sin(angle) }, { 0, 1, 0 },
                { -Math.sin(angle), 0, Math.cos(angle) } };
        return k;
    }

    public static double[][] rotationZ (double angle) {
        double[][] k = { { Math.cos(angle), -Math.sin(angle), 0 }, 
                { Math.sin(angle), Math.cos(angle), 0 },
                { 0, 0, 1 } };
        return k;
    }

    public static void connect_points(int k, int l, Integer x[], Integer y[], Graphics g, ArrayList<SpacePolygon> polygons, Vector origin) {
        g.setColor(Color.black);
        for(int i = 0; i < polygons.size(); i ++) {
            if(x[k + (i * 3)] != null && y[k + (i * 3)] != null && x[l + (i * 3)] != null && y[l + (i * 3)] != null) {
                g.drawLine((int) (origin.x + x[k + (i * 3)].intValue()), (int) origin.y + y[k + (i * 3)].intValue(),
                    (int) origin.x + x[l + (i * 3)].intValue(), (int) origin.y + y[l + (i * 3)].intValue());
            }
        }
    }
    
    public static void setOrder() {
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
    
    public static void drawPoly1(Graphics g) {
        for(int i = 1; i < polygons.size(); i += 2) {
            //SpacePolygon singlePoly = polygons.get(drawPolygonOrder[i]);
            SpacePolygon singlePoly = polygons.get(i);
            PsuedoIntegerArray ppoint_x = new PsuedoIntegerArray();
            PsuedoIntegerArray ppoint_y = new PsuedoIntegerArray();
            Vector[] singlePolyArray = singlePoly.getArray();
            for(Vector singlePoint : singlePolyArray) {
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
    
    public static void drawPoly2(Graphics g) {
        for(int i = 0; i < polygons.size(); i += 2) {
            //SpacePolygon singlePoly = polygons.get(drawPolygonOrder[i]);
            SpacePolygon singlePoly = polygons.get(i);
            PsuedoIntegerArray ppoint_x = new PsuedoIntegerArray();
            PsuedoIntegerArray ppoint_y = new PsuedoIntegerArray();
            Vector[] singlePolyArray = singlePoly.getArray();
            for(Vector singlePoint : singlePolyArray) {
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
    
    public static void setPolygonArray(ArrayList<SpacePolygon> polys) {
        polygons = polys;
    }
    
    public static void setFacingX(double angle) {
        facing.setX(angle);
    }
    
    public static void setFacingY(double angle) {
        facing.setY(angle);
    }
    
    public static void setFacingZ(double angle) {
        facing.setZ(angle);
    }
    
    public static void setCameraX(double angle) {
        camera.setX(angle);
    }
    
    public static void setCameraY(double angle) {
        camera.setY(angle);
    }
    
    public static void setCameraZ(double angle) {
        camera.setZ(angle);
    }
    
    public static double getCameraX() {
        return(camera.getX());
    }
    
    public static double getCameraY() {
        return(camera.getY());
    }
    
    public static double getCameraZ() {
        return(camera.getZ());
    }
}