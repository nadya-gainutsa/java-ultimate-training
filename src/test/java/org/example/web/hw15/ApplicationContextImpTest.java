package org.example.web.hw15;

import lombok.SneakyThrows;
import org.example.web.hw15.context.ApplicationContext;
import org.example.web.hw15.context.ApplicationContextImpl;
import org.example.web.hw15.demo.service.Circle;
import org.example.web.hw15.demo.service.Rectangle;
import org.example.web.hw15.demo.service.Square;
import org.example.web.hw15.demo.service.Triangle;
import org.example.web.hw15.exception.NoSuchBeanException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class ApplicationContextImpTest {

    private ApplicationContext applicationContext = new ApplicationContextImpl("org.example");

    @SneakyThrows
    @Test
    public void isAnnotatedBeanInContextTest() {
        assertNotNull(applicationContext.getBean(Circle.class));
    }

    @SneakyThrows
    @Test
    public void isNotAnnotatedInContextTest() {
        assertThrows(NoSuchBeanException.class, () -> applicationContext.getBean(Triangle.class));
    }

    @SneakyThrows
    @Test
    public void getBeanByTypeTest() {
        assertNotNull(applicationContext.getBean(Rectangle.class));
    }


    @Test
    @Ignore
    public void getBeanByTypeThrowsExceptionWhenFoundMoreThanOneBeanTest() {

    }

    @SneakyThrows
    @Test
    public void getBeanByNameAndTypeTest() {
        assertNotNull(applicationContext.getBean("longSquare", Rectangle.class));
    }

    @Test
    public void getBeanByNameAndTypeThrowsExceptionTest() {
        assertThrows(NoSuchBeanException.class, () -> applicationContext.getBean("rectangle", Rectangle.class));
    }

    @Test
    public void getAllBeansTest() {
        var beansMap = applicationContext.getAllBeans(Square.class);
        assertEquals(1, beansMap.size());
    }
}
