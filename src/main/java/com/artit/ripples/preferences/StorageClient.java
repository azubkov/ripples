package com.artit.ripples.preferences;

public class StorageClient {
    private StorageClient() {
    }

    public static final String MAIN_WINDOW_HEIGHT = "MAIN_WINDOW_HEIGHT";
    public static final String MAIN_WINDOW_WIDTH = "MAIN_WINDOW_WIDTH";
    public static final String X_MAIN_WINDOW_POSITION = "X_MAIN_WINDOW_POSITION";
    public static final String Y_MAIN_WINDOW_POSITION = "Y_MAIN_WINDOW_POSITION";

    public static final String CONDITION_WINDOW_HEIGHT = "CONDITION_WINDOW_HEIGHT";
    public static final String CONDITION_WINDOW_WIDTH = "CONDITION_WINDOW_WIDTH";
    public static final String X_CONDITION_WINDOW_POSITION = "X_CONDITION_WINDOW_POSITION";
    public static final String Y_CONDITION_WINDOW_POSITION = "Y_CONDITION_WINDOW_POSITION";

    public static final String TEXTINFO_WINDOW_HEIGHT = "TEXTINFO_WINDOW_HEIGHT";
    public static final String TEXTINFO_WINDOW_WIDTH = "TEXTINFO_WINDOW_WIDTH";
    public static final String X_TEXTINFO_WINDOW_POSITION = "X_TEXTINFO_WINDOW_POSITION";
    public static final String Y_TEXTINFO_WINDOW_POSITION = "Y_TEXTINFO_WINDOW_POSITION";

    public static final String TREE_WINDOW_HEIGHT = "TREE_WINDOW_HEIGHT";
    public static final String TREE_WINDOW_WIDTH = "TREE_WINDOW_WIDTH";
    public static final String X_TREE_WINDOW_POSITION = "X_TREE_WINDOW_POSITION";
    public static final String Y_TREE_WINDOW_POSITION = "Y_TREE_WINDOW_POSITION";

    /*more keys see in places, where StorageClient used (internal fields in dynamic gui classes : LoadType, etc)*/


    //    TODO default value, maybe, not here
    public static final ListValuesStorage remoteHostsAndPorts = new ListValuesStorage("remoteHostsAndPorts", "rHostPort");
    public static final ListValuesStorage localPorts = new ListValuesStorage("localPorts", "lPort");
    public static final SingleValuesStorage generalData = new SingleValuesStorage("generalData");

    static {
        if (remoteHostsAndPorts.getDefaultValue() == null) {
            remoteHostsAndPorts.setDefaultValue("abc://localhost.artit:8090");
        }

        if (localPorts.getDefaultValue() == null) {
            localPorts.setDefaultValue("5555");
        }

        if (!generalData.isValueExist(MAIN_WINDOW_HEIGHT)) {
            generalData.addValue(MAIN_WINDOW_HEIGHT, String.valueOf(850));
        }

        if (!generalData.isValueExist(MAIN_WINDOW_WIDTH)) {
            generalData.addValue(MAIN_WINDOW_WIDTH, String.valueOf(1100));
        }

        if (!generalData.isValueExist(X_MAIN_WINDOW_POSITION)) {
            generalData.addValue(X_MAIN_WINDOW_POSITION, String.valueOf(20));
        }

        if (!generalData.isValueExist(Y_MAIN_WINDOW_POSITION)) {
            generalData.addValue(Y_MAIN_WINDOW_POSITION, String.valueOf(10));
        }

        if (!generalData.isValueExist(CONDITION_WINDOW_HEIGHT)) {
            generalData.addValue(CONDITION_WINDOW_HEIGHT, String.valueOf(800));
        }

        if (!generalData.isValueExist(CONDITION_WINDOW_WIDTH)) {
            generalData.addValue(CONDITION_WINDOW_WIDTH, String.valueOf(900));
        }

        if (!generalData.isValueExist(X_CONDITION_WINDOW_POSITION)) {
            generalData.addValue(X_CONDITION_WINDOW_POSITION, String.valueOf(20));
        }

        if (!generalData.isValueExist(Y_CONDITION_WINDOW_POSITION)) {
            generalData.addValue(Y_CONDITION_WINDOW_POSITION, String.valueOf(10));
        }

        if (!generalData.isValueExist(TEXTINFO_WINDOW_HEIGHT)) {
            generalData.addValue(TEXTINFO_WINDOW_HEIGHT, String.valueOf(900));
        }

        if (!generalData.isValueExist(TEXTINFO_WINDOW_WIDTH)) {
            generalData.addValue(TEXTINFO_WINDOW_WIDTH, String.valueOf(800));
        }

        if (!generalData.isValueExist(X_TEXTINFO_WINDOW_POSITION)) {
            generalData.addValue(X_TEXTINFO_WINDOW_POSITION, String.valueOf(20));
        }

        if (!generalData.isValueExist(Y_TEXTINFO_WINDOW_POSITION)) {
            generalData.addValue(Y_TEXTINFO_WINDOW_POSITION, String.valueOf(10));
        }

        if (!generalData.isValueExist(TREE_WINDOW_HEIGHT)) {
            generalData.addValue(TREE_WINDOW_HEIGHT, String.valueOf(900));
        }

        if (!generalData.isValueExist(TREE_WINDOW_WIDTH)) {
            generalData.addValue(TREE_WINDOW_WIDTH, String.valueOf(800));
        }

        if (!generalData.isValueExist(X_TREE_WINDOW_POSITION)) {
            generalData.addValue(X_TREE_WINDOW_POSITION, String.valueOf(20));
        }

        if (!generalData.isValueExist(Y_TREE_WINDOW_POSITION)) {
            generalData.addValue(Y_TREE_WINDOW_POSITION, String.valueOf(10));
        }

    }
}
