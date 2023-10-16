///[Error:=|9]
class Main {
    public static void main() {}

    private Padre rhs;
    private Hija lhs;

    public void m() {
        lhs = rhs;
    }
}

interface Padre {}
interface Hija extends Padre {}
