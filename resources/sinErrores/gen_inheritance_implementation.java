///1&1&2&Parent&2&Child&exitosamente
interface I {
    int cat(int i);
}

class Parent implements I {
    int cat(int i) {
        return i;
    }

    void badlyPrintName() {
        System.printC('P');
        System.printC('a');
        System.printC('r');
        System.printC('e');
        System.printC('n');
        System.printC('t');
        System.println();
        System.println();
    }
}

class Child extends Parent {
    void badlyPrintName() {
        System.printC('C');
        System.printC('h');
        System.printC('i');
        System.printC('l');
        System.printC('d');
        System.println();
        System.println();
    }
}

class Client {
    static void useI(I i) {
        debugPrint(i.cat(1));
    }

    static void useP(Parent p) {
        debugPrint(p.cat(2));
        p.badlyPrintName();
    }
}

class Init {
    public static void main() {
        var p = new Parent();
        var c = new Child();

        Client.useI(p);             //1
        Client.useI(c);             //1

        Client.useP(p);             //2\nParent
        Client.useP(c);             //2\nChild
    }
}