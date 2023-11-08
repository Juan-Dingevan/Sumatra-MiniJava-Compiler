///1&exitosamente
class P {
    public static int x;
}

class H extends P {}

class Main {
    public static void main() {
        var h = new H();
        h.x = 1;
        var x = h.x;
        debugPrint(x);
    }
}