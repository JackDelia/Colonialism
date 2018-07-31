package com.jackdelia.colonialism.math;

/**
 * Static Entry-Point to the Random Number Generator Code
 */
public class RandomNumberGenerator {

    public static double generate(){
        return util().generate();
    }

    public static int generate(int maxValue){
        return util().generate(maxValue);
    }

    private static IRandomNumberGenerator util(){
        return new RandomNumberGeneratorImpl();
    }

}
