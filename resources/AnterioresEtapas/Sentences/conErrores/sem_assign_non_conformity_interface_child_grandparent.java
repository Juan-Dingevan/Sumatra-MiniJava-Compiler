///[Error:=|9]
class Main {
    public static void main() {}

    private Abuelo rhs;
    private Hija lhs;

    public void m() {
        lhs = rhs;
    }
}

interface Abuelo {}
interface Padre extends Abuelo {}
interface Hija extends Padre {}
