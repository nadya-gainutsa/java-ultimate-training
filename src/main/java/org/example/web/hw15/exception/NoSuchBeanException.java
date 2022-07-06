package org.example.web.hw15.exception;

/**
 * This is a custom exception which throws when bean was not found in the context
 */
public class NoSuchBeanException extends Exception {
    public NoSuchBeanException() {
        super("Bean with provided type not found!");
    }
}
