///[Error:!|8]
class Main {
    public static void main() {}

    public String i() {return "3";}

    public void m() {
        var x = !i();
    }
}