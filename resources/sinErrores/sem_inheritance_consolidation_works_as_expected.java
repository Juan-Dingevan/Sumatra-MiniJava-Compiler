///[SinErrores]
class Padre {
    int iPadre;
    String sPadre;

    static void metodoPadre() {}
    static float metodoPadre2(float f) {}
}

class Hija extends Padre {
    int iHija;
    String sHija;

    private int metodoHija(String s) {}
}

interface AbueloI {
    void metodoAbueloI(int x);
}

interface PadreI extends AbueloI{
    void metodoPadreI();
}

interface HijaI extends PadreI {
    void metodoHijaI(Object o);
}