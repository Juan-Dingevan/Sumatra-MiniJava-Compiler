///[Error:=|9]
class Main {
    public static void main() {}

    private Interfaz lhs;

    public void m() {
        var rhs = new Clase();
        lhs = rhs;
    }
}

interface Interfaz {}
class Clase {}