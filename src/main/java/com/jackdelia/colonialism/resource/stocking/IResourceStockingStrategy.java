package com.jackdelia.colonialism.resource.stocking;

import com.jackdelia.colonialism.location.Location;
import com.jackdelia.colonialism.resource.Resource;
import com.jackdelia.colonialism.resource.ResourceCollection;

@FunctionalInterface
public interface IResourceStockingStrategy {

    void stockResources(Resource[][][] mapResources, ResourceCollection naturalResources, Location location);

}
