///[Error:=|9]
class Main {
    public static void main() {}

    public void m() {
        var lhs = new Hija();
        var rhs = new Abuelo();

        lhs = rhs;
    }
}

class Hija extends Padre {}
class Padre extends Abuelo {}
class Abuelo {}