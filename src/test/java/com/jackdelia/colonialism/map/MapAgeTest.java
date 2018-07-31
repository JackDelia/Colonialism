package com.jackdelia.colonialism.map;

import org.junit.Test;

import static org.junit.Assert.*;

public class MapAgeTest {

    @Test
    public void ageShouldInitializeWithZero(){

        MapAge testMapAge = new MapAge();

        assertEquals(0, testMapAge.getAge());
    }

    @Test
    public void incrementAgeShouldIncreaseByOne(){

        MapAge testMapAge = new MapAge();

        int expectedAge = 5;

        for(int i = 0; i < expectedAge; i++){
            testMapAge.incrementAge();
        }

        assertEquals(expectedAge, testMapAge.getAge());
    }

    @Test
    public void isInNormalAgeShouldReturnTrueForAfterDarkAge(){

        MapAge testMapAge = new MapAge();

        int afterDarkAge = 50000;

        for(int i = 0; i < afterDarkAge; i++){
            testMapAge.incrementAge();
        }

        assertEquals(true, testMapAge.isInNormalAge());
    }

    @Test
    public void isInNormalAgeShouldReturnTrueForBeforeDarkAge(){

        MapAge testMapAge = new MapAge();

        int beforeDarkAge = 50;

        for(int i = 0; i < beforeDarkAge; i++){
            testMapAge.incrementAge();
        }

        assertEquals(true, testMapAge.isInNormalAge());
    }

    @Test
    public void isInNormalAgeShouldReturnFalseDuringDarkAge(){

        MapAge testMapAge = new MapAge();

        int darkAge = 6800;

        for(int i = 0; i < darkAge; i++){
            testMapAge.incrementAge();
        }

        assertEquals(false, testMapAge.isInNormalAge());
    }


}