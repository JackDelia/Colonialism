package com.jackdelia.colonialism.math

/**
 * Wrapper for the Random Class (better quality random numbers are
 *          generated when sharing an instance)
 */
class RandomSingleton {

    private static final RandomSingleton instance = new RandomSingleton()

    Random randomInstance

    static RandomSingleton getInstance() {
        return instance
    }

    /**
     * Fetches the Instance of java.util.random
     * @return java.util.random instance
     */
    Random getRandomInstance() {
        if(randomInstance == null) {
            randomInstance = new Random()
        }

        return randomInstance
    }

}
