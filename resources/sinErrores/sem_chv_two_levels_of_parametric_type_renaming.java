///[SinErrores]
class Abuelo<X> {
    X metodoAbuelo() {}
}

class Padre<Y> extends Abuelo<Y> {
    Y metodoPadre() {}
}

class Hija<Z> extends Padre<Z> {
    Z metodoAbuelo() {}
    Z metodoPadre() {}
    Z metodoHija() {}
}