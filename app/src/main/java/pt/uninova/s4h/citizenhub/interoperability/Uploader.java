package pt.uninova.s4h.citizenhub.interoperability;

import java.util.Collection;

import okhttp3.Response;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public interface Uploader<T> {

    void upload(T content, Observer<Response> observer);

    void upload(Collection<T> content, Observer<Collection<Response>> observer);

}
