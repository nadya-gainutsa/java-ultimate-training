package org.example.web.hw15.demo.service;

import org.example.web.hw15.annotation.LikeBean;

@LikeBean
public class Circle implements IShape {
    @Override
    public void draw() {
        System.out.println("Drawing circle...");
    }
}
