/**
 */
public class MyMain {

    public static void main(String[] args) throws Exception {
        MyMain m = new MyMain();
        m.sort(6,2,4,1,3,0,5,8,7,9); // length 10
    }


    int[] list;

    void sort(int... array) {
        list = new int[array.length-1];
        merge(0, array.length-1);
    }

    void merge(int start, int end) {
        if (start<end) {
            int center = (start + end) / 2;
            System.out.println("x \t"+start+"\t"+center+"\t"+end);
            merge(start, center);
            merge(center + 1, end);
            mersrt(start, center, end);
        }
    }

    void mersrt(int start, int center, int end) {
        System.out.println("y\t"+start+"\t"+center+"\t"+end);
    }
}
