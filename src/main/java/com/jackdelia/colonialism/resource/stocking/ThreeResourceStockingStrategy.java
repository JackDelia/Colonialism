package com.jackdelia.colonialism.resource.stocking;

import com.jackdelia.colonialism.location.Location;
import com.jackdelia.colonialism.resource.Resource;
import com.jackdelia.colonialism.resource.ResourceCollection;

public class ThreeResourceStockingStrategy implements IResourceStockingStrategy {
    

    @Override
    public void stockResources(Resource[][][] mapResources, ResourceCollection naturalResources, Location location) {
                
        mapResources[location.getXValue()][location.getYValue()] = new Resource[3];
        mapResources[location.getXValue()][location.getYValue()][0] = naturalResources.getRandomResource();
        mapResources[location.getXValue()][location.getYValue()][1] = naturalResources.getRandomResource();

        while(mapResources[location.getXValue()][location.getYValue()][0] == mapResources[location.getXValue()][location.getYValue()][1]) {
            mapResources[location.getXValue()][location.getYValue()][1] = naturalResources.getRandomResource();
        }

        mapResources[location.getXValue()][location.getYValue()][2] = naturalResources.getRandomResource();

        while((mapResources[location.getXValue()][location.getYValue()][0] == mapResources[location.getXValue()][location.getYValue()][2])
                || (mapResources[location.getXValue()][location.getYValue()][1] == mapResources[location.getXValue()][location.getYValue()][2])) {
            mapResources[location.getXValue()][location.getYValue()][2] = naturalResources.getRandomResource();
        }
    }
}
