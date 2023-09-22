///[SinErrores]
interface GenericInterface<E> {
    E parametricTypeReturnMethod();
    void parametricTypeParameter(E e);
    GenericInterface<E> parametizedInterfaceReturnType();
    GenericClass<E> parametizedClassReturnType();
    void parametizedInterfaceParameter(GenericInterface<E> gi);
    void parametizedClassParameter(GenericClass<E> gc);
}

class GenericClass<T> {

}

interface GenericDaughterInterface<E> extends GenericInterface<E> {

}

class GenericImplementation<T> implements GenericInterface<T> {
    T parametricTypeReturnMethod() {}
    void parametricTypeParameter(T e) {}
    GenericInterface<T> parametizedInterfaceReturnType() {}
    GenericClass<T> parametizedClassReturnType() {}
    void parametizedInterfaceParameter(GenericInterface<T> gi) {}
    void parametizedClassParameter(GenericClass<T> gc) {}
}

class GenericDaughterClass<T> extends GenericClass<T> {
    //MIEMBROS DE TIPO "TIPO PARAMETRICO"
    T t;

    T getT() {
        return t;
    }

    void setT(T t) {
        this.t = t;
    }
    //-------------------------------------
    //MIEMBROS DE TIPO "TIPO PARAMETRIZADO CLASE"
    GenericClass<T> gc;

    GenericClass<T> getGC() {
        return gc;
    }

    void setGC(GenericClass<T> gc) {
        this.gc = gc;
    }
    //-------------------------------------
    //MIEMBROS DE TIPO "TIPO PARAMETRIZADO INTERFAZ"
    GenericInterface<T> gi;

    GenericInterface<T> getGI() {
        return gi;
    }

    void setGI(GenericInterface<T> gi) {
        this.gi = gi;
    }
}