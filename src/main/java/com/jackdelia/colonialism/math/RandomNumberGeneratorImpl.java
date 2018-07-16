package com.jackdelia.colonialism.math;

import java.util.Random;

/**
 * Implementation of Random Number Generation Logic
 */
public class RandomNumberGeneratorImpl implements IRandomNumberGenerator {

    /**
     * Default Constructor
     */
    RandomNumberGeneratorImpl(){}

    @Override
    public double generate() {
        return Math.random();
    }

    @Override
    public int generate(int maxValue) {
        return new Random().nextInt(maxValue);
    }
}
