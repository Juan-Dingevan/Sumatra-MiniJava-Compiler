///[Error:m1|7]
class C {
    int m1(int x, C c) {}
}

class D extends C {
    int m1(C parametroLegacy, int numeroQueSeUtilizaParaResolverElHanltingProblem) {}
}

class Main {
    public static void main() {}
}