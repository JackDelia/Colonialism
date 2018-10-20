package com.jackdelia.colonialism.resource

import spock.lang.Specification

class ResourceTest extends Specification {

    def "getPrice: Should Return the Correct Price"(Resource testResource, double expectedValue) {

        expect: "The Method's Return Value to Match the Expected Value"
            testResource.getPrice() == expectedValue

        where: "the Enum and Expected Value are provided"
            testResource        | expectedValue
             Resource.COTTON    | 0.09
             Resource.IRON      | 0.10
             Resource.STONE     | 0.08
             Resource.GOLD      | 1.00
             Resource.JEWELRY   | 1.20
             Resource.MEAT      | 0.10
             Resource.FISH      | 0.08
             Resource.GRAIN     | 0.07
             Resource.WEAPONS   | 0.20
             Resource.CLOTHING  | 0.15
             Resource.TOOLS     | 0.18
             Resource.SOLDIERS  | 0.40

    }

}
