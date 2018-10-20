package com.jackdelia.colonialism.math;


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
        return RandomSingleton.getInstance().getRandomInstance().nextInt(maxValue);
    }
}
