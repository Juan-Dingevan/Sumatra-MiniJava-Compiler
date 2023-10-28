///[Error:new|6]
class G<A, B> {}

class C {
    public void m() {
        var g = new G<String>();
    }
}

class Main {
    public static void main() {}
}