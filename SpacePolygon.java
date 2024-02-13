public class SpacePolygon
{
    private Point3D[] points;
    private Point3D midPoint;
    
    public SpacePolygon(Point3D p1, Point3D p2, Point3D p3) {
        points = new Point3D[] {p1, p2, p3};
        midPoint = new Point3D((p1.getX() + p2.getX() + p3.getX()) / 3,
            (p1.getY() + p2.getY() + p3.getY()) / 3,
            (p1.getZ() + p2.getZ() + p3.getZ()) / 3);
    }
    
    public SpacePolygon(int x1, int y1, int z1, int x2, int y2, int z2, int x3, int y3, int z3) {
        Point3D p1 = new Point3D(x1, y1, z1);
        Point3D p2 = new Point3D(x2, y2, z2);
        Point3D p3 = new Point3D(x3, y3, z3);
        points = new Point3D[] {p1, p2, p3};
        midPoint = new Point3D((p1.getX() + p2.getX() + p3.getX()) / 3,
            (p1.getY() + p2.getY() + p3.getY()) / 3,
            (p1.getZ() + p2.getZ() + p3.getZ()) / 3);
    }
    
    public int getDistanceFrom(Point3D point) {
        return((int)(Math.sqrt(Math.pow(point.getX() - midPoint.getX(), 2) + Math.pow(point.getY() - midPoint.getY(), 2) + Math.pow(point.getZ() - midPoint.getZ(), 2))));
    }
    
    public Point3D[] getArray() {
        return(points);
    }
}