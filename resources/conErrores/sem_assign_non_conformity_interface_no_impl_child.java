///[Error:=|9]
class Main {
    public static void main() {}

    private Hija lhs;

    public void m() {
        var rhs = new Clase();
        lhs = rhs;
    }
}

interface Padre {}
interface Hija extends Padre {}

class Clase implements Padre {}