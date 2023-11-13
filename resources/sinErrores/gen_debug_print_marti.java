///2&exitosamente
class S {
    static void debugPrint(int i){
        System.printIln(i*2);
    }
}
class Init {
    static Object s;
    static void main() {
        s = new S();
        s.debugPrint(2);
    }
}