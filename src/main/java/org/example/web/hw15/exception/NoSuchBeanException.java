package org.example.web.hw15.exception;

/**
 * This is a custom exception which throws when bean was not found in the context
 */
public class NoSuchBeanException extends Exception {
    public NoSuchBeanException(String typeOrName) {
        super("Bean with provided type or name [" + typeOrName + "] not found!");
    }
}
