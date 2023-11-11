///El numero 42 no es primo&El numero 17 es primo&exitosamente
class Main {
    public static void main() {
        esPrimo(42);
        esPrimo(17);
    }

    public static void esPrimo(int n) {
        var count = 1;
        var divisores = 0;

        while (count < n) {
            if (n % count == 0) {
                divisores = divisores + 1;
            }
            count = count + 1;
        }

        if (divisores > 2) {
            System.printS("El numero ");
            System.printI(n);
            System.printSln(" no es primo");
        } else {
            System.printS("El numero ");
            System.printI(n);
            System.printSln(" es primo");
        }
    }
}