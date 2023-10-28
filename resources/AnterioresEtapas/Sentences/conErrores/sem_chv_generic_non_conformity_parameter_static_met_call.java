///[Error:metodo|11]
class Pair<A, B> {}

class Estatica {
    public static void metodo(Pair<String, Object> p) {}
}

class User {
    public void m() {
        var p = new Pair<String, String>();
        Estatica.metodo(p);
    }
}

class Main {
    public static void main() {}
}