import java.util.ArrayList;

public class PsuedoIntegerArray
{
    private ArrayList<Integer> values;
    
    public PsuedoIntegerArray() {
        values = new ArrayList<Integer>();
    }
    
    public void add(int value) {
        values.add(value);
    }
    
    public void remove(int index) {
        values.remove(index);
    }
    
    public int[] getArray() {
        int[] arr = new int[values.size()];
        for(int i = 0; i < values.size(); i ++) {
            arr[i] = values.get(i);
        }
        return(arr);
    }
}
