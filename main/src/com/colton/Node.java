package com.colton;

import java.awt.*;

/**
 * Created by colton on 7/3/15.
 */
public class Node {
    private final Point m_location;

    public Node(){
        m_location = new Point();
    }

    public Node(int x, int y) {
        m_location = new Point(x, y);
    }

    public final Point getLocation() {
        return m_location;
    }
}
