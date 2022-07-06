package org.example.web.hw15.context;

import org.example.web.hw15.exception.NoSuchBeanException;
import org.example.web.hw15.exception.NoUniqueBeanException;

import java.util.Map;

public interface ApplicationContext {
    /**
     *
     * @param beanType
     * @return a bean by its type
     * @param <T>
     * @throws NoSuchBeanException if nothing is found
     * @throws NoUniqueBeanException if more than one bean is found
     *
     */
    public <T> T getBean(Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException;

    /**
     *
     * @param name
     * @param beanType
     * @return a bean by its name
     * @param <T>
     * @throws NoSuchBeanException if nothing is found
     */
    public <T> T getBean(String name, Class<T> beanType) throws NoSuchBeanException;

    /**
     *
     * @param beanType
     * @return a map of beans where the key is it’s name and the value is the bean.
     *      If there is no beans with provided type then returns an empty map
     * @param <T>
     */
    public <T> Map<String, T> getAllBeans(Class<T> beanType);
}
