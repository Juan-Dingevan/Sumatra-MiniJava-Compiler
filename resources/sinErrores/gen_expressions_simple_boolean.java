///true&false&true&false&false&false&true&true&true&false&true&false&true&false&true&false&true&false&true&false&true&false&true&true&false&false&true&true&false&false&true&false&true&false&true&false&false&true&true&false&false&true&true&false&exitosamente
class Main {
    public static void main() {
        simpleUnaryBoolean();
        simpleBinaryBooleanBoolean();
        simpleBinaryNumericBoolean();
        simpleOthersBoolean();
    }

    public static void simpleUnaryBoolean() {
        var t = true;
        var f = false;

        var not1 = !f;          //true
        var not2 = !t;          //false

        System.printBln(not1);
        System.printBln(not2);
        System.println();
    }

    public static void simpleBinaryBooleanBoolean() {
        var t = true;
        var f = false;

        var and1 = t && t;      //true
        var and2 = t && f;      //false
        var and3 = f && t;      //false
        var and4 = f && f;      //false

        var or1 = t || t;      //true
        var or2 = t || f;      //true
        var or3 = f || t;      //true
        var or4 = f || f;      //false

        System.printBln(and1);
        System.printBln(and2);
        System.printBln(and3);
        System.printBln(and4);
        System.println();

        System.printBln(or1);
        System.printBln(or2);
        System.printBln(or3);
        System.printBln(or4);
        System.println();
    }

    public static void simpleBinaryNumericBoolean() {
        var gt1 = 3 > 2;
        var gt2 = 3 > 3;

        var ge1 = 3 >= 3;
        var ge2 = 3 >= 4;

        var lt1 = 2 < 3;
        var lt2 = 3 < 3;

        var le1 = 3 <= 3;
        var le2 = 4 <= 3;

        System.printBln(gt1);
        System.printBln(gt2);
        System.println();

        System.printBln(ge1);
        System.printBln(ge2);
        System.println();

        System.printBln(lt1);
        System.printBln(lt2);
        System.println();

        System.printBln(le1);
        System.printBln(le2);
        System.println();
    }

    public static void simpleOthersBoolean() {
        var e1 = 1 == 1; //true
        var e2 = 1 == 2; //false

        var e3 = 'c' == 'c'; //true
        var e4 = 'c' == 'd'; //false

        var e5 = true == true; //true
        var e6 = false == false; //true
        var e7 = true == false; //false
        var e8 = false == true; //true

        var s = "";
        var o = new Object();

        var e9 = s == s; //true
        var e10 = o == o; //true
        var e11 = o == s; //false
        var e12 = s == o; //false

        o = s;

        var e13 = s == o; //true

        System.printBln(e1);
        System.printBln(e2);
        System.printBln(e3);
        System.printBln(e4);
        System.printBln(e5);
        System.printBln(e6);
        System.printBln(e7);
        System.printBln(e8);
        System.printBln(e9);
        System.printBln(e10);
        System.printBln(e11);
        System.printBln(e12);
        System.printBln(e13);
        System.println();

        e1 = 1 != 1; //false
        e2 = 1 != 2; //true

        e3 = 'c' != 'c'; //false
        e4 = 'c' != 'd'; //true

        e5 = true != true; //false
        e6 = false != false; //false
        e7 = true != false; //true
        e8 = false != true; //true

        s = "";
        o = new Object();

        e9 = s != s; //false
        e10 = o != o; //false
        e11 = o != s; //true
        e12 = s != o; //true

        o = s;

        e13 = s != o; //false

        System.printBln(e1);
        System.printBln(e2);
        System.printBln(e3);
        System.printBln(e4);
        System.printBln(e5);
        System.printBln(e6);
        System.printBln(e7);
        System.printBln(e8);
        System.printBln(e9);
        System.printBln(e10);
        System.printBln(e11);
        System.printBln(e12);
        System.printBln(e13);
        System.println();
    }
}