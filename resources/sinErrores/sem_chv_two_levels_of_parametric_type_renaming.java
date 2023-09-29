///[SinErrores]
class Abuelo<X> {
    X metodoAbuelo() {}
}

class Padre<Y> extends Abuelo<Y> {
    Y metodoPadre() {}
}

class Hija<Z> extends Padre<Z> {
    Z metodoAbuelo() {}
    Z metodoPadre() {}
    Z metodoHija(Z z) {}
}

class Bebe extends Hija<String> {
    String metodoAbuelo() {}
    String metodoPadre() {}
    String metodoHija(String z) {}
    String metodoBebe() {}
    String s;

}

class Main {
    public static void main() {}
}