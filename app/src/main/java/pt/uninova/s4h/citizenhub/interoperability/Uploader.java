package pt.uninova.s4h.citizenhub.interoperability;

import java.util.Collection;

public interface Uploader<T> {

    void upload(T content);

    void upload(Collection<T> content);

}
