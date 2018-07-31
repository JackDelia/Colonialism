package com.jackdelia.colonialism.math;

import java.util.Random;

/**
 * Implementation of Random Boolean Generation Logic
 */
public class RandomBooleanGeneratorImpl implements IRandomBooleanGenerator {

    /**
     * Default Constructor
     */
    RandomBooleanGeneratorImpl(){}

    @Override
    public boolean generate() {
        return new Random().nextBoolean();
    }

}
