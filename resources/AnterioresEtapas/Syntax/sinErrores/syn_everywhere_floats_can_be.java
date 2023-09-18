class FloatsClass {
    //float as attribute types
    static float f;
    float f2;

    //float as return type
    float f() {

    }

    //float as static return type
    static float f2() {

    }

    float f3() {
        //float literals in assignment
        var ff = 1.1;
    }

    //float keyword as first keyword in parameter list
    float f4(float ff) {
        ;
    }

    //float keyword in parameter list
    float f5(int x, float ff) {
        ;
    }

    float f6() {
        //float literals in different parts of an expression
        var x = 1.1 + 0.3E10 / ((((1.2222)))) + "hello";
    }

    float f7() {
        //float literals in actual arguments list
        return this.f2(1.1, 1.2, 1.3, 10, "hello", 'c', 0.E10);
    }
}

interface FloatsInterface {
    float f();
    static float f2();
    float f3(float ff);
    float f5(int x, float ff);
}