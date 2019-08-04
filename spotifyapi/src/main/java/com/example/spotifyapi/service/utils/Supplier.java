package com.example.spotifyapi.service.utils;

@FunctionalInterface
public interface Supplier<T> {
    T get();
}
