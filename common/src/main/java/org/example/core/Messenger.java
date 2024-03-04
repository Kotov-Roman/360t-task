package org.example.core;

/**
 * Just an interface showing that whoever implements it can read and send information
 */
public interface Messenger {

    String read();

    void send(String message);
}
