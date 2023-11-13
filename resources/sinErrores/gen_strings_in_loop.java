///test&test&test&exitosamente
class Main {
    public static void main() {
        var i = 0;
        while(i < 3) {
            var s = "test";
            System.printSln(s);
            i = i + 1;
        }

        i = 0;
        while(i < 3) {
            var s = new String();
            System.printSln(s);
            i = i + 1;
        }
    }
}