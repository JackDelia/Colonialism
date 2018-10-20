package com.jackdelia.colonialism.math;


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
        return RandomSingleton.getInstance().getRandomInstance().nextBoolean();
    }

}
