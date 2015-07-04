package com.colton;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by colton on 7/3/15.
 */
public final class Constants {
    // Possible directions
    public static final String DIRECTION_EAST = "east";
    public static final String DIRECTION_SOUTH = "south";
    public static final String DIRECTION_WEST = "west";
    public static final String DIRECTION_NORTH = "north";

    // possible attack formations
    public static final String ACTION_ALPHA = "alpha";
    public static final String ACTION_BETA = "beta";
    public static final String ACTION_GAMMA = "gamma";
    public static final String ACTION_DELTA = "delta";

    // lists of directions and attacks for convenience
    public static final List<String> DIRECTION_LIST = Collections.unmodifiableList(Arrays.asList(DIRECTION_EAST, DIRECTION_SOUTH, DIRECTION_WEST, DIRECTION_NORTH));
    public static final List<String> ACTION_LIST = Collections.unmodifiableList(Arrays.asList(ACTION_ALPHA, ACTION_BETA, ACTION_GAMMA, ACTION_DELTA));

    // no one should be able to instantiate this class
    private Constants() {}
}
