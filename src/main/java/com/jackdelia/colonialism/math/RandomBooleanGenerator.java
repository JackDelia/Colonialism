package com.jackdelia.colonialism.math;

/**
 * Static Entry-Point to the Random Boolean Generator Code
 */
public class RandomBooleanGenerator {

    public static boolean generate(){
        return util().generate();
    }

    private static IRandomBooleanGenerator util(){
        return new RandomBooleanGeneratorImpl();
    }

}
