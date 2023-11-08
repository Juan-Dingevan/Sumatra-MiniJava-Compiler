///5&4&3&2&1&0&exitosamente
class Rec {

    public void rec(int n) {
        if(n < 0)
            return;

        debugPrint(n);
        rec(n - 1);
    }
}

class Init {
    public static void main() {
        var r = new Rec();
        r.rec(5);
    }
}