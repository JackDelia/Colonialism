package com.jackdelia.colonialism.map.resource;


import com.jackdelia.colonialism.math.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>Information about a collection of {@link Resource}s</p>
 * <p>
 * Change Log:
 * </p>
 * <ul>
 *     <li>v0.5 - Initial File Creation. Extracted logic from {@link com.jackdelia.colonialism.map.Map}.</li>
 * </ul>
 *
 * @author <a href="mailto:andrewparise1994@gmail.com">Andrew Parise</a>
 * @since 0.5
 * @version 0.5
 */
public class ResourceCollection {

    private ArrayList<Resource> resources;

    /**
     * Default Constructor
     */
    public ResourceCollection(){}

    /**
     * Constructor including Resource Array
     *
     * @param resources resources to be encapsulated
     */
    public ResourceCollection(Resource[] resources) {
        this.resources = new ArrayList<>();
        this.resources.addAll(Arrays.asList(resources));
    }

    /**
     * Adds the resource to the Collection
     *
     * @param newResource to be added to the Collection
     */
    public void addResource(Resource newResource) {
        this.resources.add(newResource);
    }

    /**
     * Fetches the inner Resource Array
     *
     * @return Resource[] the inner resource array
     */
    public Resource[] getResources() {
        return (Resource[]) this.resources.toArray();
    }

    /**
     * Check if the {@param resource} is in the ResourceCollection
     *
     * @param resource the resource to check if it is in the collection
     * @return boolean true if it is in collection; false if not
     */
    public boolean contains(Resource resource){
        return this.resources.contains(resource);
    }

    /**
     * Fetches a random resource from the collection
     *
     * @return Resource a random item in the collection
     */
    public Resource getRandomResource() {
        return this.resources.get(RandomNumberGenerator.generate(this.resources.size()));
    }

}
