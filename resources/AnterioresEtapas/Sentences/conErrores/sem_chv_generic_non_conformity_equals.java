///[Error:==|8]
class Generic<E> {}

class User {
    public void m() {
        var o = new Generic<Object>();
        var s = new Generic<String>();
        var b = o == s;
    }
}

class Main {
    public static void main() {}
}