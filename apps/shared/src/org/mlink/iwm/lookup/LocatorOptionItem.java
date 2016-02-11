package org.mlink.iwm.lookup;

/**
 * Created by IntelliJ IDEA.
 * User: andrei
 * Date: Jul 29, 2004
 * Time: 2:28:55 PM
 * To change this template use Options | File Templates.
 */
public class LocatorOptionItem extends TreeOptionItem{
    private String fullLocator;

    public String getFullLocator() {
        return fullLocator;
    }

    public LocatorOptionItem(Long value, String label, Long parentId, Integer schemaId, String fullLocator) {
        super(value, label,parentId,schemaId);
        this.fullLocator = fullLocator;
    }
}
