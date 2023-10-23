///[SinErrores]
class G<A, B> {}

class C {
    private G<String, String> g;
    public void m() {
        g = new G<>();
    }
}

class Main {
    public static void main() {}
}