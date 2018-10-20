package com.jackdelia.colonialism.resource

import spock.lang.Specification

class ResourceFactoryTest extends Specification {

    def "getInstance: Should Return Instance of ResourceFactory"() {

        when: "The Instance is Retrieved"
            def factoryInstance = ResourceFactory.getInstance()

        then: "it's type should be of ResourceFactory"
            factoryInstance.getClass() == ResourceFactory.class
    }

    def "getNaturalResources: Should Include the Correct Resource Types"() {

        when: "The Natural Resource Are Retrieved"
            def naturalResource = ResourceFactory.getInstance().getNaturalResources()

        then: "Gold should be included"
            naturalResource.contains(Resource.GOLD)

        and: "Stone should be included"
            naturalResource.contains(Resource.STONE)

        and: "Iron should be included"
            naturalResource.contains(Resource.IRON)
    }

    def "getAdvancedResources: Should Include the Correct Resource Types"() {

        when: "The Advanced Resource Are Retrieved"
            def advancedResource = ResourceFactory.getInstance().getAdvancedResources()

        then: "Weapons should be included"
            advancedResource.contains(Resource.WEAPONS)

        and: "Iron should be included"
            advancedResource.contains(Resource.IRON)

        and: "Soldiers should be included"
            advancedResource.contains(Resource.SOLDIERS)

        and: "Tools should be included"
            advancedResource.contains(Resource.TOOLS)

        and: "Stone should be included"
            advancedResource.contains(Resource.STONE)

        and: "Clothing should be included"
            advancedResource.contains(Resource.CLOTHING)

        and: "Cotton should be included"
            advancedResource.contains(Resource.COTTON)

        and: "Jewelery should be included"
            advancedResource.contains(Resource.JEWELRY)

        and: "Gold should be included"
            advancedResource.contains(Resource.GOLD)
    }



}
