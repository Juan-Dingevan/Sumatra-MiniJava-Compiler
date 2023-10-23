///[Error:m|8]
class Generic<E> {
    public static void m(E e) {}
}

class User {
    public void method() {
        Generic.m("Hello");
    }
}

class Main {
    public static void main() {}
}