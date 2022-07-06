package org.example.web.hw15.demo.service;

import org.example.web.hw15.annotation.LikeBean;

@LikeBean
public class Square implements IShape {
    @Override
    public void draw() {
        System.out.println("Drawing square...");
    }
}
