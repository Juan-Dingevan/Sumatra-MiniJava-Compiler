///[Error:=|12]
interface I {}

class A implements I {}

class Main {
    public A a;
    public I i;
    public static void main() {}

    private void m() {
        a = i;
    }
}