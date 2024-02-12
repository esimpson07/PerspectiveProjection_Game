
import java.awt.Color;

/**
 * @author esimpson07
 *
 */
public class Point3D {

    public double x, y, z,vx,vy,vz,radius;
    Color c;

    Point3D(double x, double y, double z, Color c) {
        this.x = x; 
        this.y = y; 
        this.z = z;
        this.c = c; 
    } 

    Point3D(double x, double y, double z) {
        this.x = x; 
        this.y = y;
        this.z = z;
    } 

    Point3D(double x, double y, double z, Color c,double radius) {
        this.x = 400*(Math.random()-0.5);
        this.y = 400*(Math.random()-0.5);
        this.z = 400*(Math.random()-0.5);
        this.vx = Math.random()-0.5;
        this.vy = Math.random()-0.5;
        this.vz = Math.random()-0.5; 
        this.c = c;
        this.radius = radius;
    }

    Point3D(double radius) {
        this.x = 400*(Math.random()-0.5);
        this.y = 400*(Math.random()-0.5);
        this.z = 400*(Math.random()-0.5);
        this.vx = 5*(Math.random()-0.5);
        this.vy = 5*Math.random()-0.5;
        this.vz = 5*Math.random()-0.5; 
        this.radius = radius;
    }

    public double distanceTo(Point3D k) {
        return Math.sqrt(Math.pow(this.x - k.x, 2) + Math.pow(this.y - k.y, 2) + Math.pow(this.z-k.z, 2));
    }

    public double[] getMatris() {
        double[] k = { this.x, this.y, this.z };
        return k;
    }
    
    public double findAngle(Point3D point) {
        return(Math.acos(dotProduct(point) / (findMagnitude(point) * findMagnitude(this))));
    }

    private double dotProduct(Point3D point) {
        return(x * point.getX() + y * point.getY() + z * point.getZ());
    }

    private double findMagnitude(Point3D point) {
        return(Math.sqrt(Math.pow(point.getX(),2) + 
                Math.pow(point.getY(),2) + Math.pow(point.getZ(),2)));
    }
    
    public void setX(double xVal) {
        x = xVal;
    }
    
    public void setY(double yVal) {
        y = yVal;
    }
    
    public void setZ(double zVal) {
        z = zVal;
    }
    
    public double getX() {
        return(x);
    }
    
    public double getY() {
        return(y);
    }
    
    public double getZ() {
        return(z);
    }

    public void setMatris(double[] k) {
        this.x = k[0];
        this.y = k[1];
        this.z = k[2];
    }

}