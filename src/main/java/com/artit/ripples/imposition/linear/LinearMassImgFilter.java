package com.artit.ripples.imposition.linear;

import com.artit.ripples.ArtiMarkFrame;
import com.artit.ripples.ImageManager;
import com.artit.ripples.WaterMarkModel;
import com.artit.ripples.imposition.MassImgFilter;
import com.artit.ripples.utils.ExecutorTask;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LinearMassImgFilter extends MassImgFilter.Stub {

    @Override
    public void start(ExecutorTask executorTask) {


        if(ImageManager.imagesFinder.getRandomFilename(WaterMarkModel.isJpgTreatment(),WaterMarkModel.isPngTreatment(),WaterMarkModel.isGifTreatment()) == null){
                return;
        }


        Rectangle panelRect=ArtiMarkFrame.panelWatermarkView.getBounds();

        int leftSpare=Math.min(ArtiMarkFrame.watermarkImgComp.getX(),ArtiMarkFrame.figureImgComp.getX());
        int widthRequed=Math.max(ArtiMarkFrame.watermarkImgComp.getX()+ArtiMarkFrame.watermarkImgComp.getWidth(),ArtiMarkFrame.figureImgComp.getX()+ArtiMarkFrame.figureImgComp.getWidth());
        int topSpare=Math.min(ArtiMarkFrame.watermarkImgComp.getY(),ArtiMarkFrame.figureImgComp.getY());
        int heightRequed=Math.max(ArtiMarkFrame.watermarkImgComp.getY()+ArtiMarkFrame.watermarkImgComp.getHeight(),ArtiMarkFrame.figureImgComp.getY()+ArtiMarkFrame.figureImgComp.getHeight());
        widthRequed-=leftSpare;
        heightRequed-=topSpare;
        ArtiMarkFrame.watermarkImgComp.setLocation(ArtiMarkFrame.watermarkImgComp.getX()-leftSpare ,ArtiMarkFrame.watermarkImgComp.getY()-topSpare);
        ArtiMarkFrame.figureImgComp.setLocation(ArtiMarkFrame.figureImgComp.getX()-leftSpare ,ArtiMarkFrame.figureImgComp.getY()-topSpare);
        ArtiMarkFrame.panelWatermarkView.setSize(widthRequed, heightRequed);

/*-------------------------------------------------------------------------*/
           BufferedImage panelImg = (BufferedImage)ImageManager.getImageFromComponent(ArtiMarkFrame.panelWatermarkView);
           ArtiMarkFrame.panelWatermarkView.setBounds(panelRect);
        ArtiMarkFrame.watermarkImgComp.setLocation(ArtiMarkFrame.watermarkImgComp.getX()+leftSpare ,ArtiMarkFrame.watermarkImgComp.getY()+topSpare);
        ArtiMarkFrame.figureImgComp.setLocation(ArtiMarkFrame.figureImgComp.getX()+leftSpare ,ArtiMarkFrame.figureImgComp.getY()+topSpare);
//        ArtiMarkFrame.transBackofPanel.setVisible(true);
//        ArtiMarkFrame.switchXslider.setVisible(true);
//        ArtiMarkFrame.switchYslider.setVisible(true);
          Marker marker = new Marker(panelImg);
           /*-------------------------------------------------------------------------*/
        Image imgStorage;
        int left;
        int top;
        double widthStrech=1,heightStrech=1,stratchWaterMarkFactor;

        if(WaterMarkModel.isJpgTreatment() ){
            for(String filename:ImageManager.imagesFinder.getJPGfiles()){
                imgStorage=ImageManager.open(ImageManager.fromFileOpener,
                        ImageManager.imagesFinder.getDirectory()+filename);

                imgStorage = marker.convertImage(imgStorage);

                ImageManager.save(
                        ImageManager.jpgSaver,
                        ImageManager.imagesFinder.getDirectory()+WaterMarkModel.getImageOutputDirectoryName()+"art"+filename,
                        imgStorage);
            }
        }

        if(WaterMarkModel.isPngTreatment() ){
            for(String filename:ImageManager.imagesFinder.getPNGfiles()){
                imgStorage=ImageManager.open(ImageManager.fromFileOpener,
                        ImageManager.imagesFinder.getDirectory()+filename);

                imgStorage = marker.convertImage(imgStorage);

                ImageManager.save(
                        ImageManager.pngSaver,
                        ImageManager.imagesFinder.getDirectory()+WaterMarkModel.getImageOutputDirectoryName()+"art"+filename,
                        imgStorage);
            }
        }


        if(WaterMarkModel.isGifTreatment() ){
            for(String filename:ImageManager.imagesFinder.getGIFfiles()){

                imgStorage=ImageManager.open(ImageManager.fromFileOpener,
                        ImageManager.imagesFinder.getDirectory()+filename);


                imgStorage = marker.convertImage(imgStorage);

                ImageManager.save(
                        ImageManager.gifSaver,
                        ImageManager.imagesFinder.getDirectory()+WaterMarkModel.getImageOutputDirectoryName()+"art"+filename,
                        imgStorage);
            }
        }

        ArtiMarkFrame.butBeginWMarking.setEnabled(false);

        executorTask.run();


    }
}
