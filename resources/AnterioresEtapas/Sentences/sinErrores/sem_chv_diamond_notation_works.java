///[SinErrores]
class G<E> {}

class Estatica {
    public static void metodo(G<String> g) {}
}

class Constructor {
    public Constructor(G<String> g) {}
}

class A {
    private G<String> g1;

    private G<String> g2() {
        return new G<>();
        //se infiere por el ret. type del método.
    }

    private void g3(G<String> s) {}

    public void m() {
        //se infiere por el tipo estático
        g1 = new G<>();
        //se infiere por el tipo del parámetro
        g3(new G<>());

        //se infiere por el tipo de g4
        var g4 = new G<String>();
        g4 = new G<>();

        Estatica.metodo(new G<>());

        var c = new Constructor(new G<>());
    }
}



class Main {
    public static void main() {}
}