package com;

import java.net.URL;

public class Test {
    public Test() {
        URL url = this.getClass().getResource("\\test");
        System.out.println(url.toString());
    }
}
