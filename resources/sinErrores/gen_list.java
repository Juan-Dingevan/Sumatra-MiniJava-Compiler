///Dibujando un circulo&Dibujando un rectangulo&Dibujando un triangulo&exitosamente
class Nodo {
    Forma forma;
    Nodo siguiente;

    public Nodo(Forma forma) {
        this.forma = forma;
        this.siguiente = null;
    }
}

class ListaEnlazada {
    Nodo inicio;

    public ListaEnlazada() {
        this.inicio = null;
    }

    public void agregar(Forma forma) {
        var nuevoNodo = new Nodo(forma);
        if (inicio == null) {
            inicio = nuevoNodo;
        } else {
            var actual = inicio;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevoNodo;
        }
    }

    public void imprimir() {
        var actual = inicio;
        while (actual != null) {
            actual.forma.dibujar();
            actual = actual.siguiente;
        }
    }
}

interface Forma {
    void dibujar();
}

class Circulo implements Forma {
    public void dibujar() {
        System.printSln("Dibujando un circulo");
    }
}

class Rectangulo implements Forma {
    public void dibujar() {
        System.printSln("Dibujando un rectangulo");
    }
}

class Triangulo implements Forma {
    public void dibujar() {
        System.printSln("Dibujando un triangulo");
    }
}

class Main {
    public static void main() {
        var lista = new ListaEnlazada();

        // Agrega algunas formas a la lista
        lista.agregar(new Circulo());
        lista.agregar(new Rectangulo());
        lista.agregar(new Triangulo());

        // Imprime las formas
        lista.imprimir();
    }
}