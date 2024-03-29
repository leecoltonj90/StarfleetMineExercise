package com.colton;

/**
 * Created by colton on 7/3/15.
 */
public class Mine extends Node {
    private int m_distance;
    private boolean m_isAlive = true;

    public Mine(int x, int y, char z) {
        super(x, y);
        m_distance = convertToDistance(z);
    }

    /**
     * Find the integer representation of the Z-axis coordinate
     * @param zIndex that character representation of the coordinate
     * @return the positive integer representation of the Z-axis/depth, -1 if invalid character
     */
    private int convertToDistance(char zIndex) {
        int distance = 0;
        if(Character.isAlphabetic(zIndex)) {
            if(zIndex >= 97) { //lowercase
                distance = zIndex - 96; // ascii(a == 97); sim(a == 1)
            } else { //uppercase
                distance = zIndex - (64 - 26); // ascii(A == 65); sim(A == 27)
            }
        } else {
            distance = -1;
        }
        return distance;
    }

    /**
     * Find the character representation of mines z-axis
     * @return an alphabetical character representation of the mines position, * if the mine is behind the ship
     */
    public char getDisplayDistance() {
        if (m_distance <= 0) { //passed mine
            return '*';
        }else if (m_distance < 27) { //lowercase
            return (char) (m_distance + 96);
        } else  { //uppercase
            return (char) (m_distance - 26 + 64);
        }
    }

    /**
     * Find if a mine has yet to be destroyed by the ship
     * @return true if the mine has not already been destroyed, false otherwise
     */
    public boolean isAlive() {
        if (m_isAlive) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * Mark the mine as destroyed by one of the ships firing patterns
     */
    public void destroy() {
        m_isAlive = false;
    }

    /**
     * Find the z-axis value of the mine relative to the ships current depth
     * @return the integer value of the mines current depth
     */
    public int getDistance() {
        return m_distance;
    }

    /**
     * Decrement the mines depth position as the ship "sinks" closer
     */
    public void survivedWave() {
        m_distance--;
    }

    /**
     * Determine if the mine has been passed without being destroyed by a missle volley
     * @return true if the mine is still active and its z-axis depth relative to the ship has reached 0, false otherwise
     */
    public boolean didMineExplode() {
        return isAlive() && m_distance <= 0;
    }
}
