package com.jackdelia.colonialism.resource;


/**
 * <p>Factory for Creating Resources</p>
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
public class ResourceFactory {

    private static ResourceFactory instance;

    /**
     * Defeat Instantiation
     */
    private ResourceFactory(){}

    /**
     * Singleton Accessor
     *
     * @return instance of the ResourceFactory
     */
    public static ResourceFactory getInstance() {
        if(instance == null) {
            instance = new ResourceFactory();
        }
        return instance;
    }

    /**
     * Returns a list of Natural Resources
     *
     * @return Array of Natural Resources
     */
    public ResourceCollection getNaturalResources() {
        return new ResourceCollection(new Resource[]{Resource.GOLD, Resource.STONE, Resource.IRON});
    }

    /**
     * Returns a list of Natural Resources
     *
     * @return Array of Natural Resources
     */
    public ResourceCollection getAdvancedResources() {
        return new ResourceCollection(new Resource[]{
                Resource.WEAPONS,
                Resource.IRON,
                Resource.SOLDIERS,
                Resource.TOOLS,
                Resource.STONE,
                Resource.CLOTHING,
                Resource.COTTON,
                Resource.JEWELRY,
                Resource.GOLD
        });
    }
}
