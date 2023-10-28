///[Error:m|8]
class Generic<E> {
    public static E m() {}
}

class User {
    public void method() {
        var x = Generic.m();
    }
}

class Main {
    public static void main() {}
}