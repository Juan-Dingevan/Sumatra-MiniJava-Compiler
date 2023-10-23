///[Error:method|11]
class Generic<E> {}

class User {
    public void method(Generic<Object> g) {

    }

    public void m() {
        var x = new Generic<String>();
        this.method(x);
    }
}

class Main {
    public static void main() {}
}