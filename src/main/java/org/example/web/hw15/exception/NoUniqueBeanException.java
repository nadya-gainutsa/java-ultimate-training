package org.example.web.hw15.exception;

/**
 * This is a custom exception which throws when more than one bean was found in the context
 */
public class NoUniqueBeanException extends Exception {
    public NoUniqueBeanException(String type) {
        super("More than one bean with provided type [" + "] was found!");
    }
}
