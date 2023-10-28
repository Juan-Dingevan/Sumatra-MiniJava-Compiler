///[Error:+|10]
class Main {
    public static void main() {}

    public Object s() {
        return new Object();
    }

    public void m() {
        var x = 1 + s();
    }
}