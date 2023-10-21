///[Error:this|8]
class Main {
    public static void main() {}

    public int x;

    public static void m() {
        var y = (this).x;
    }
}