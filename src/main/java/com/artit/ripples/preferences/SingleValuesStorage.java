package com.artit.ripples.preferences;

import org.apache.log4j.Logger;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * </br>Saves parameters to branch {@link ListValuesStorage#PREFERENCES_PROGRAM_PATH} and selected node.
 */
public class SingleValuesStorage {
    private static final Logger LOG = Logger.getLogger(SingleValuesStorage.class);

    private String subNodeName;
    private Preferences node;

    /**
     * @param subNodeName 'hostAndPorts', for example
     */
    SingleValuesStorage(String subNodeName) {
//        TODO validation of subNodeName and valueKey
        this.subNodeName = subNodeName;
        initNode();
    }

    private void initNode() {
        Preferences root = Preferences.userRoot();
        node = root.node(ListValuesStorage.PREFERENCES_PROGRAM_PATH + "/" + subNodeName);
        try {
            node.flush();
        } catch (BackingStoreException e) {
            LOG.error(e, e);
        }
    }

    /**
     * overrides existing value if key names the same
     */
    public void addValue(String key, String value) {
        node.put(key, value);
        try {
            node.flush();
        } catch (BackingStoreException e) {
            LOG.error(e, e);
        }
    }

    /**
     * @return null if no value with given key
     */
    public String getValue(String key) {
        try {
            node.sync();
        } catch (BackingStoreException e) {
            LOG.error(e, e);
        }
        return node.get(key, null);
    }

    public void removeValue(String key) {
        node.remove(key);
    }

    public boolean isValueExist(String key) {
        return getValue(key) != null;
    }

    public void removeNode() {
        try {
            node.removeNode();
        } catch (BackingStoreException e) {
            LOG.error(e, e);
        }
    }

    public void printOut(java.io.OutputStream os) {
        try {
            node.exportNode(os);
        } catch (Exception e) {
            LOG.error(e, e);
        }
    }

}
