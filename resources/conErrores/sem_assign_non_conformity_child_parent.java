///[Error:=|9]
class Main {
    public static void main() {}

    public void m() {
        var lhs = new Hija();
        var rhs = new Padre();

        lhs = rhs;
    }
}

class Padre {}
class Hija extends Padre {}
