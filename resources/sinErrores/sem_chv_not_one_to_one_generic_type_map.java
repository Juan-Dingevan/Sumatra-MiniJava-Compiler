///[SinErrores]
class Pair<A, B> {
    public A a;
    public B b;

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }
}

class WeirdPair<X> extends Pair<X, X> {
    public X getA() {}
    public void setA(X a) {}
    public X getB() {}
    public void setB(X b) {}
}

class StringPair extends Pair<String, String> {
    public String getA() {}
    public void setA(String a) {}
    public String getB() {}
    public void setB(String b) {}
}

class Main {
    public static void main() {}
}