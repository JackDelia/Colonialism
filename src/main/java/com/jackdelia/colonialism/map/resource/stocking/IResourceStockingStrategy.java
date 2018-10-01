package com.jackdelia.colonialism.map.resource.stocking;

import com.jackdelia.colonialism.location.Location;
import com.jackdelia.colonialism.map.resource.Resource;
import com.jackdelia.colonialism.map.resource.ResourceCollection;

@FunctionalInterface
public interface IResourceStockingStrategy {

    void stockResources(Resource[][][] mapResources, ResourceCollection naturalResources, Location location);

}
