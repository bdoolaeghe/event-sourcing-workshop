package kafka;

public interface EventListener<T> {

    void on(T event);
}
