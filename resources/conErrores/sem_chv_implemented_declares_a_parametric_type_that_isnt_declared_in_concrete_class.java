///[Error:D|3]
interface Padre<A, B, C> {}
class Hija<A, B, C> implements Padre<A, B, D> {}

class Main {
    public static void main() {}
}