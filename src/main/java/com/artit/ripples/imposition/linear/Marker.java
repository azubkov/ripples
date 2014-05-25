package com.artit.ripples.imposition.linear;

import com.artit.ripples.ImageManager;
import com.artit.ripples.WaterMarkModel;
import com.artit.ripples.imposition.ImgFilter;

import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * Created by IntelliJ IDEA.
 * User: art
 * Date: 07.05.2011
 * Time: 23:17:17
 * To change this template use File | Settings | File Templates.
 */
public class Marker implements ImgFilter {
    private final Image mark;

    public Marker(Image mark) {
        this.mark = mark;
    }

    @Override
    public String convertName(String sourcePath) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Image convertImage(Image sourceImage) {
        int left;
        int top;
        double widthStrech = 1, heightStrech = 1, stratchWaterMarkFactor;

        if(WaterMarkModel.getLimitXBigImageSizeForWMResize()>
                sourceImage.getWidth(null)){
                    widthStrech=(double)sourceImage.getWidth(null)/WaterMarkModel.getLimitXBigImageSizeForWMResize();
                }else{widthStrech=1;}
                if(WaterMarkModel.getLimitYBigImageSizeForWMResize()>
                sourceImage.getHeight(null)){
                    widthStrech=(double)sourceImage.getHeight(null)/WaterMarkModel.getLimitYBigImageSizeForWMResize();
                }else{heightStrech=1;}
                stratchWaterMarkFactor=Math.min(widthStrech, heightStrech);

                if(WaterMarkModel.isCompressXDistanceFromLeft()){
                    left=(int)(WaterMarkModel.getCompressXDistance()*sourceImage.getWidth(null));
                }else{
                    left=sourceImage.getWidth(null)-(int)(mark.getWidth(null)*stratchWaterMarkFactor)-(int)(WaterMarkModel.getCompressXDistance()*sourceImage.getWidth(null));
                }

                if(WaterMarkModel.isCompressYDistanceFromTop()){
                    top=(int)(WaterMarkModel.getCompressYDistance()*sourceImage.getHeight(null));
                }else{
                    top=sourceImage.getHeight(null)-(int)(mark.getHeight(null)*stratchWaterMarkFactor)-(int)(WaterMarkModel.getCompressYDistance()*sourceImage.getHeight(null));
                }

                sourceImage.getGraphics().drawImage(
                        ImageManager.resizeImage( (BufferedImage)mark,
                                (int)(mark.getWidth(null)*stratchWaterMarkFactor),
                                (int)(mark.getHeight(null)*stratchWaterMarkFactor)
                        )
                        ,(int)(left/1),(int)(top/1),null);

        return sourceImage;

    }
}
