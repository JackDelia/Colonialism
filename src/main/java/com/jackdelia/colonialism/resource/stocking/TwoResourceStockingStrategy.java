package com.jackdelia.colonialism.resource.stocking;

import com.jackdelia.colonialism.location.Location;
import com.jackdelia.colonialism.resource.Resource;
import com.jackdelia.colonialism.resource.ResourceCollection;

public class TwoResourceStockingStrategy implements IResourceStockingStrategy {
    

    @Override
    public void stockResources(Resource[][][] mapResources, ResourceCollection naturalResources, Location location) {
        mapResources[location.getXValue()][location.getYValue()] = new Resource[2];
        Resource res1 = naturalResources.getRandomResource();
        Resource res2 = naturalResources.getRandomResource();

        if(res1 != Resource.IRON && res2 != Resource.IRON) {
            res1 = naturalResources.getRandomResource();
            res2 = naturalResources.getRandomResource();
        }

        mapResources[location.getXValue()][location.getYValue()][0] = res1;
        mapResources[location.getXValue()][location.getYValue()][1] = res2;
        while(mapResources[location.getXValue()][location.getYValue()][0] == mapResources[location.getXValue()][location.getYValue()][1]) {
            mapResources[location.getXValue()][location.getYValue()][1] = naturalResources.getRandomResource();
        }
    }
}
