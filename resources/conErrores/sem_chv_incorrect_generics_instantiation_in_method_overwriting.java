///[Error:t|9]
class Tree<A> {}

class P<E> {
    Tree<E> t() {}
}

class H<E> extends P<E> {
    Tree<String> t() {}
}

class Main {
    public static void main() {}
}