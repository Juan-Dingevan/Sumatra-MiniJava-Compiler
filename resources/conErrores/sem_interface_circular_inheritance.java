///[Error:A|2]
interface A extends B {}
interface B extends C {}
interface C extends D {}
interface D extends A {}

class Main {
    public static void main() {}
}