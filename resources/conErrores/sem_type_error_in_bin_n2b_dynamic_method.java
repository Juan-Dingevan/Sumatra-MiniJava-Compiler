///[Error:>|10]
class Main {
    public static void main() {}

    public String s() {
        return "3";
    }

    public void m() {
        var x = 1 > s();
    }
}