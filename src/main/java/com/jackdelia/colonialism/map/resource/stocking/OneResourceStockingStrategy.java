package com.jackdelia.colonialism.map.resource.stocking;

import com.jackdelia.colonialism.location.Location;
import com.jackdelia.colonialism.map.resource.Resource;
import com.jackdelia.colonialism.map.resource.ResourceCollection;

public class OneResourceStockingStrategy implements IResourceStockingStrategy {

    @Override
    public void stockResources(Resource[][][] mapResources, ResourceCollection naturalResources, Location location) {
        mapResources[location.getXValue()][location.getYValue()] = new Resource[1];
        Resource res = naturalResources.getRandomResource();
        if(res != Resource.IRON) {
            res = naturalResources.getRandomResource();
        }
        mapResources[location.getXValue()][location.getYValue()][0] = res;
    }

}
