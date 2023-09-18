interface I1 {
    int i1m1();
    static void i1m2();
}

class C1 {
    int x;
}

interface I2 {
    char i2m1();
    static String i2m2();
}

class C2 implements I2 {
    char i2m1() {
        return 'c';
    }

    static String i2m2() {
        //do stuff!!
    }
}