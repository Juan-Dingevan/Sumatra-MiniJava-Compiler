///[SinErrores]
class A {}

class Abuelo<One, Two, Three> {
    One m1() {}
    Two m2() {}
    Three m3() {}
}

class Padre<X, Y> extends Abuelo<Y, X, A> {

}

class Hija<X, Y> extends Padre<X, Y> {
    Y m1() {}
    X m2() {}
    A m3() {}
}

class Main {
    public static void main() {}
}