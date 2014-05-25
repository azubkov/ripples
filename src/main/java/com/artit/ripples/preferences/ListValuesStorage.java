package com.artit.ripples.preferences;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * </br>Saves parameters to branch {@link ListValuesStorage#PREFERENCES_PROGRAM_PATH} and selected node.
 * Works with list of params.
 */
public class ListValuesStorage {
    private static final Logger LOG = Logger.getLogger(ListValuesStorage.class);
    public static final String PREFERENCES_PROGRAM_PATH = "com/artit/ripples/gui/preferences";

    private static final String NUMBER_SIGN = "#";
    private static final String DEFAULT_VALUE_NUM_KEY = "DefaultValueNum";
    private static final String NUMBER_OF_VALUES_KEY = "ValuesNumber";
    private static final String NOT_RESTORED_STUB = "?notRestoredStub?";

    private String valueKey;
    private String subNodeName;

    private Preferences node;

    /**
     * @param subNodeName 'hostAndPorts', for example
     * @param valueKey    'hostNport' for example
     */
    ListValuesStorage(String subNodeName, String valueKey) {
//        TODO validation of subNodeName and valueKey
        this.subNodeName = subNodeName;
        this.valueKey = valueKey;
        initNode();
    }

    private void initNode() {
        Preferences root = Preferences.userRoot();
        node = root.node(PREFERENCES_PROGRAM_PATH + "/" + subNodeName);
        try {
            node.flush();
        } catch (BackingStoreException e) {
            LOG.error(e, e);
        }
    }

    /**
     * @return order number
     */
    public int addValue(String value) {
        int valuesNumber = node.getInt(NUMBER_OF_VALUES_KEY, 0);
        node.put(generateKey(valuesNumber), value);
        node.putInt(NUMBER_OF_VALUES_KEY, valuesNumber + 1);
        try {
            node.flush();
        } catch (BackingStoreException e) {
            LOG.error(e, e);
        }
        return valuesNumber;
    }

    /**
     * @param value if null - default param be removed
     */
    public void setDefaultValue(String value) {
//        LOG.debug("req for set to default : ["+value+"]");
        if (value == null) {
            node.remove(DEFAULT_VALUE_NUM_KEY);
            try {
                node.flush();
            } catch (BackingStoreException e) {
                LOG.error(e, e);
            }
//            LOG.debug("def value num removed at all");
            return;
        }
        List<String> values = getValues();

        for (int i = 0; i < values.size(); i++) {
            String s = values.get(i);
            if (s.equals(value)) {
                node.putInt(DEFAULT_VALUE_NUM_KEY, i);
                try {
                    node.flush();
                } catch (BackingStoreException e) {
                    LOG.error(e, e);
                }
//                LOG.debug("def value already exist, num set to ["+i+"]");
                return;
            }
        }

        int number = addValue(value);
//        LOG.debug("def value added as # ["+number+"]");
        node.putInt(DEFAULT_VALUE_NUM_KEY, number);
        try {
            node.flush();
        } catch (BackingStoreException e) {
            LOG.error(e, e);
        }
    }

    /**
     * @return null if storage not accessible or default not set
     */
    public String getDefaultValue() {
        try {
            node.sync();
        } catch (BackingStoreException e) {
            LOG.error(e, e);
        }
        int defaultNum = node.getInt(DEFAULT_VALUE_NUM_KEY, -3200);
        return getValue(defaultNum);
    }

    public List<String> getValues() {
        try {
            node.sync();
        } catch (BackingStoreException e) {
            LOG.error(e, e);
        }
        int valuesNumber = node.getInt(NUMBER_OF_VALUES_KEY, 0);
//        LOG.debug("valuesNumber : ["+valuesNumber+"]");
        List<String> values = new ArrayList<String>();
        for (int i = 0; i < valuesNumber; i++) {
            values.add(getValue(i));
        }

//        LOG.debug("def vals : ");
//        for (int i = 0; i < values.size(); i++) {
//             LOG.debug("val["+i+"]:["+values.get(i)+"]");
//        }

        return values;
    }
//TODO junit test required

    public void removeAllValues() {
        List<String> values = getValues();
        for (String value : values) {
            removeValue(value);
        }
        node.putInt(NUMBER_OF_VALUES_KEY, 0);
        setDefaultValue(null);
        try {
            node.flush();
        } catch (BackingStoreException e) {
            LOG.error(e, e);
        }
    }

    public boolean isValueExist(String value) {
        List<String> values = getValues();
        return values.contains(value);
    }

    /**
     * Rmoves value, if it is default - set default to another, if no another - set default to null
     *
     * @param value null not premitted
     */
    public void removeValue(String value) {
        if (value == null) {
            return;
        }
        if (!isValueExist(value)) {
            return;
        }
        //key finding
        int removeValueNum = -1;
        List<String> values = getValues();
        for (int i = 0; i < values.size(); i++) {
            String s = values.get(i);
            if (s.equals(value)) {
                removeValueNum = i;
                values.remove(value);
                break;
            }
        }

        if (removeValueNum < 0) {
            return;
        }

        if (value.equals(getDefaultValue())) {
            if (values.size() != 0) {
                setDefaultValue(values.get(0));
            } else {
                setDefaultValue(null);
            }
        }

        node.remove(generateKey(removeValueNum));

        if (values.size() == 0) {
        } else {

            int lastNumber = values.size();
            if (lastNumber != removeValueNum) {
                String lastValue = values.get(values.size() - 1);
                node.remove(generateKey(lastNumber));
                node.put(generateKey(removeValueNum), lastValue);
            }
        }
        node.putInt(NUMBER_OF_VALUES_KEY, values.size());
        try {
            node.flush();
        } catch (BackingStoreException e) {
            LOG.error(e, e);
        }
    }

    /**
     * @return null, if num<0
     */
    private String getValue(int num) {
        if (num < 0) {
            return null;
        }
        return node.get(generateKey(num), NOT_RESTORED_STUB);
    }

    private String generateKey(int num) {
        return valueKey + NUMBER_SIGN + String.valueOf(num);
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
