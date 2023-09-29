///[SinErrores]
class One {}
class Two {}
class Three {}

class Parent<A, B, C> {}

class ChildOne<D, E, F> extends Parent<D, E, F> {}
class ChildTwo<D, E> extends Parent<D, E, One> {}
class ChildThree<D> extends Parent<D, Two, One> {}
class ChildFour extends Parent<Three, Two, One> {}

class ChildFive<T> extends Parent<Three, Two, One> {}

class Main {
    public static void main() {}
}