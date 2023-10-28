///[Error:=|9]
class Main {
    public static void main() {}

    private A rhs;
    private U lhs;

    public void m() {
        lhs = rhs;
    }
}

interface Padre {}
interface Hija extends Padre {}

class A implements Padre {}
class U implements Hija {}