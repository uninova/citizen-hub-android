package pt.uninova.s4h.citizenhub.util;

import java.util.Objects;

public final class Triple<F, S, T> {

    private final F first;
    private final S second;
    private final T third;

    public Triple(F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    //settings page
    //estilos
    //Settings shared preferences settings.workhours.
    //Check para ver se tá a trabalhar 0,1. Migrações

    //3 fases: pagina settings, carregar e descarregar
    //base de dados com campo adicional
    //onde coleccionamos os dados ler a informação e escrever no campo
    //notificar o measurementRepository

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Triple)) {
            return false;
        }

        final Triple<?, ?, ?> other = (Triple<?, ?, ?>) obj;

        return first.equals(other.first) && second.equals(other.second) && third.equals(other.third);
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    public T getThird() {
        return third;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }

    @Override
    public String toString() {
        return "(" + first + "," + second + "," + third + ")";
    }
}
