class Generics {
    static void printTree(Tree<E> t) {

    }

    static void usePrintTree() {
        printTree(new Tree<Integer>());
        printTree(new Tree<>());
    }
}