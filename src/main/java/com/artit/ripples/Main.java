package com.artit.ripples;

import com.artit.ripples.imposition.MassImgFilter;
import com.artit.ripples.imposition.linear.LinearMassImgFilter;
import com.artit.ripples.imposition.threaded.ThreadedMassImgFilter;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import javax.swing.*;

public class Main {
    public static final String CURRENT_DIRECTORY;
    private static String DEFAULT_WATER_MARK_IMAGE = "waterMark.png";

    static {
        String currentDirectory = System.getProperty("user.dir") + System.getProperty("file.separator");
        String resourcesDir = "N/A";
        try {
            URL url = Main.class.getResource("/");
            resourcesDir = url.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("resourcesDir is : " + resourcesDir);

        if (resourcesDir.contains("target/classes/".replaceAll("/", System.getProperty("file.separator")))) {
            currentDirectory += "src/main/resources/".replaceAll("/", System.getProperty("file.separator"));
        }
        System.err.println("currentDirectory is : " + currentDirectory);
        CURRENT_DIRECTORY = currentDirectory;
    }




    public static final MassImgFilter massImgFilter = new ThreadedMassImgFilter();

    public static void main(String[] args) {
        WaterMarkModel.setWaterMarkPath(CURRENT_DIRECTORY + DEFAULT_WATER_MARK_IMAGE);


        //todo over storage here
        WaterMarkModel.setImagesFindDirectory(CURRENT_DIRECTORY + "sample");
        ArtiMarkFrame.currentFolderTField.setText(WaterMarkModel.getImagesFindDirectory());
        ImageManager.imagesFinder.setDirectory(WaterMarkModel.getImagesFindDirectory());

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                ArtiMarkFrame.getInstance().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ArtiMarkFrame.getInstance().setVisible(true);
            }
        });
    }
}