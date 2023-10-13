///[Error:!|8]
class Main {
    public static void main() {}

    public int i;

    public void m() {
        var x = !A.a();
    }
}

class A {
    public static String a(){
        return "";
    }
}