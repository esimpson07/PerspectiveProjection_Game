import java.util.Scanner;
import java.util.ArrayList;

public class OBJ_to_Point3D_Polygons
{   
    public static ArrayList<Point3D> points = new ArrayList<Point3D>();
    
    public static void main(String[] args) {
        Scanner myObj = new Scanner(System.in);
        System.out.println("POINTS = ");
        for(int i = 0; i < 999; i ++) {
            String str = myObj.nextLine();
            int x = myObj.nextInt();
            int y = myObj.nextInt();
            int z = myObj.nextInt();
            points.add(new Point3D(x,y,z));
        }
        System.out.println(points);
    }
}
