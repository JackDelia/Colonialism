package com.jackdelia.colonialism.math;

/**
 * Abstraction for the Generation of Random Numbers
 */
public interface IRandomNumberGenerator {

    double generate();

    int generate(int maxValue);

}
