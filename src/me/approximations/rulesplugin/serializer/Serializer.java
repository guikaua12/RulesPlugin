package me.approximations.rulesplugin.serializer;

public interface Serializer<T> {
    String encode(T data);

    T decode(String data);
}
