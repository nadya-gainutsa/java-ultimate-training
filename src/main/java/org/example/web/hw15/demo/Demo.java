package org.example.web.hw15.demo;

import lombok.SneakyThrows;
import org.example.web.hw15.context.ApplicationContextImpl;
import org.example.web.hw15.demo.service.Circle;
import org.example.web.hw15.demo.service.Rectangle;
import org.example.web.hw15.demo.service.Square;
import org.example.web.hw15.demo.service.Triangle;
import org.example.web.hw15.exception.NoSuchBeanException;

public class Demo {
    @SneakyThrows
    public static void main(String[] args) {
        var context = new ApplicationContextImpl("org.example");
        var circleService = context.getBean(Circle.class);
        circleService.draw();

        try {
            var triangle = context.getBean(Triangle.class);
            triangle.draw();
        } catch (NoSuchBeanException e) {
            System.out.println(e.getMessage());
        }

        var rectangle = context.getBean("longSquare", Rectangle.class);
        rectangle.draw();

        context.getAllBeans(Square.class).values().forEach(Square::draw);

        context.getAllBeans(Triangle.class).values().forEach(Triangle::draw);

    }
}
