///10&exitosamente
class A {
    private int x;

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }
}

class Main {
    public static void main() {
        var a = new A();
        a.setX(10);
        debugPrint(a.getX());
    }
}