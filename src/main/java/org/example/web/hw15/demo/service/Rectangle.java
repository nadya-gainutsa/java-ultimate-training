package org.example.web.hw15.demo.service;

import org.example.web.hw15.annotation.LikeBean;

@LikeBean("longSquare")
public class Rectangle extends Square {
    @Override
    public void draw() {
        System.out.println("Drawing rectangle...");
    }
}
