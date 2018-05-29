package com.ksatstuttgart.usoc.gui.setup.configuration;

/**
 * Represents a Pane Component that can be parsed into a JSON file
 */
public interface Parsable {

    /**
     * Set all defined properties to a POJO Class
     * @param pojoClass POJO Class to set properties
     */
    void writeToPOJO(GeneralProperties pojoClass);
}
