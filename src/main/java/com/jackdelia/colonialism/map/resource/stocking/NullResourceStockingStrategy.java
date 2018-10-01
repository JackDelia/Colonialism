package com.jackdelia.colonialism.map.resource.stocking;

import com.jackdelia.colonialism.location.Location;
import com.jackdelia.colonialism.map.resource.Resource;
import com.jackdelia.colonialism.map.resource.ResourceCollection;

public class NullResourceStockingStrategy implements IResourceStockingStrategy {

    @Override
    public void stockResources(Resource[][][] mapResources, ResourceCollection naturalResources, Location location) {
        mapResources[location.getXValue()][location.getYValue()] = new Resource[0];
    }

}
