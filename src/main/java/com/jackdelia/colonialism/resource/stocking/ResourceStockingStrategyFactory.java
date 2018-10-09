package com.jackdelia.colonialism.resource.stocking;

import com.jackdelia.colonialism.location.Location;
import com.jackdelia.colonialism.resource.Resource;
import com.jackdelia.colonialism.resource.ResourceCollection;

import java.util.HashMap;
import java.util.Map;

public final class ResourceStockingStrategyFactory {

    private final Map<String, IResourceStockingStrategy> strategies;

    /**
     * Default Constructor
     */
    private ResourceStockingStrategyFactory() {
        strategies = new HashMap<>();
    }

    private void addStrategy(final String name, final IResourceStockingStrategy strategy) {
        strategies.put(name, strategy);
    }

    public void executeResourceStockingStrategy(String name, Resource[][][] mapResources, ResourceCollection naturalResources, Location location) {
        if(strategies.containsKey(name)) {
            strategies.get(name).stockResources(mapResources, naturalResources, location);
        } else {
            strategies.get("NULL").stockResources(mapResources, naturalResources, location);
        }
    }

    /**
     * Factory Pattern
     */
    public static ResourceStockingStrategyFactory init() {
        final ResourceStockingStrategyFactory factory;
        factory = new ResourceStockingStrategyFactory();

        factory.addStrategy("NULL", new NullResourceStockingStrategy());
        factory.addStrategy("ONE", new OneResourceStockingStrategy());
        factory.addStrategy("TWO", new TwoResourceStockingStrategy());
        factory.addStrategy("THREE", new ThreeResourceStockingStrategy());
        factory.addStrategy("MOUNTAINS", new MountainsResourceStockingStrategy());

        return factory;
    }



}
