///[SinErrores]
class Clase {
    void m1() {
        //x is of type T1, which has a public method m1 of
        //type T2, which as a public method m2();
        x.m1.m2();
    }
}