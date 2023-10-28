///[Error:Generic|13]
class Generic<E> {
    public Generic(Generic<E> g) {}
}

class User {
    public void method(Generic<Object> g) {

    }

    public void m() {
        var o = new Generic<Object>(null);
        var x = new Generic<String>(o);
    }
}

class Main {
    public static void main() {}
}