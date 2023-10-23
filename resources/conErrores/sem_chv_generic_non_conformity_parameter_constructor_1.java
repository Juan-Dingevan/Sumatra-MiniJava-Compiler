///[Error:Generic|13]
class Generic<E> {
    public Generic(E e) {}
}

class User {
    public void method(Generic<Object> g) {

    }

    public void m() {
        var o = new Object();
        var x = new Generic<String>(o);
    }
}

class Main {
    public static void main() {}
}