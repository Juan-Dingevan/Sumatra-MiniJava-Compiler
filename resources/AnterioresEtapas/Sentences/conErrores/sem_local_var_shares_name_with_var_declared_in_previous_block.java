///[Error:x|9]
class Main {
    public static void main() {}

    public void m() {
        var x = 3;

        if(x > 4) {
            var x = 40;
        }
    }
}