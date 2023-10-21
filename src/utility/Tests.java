package utility;
public class Tests {
    private class A {
        public int x = 1;
    }

    public static A attr;
    public int dynamic = 1;

    public static void m() {
        attr.x = 2;
    }

    public static int m2() {
        //return dynamic;
        return 1;
    }
}
