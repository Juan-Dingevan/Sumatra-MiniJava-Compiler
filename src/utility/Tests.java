package utility;
public class Tests {
    private interface Abuelo {}
    private interface Padre extends Abuelo {}
    private interface Hija extends Padre {}

    private class A implements Abuelo {}
    private class B extends A {}

    private class U implements Padre {}
    private class V extends U {}

    private class X implements Hija {}
    private class Y extends X {}

    private void m() {
        Abuelo abuelo = null;
        Padre padre = null;
        Hija hija = null;

        A a = null;
        B b = null;

        U u = null;
        V v = null;

        X x = null;
        Y y = null;

        abuelo = padre;
        abuelo = hija;
        abuelo = a;
        abuelo = b;
        abuelo = u;
        abuelo = v;
        abuelo = x;
        abuelo = y;

        padre = padre;
        padre = hija;
        //padre = a;
        //padre = b;
        padre = u;
        padre = v;
        padre = x;
        padre = y;
    }
}
