package com.artit.ripples;

import java.io.File;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;

import java.awt.image.BufferedImage;

import java.awt.*;

public class WaterMarkModel {
	
	static public final int screenXOnCreate = 1280;
	static public final int screenYOnCreate = 1024;
	
	static public final int screenXNow=Toolkit.getDefaultToolkit().getScreenSize().width ;
	static public final int screenYNow=Toolkit.getDefaultToolkit().getScreenSize().height ;
	
	

	
	static private boolean jpgTreatment=true;
	static private boolean pngTreatment=false;
	static private boolean gifTreatment=false;
	static private String waterMarkPath;//"F:\\mydocs\\waterMark.png"
	static private /*Image*/BufferedImage waterMarkFileImage;//MODEL
	static private Float visibleKoeff;
//	static private Dimension imageStandartSize;

	static private String imagesFindDirectory;/*"F:\\mydocs\\testWarter"*/
	static private String imageOutputDirectoryName;//"artiring\\"
	


//	public static ColorModel transparentPngColorModel;
	
	static private boolean isCompressXDistanceFromLeft;
	static private boolean isCompressYDistanceFromTop;
	static private double compressXDistance;
	static private double compressYDistance;
	
	static private int limitXBigImageSizeForWMResize;
	static private int limitYBigImageSizeForWMResize;
	

	//private static Point coorsOfWaterPanel = new Point();
	
	static{

		visibleKoeff=0.4f;
		//imageStandartSize=new Dimension(2592,1944);
		


		imagesFindDirectory=Main.CURRENT_DIRECTORY;
		imageOutputDirectoryName="WATERMARK\\";
		
		limitXBigImageSizeForWMResize = 800;
		limitYBigImageSizeForWMResize = 600;
	//	coorsOfWaterPanel.x=0;
	//	coorsOfWaterPanel.y=0;
		
		
		
	}
	
	static public Image xxxxxxxxxxxxx(){
		
			   BufferedImage bimg=null;
			   	
			   Dimension panelSize =  new Dimension(
					   ArtiMarkFrame.panelWatermarkView.getWidth(),
					   ArtiMarkFrame.panelWatermarkView.getHeight()   
			   );
			   Dimension imgSize =  new Dimension(
					   ArtiMarkFrame.watermarkImgComp.getAppearanceImg().getWidth(null),
					   ArtiMarkFrame.watermarkImgComp.getAppearanceImg().getHeight(null)
					   );
			   Rectangle figBounds = ArtiMarkFrame.figureImgComp.getBounds();
			
			   Rectangle wmSize=new Rectangle();
//			   String  left = "null",
//					   top = "null", 
//				 	   right = "null", 
//				 	   bottom = "null";
			   int figureCoor=figBounds.x;
			   int imageCoor=(panelSize.width/2-imgSize.width/2);
			   
			   if(figureCoor<imageCoor){
				   wmSize.x=figureCoor;
//				   left="shape";
			   }else{
				   wmSize.x=imageCoor;
//				   left="image";				   
			   }
			   
			   figureCoor=figBounds.y;
			   imageCoor=(panelSize.height/2-imgSize.height/2);			   
			   if(figureCoor<imageCoor){
				   wmSize.y=figureCoor;
//				   top="shape";
			   }else{
				   wmSize.y=imageCoor;
//				   top="image";				   
			   }
			   
			   figureCoor=figBounds.width+figBounds.x;
			   imageCoor=(panelSize.width/2+imgSize.width/2);			   
			   if(figureCoor>imageCoor){
				   wmSize.width=figureCoor;
//				   right="shape";
			   }else{
				   wmSize.width=imageCoor;
//				   right="image";				   
			   }
			   
			   figureCoor=figBounds.height+figBounds.y;
			   imageCoor=(panelSize.height/2+imgSize.height/2);			   
			   if(figureCoor>imageCoor){
				   wmSize.height=figureCoor;
//				   bottom="shape";
			   }else{
				   wmSize.height=imageCoor;
//				   bottom="image";				   
			   }
			   wmSize.width-=wmSize.x;
			   wmSize.height-=wmSize.y;

			   ArtiMarkFrame.transBackofPanel.setVisible(false);
			   ArtiMarkFrame.switchXslider.setVisible(false);
			   ArtiMarkFrame.switchYslider.setVisible(false);
			   
	
			
//			   			BufferedImage temp =  new BufferedImage(WaterMarkModel.transparentPngColorModel,
//			   					WaterMarkModel.transparentPngColorModel.createCompatibleWritableRaster(panelSize.width,panelSize.height)
//			   					,false,null);

			   				/**7 6 3 2 
			   				 * TYPE_INT_ARGB
			   				 * TYPE_INT_ARGB_PRE
			   				 * TYPE_4BYTE_ABGR
			   				 * TYPE_4BYTE_ABGR_PRE
			   				 * 
			   				 * 5 8 9 10 11 13 
			   				 * */
			   			int imagetype=BufferedImage.TYPE_INT_ARGB;
			   			BufferedImage temp =  new BufferedImage(panelSize.width,panelSize.height,
			   					imagetype);
			   		
			   			/*	WritableRaster tempRaster = temp.getRaster();			   			
			   			int pixs[]=null;
			   			pixs= tempRaster.getPixels(0, 0,panelSize.width,panelSize.height,pixs );
			   			Arrays.fill(pixs,0x00000000) ;
			   			System.out.println("pixs wm ");
			   			System.out.println(pixs[10]&0x000000ff);
			   			System.out.println(pixs[10]&0x0000ff00);
			   			System.out.println(pixs[10]&0x00ff0000);
			   			System.out.println(pixs[10]&0xff000000);
			   			tempRaster.setPixels(0, 0,panelSize.width,panelSize.height, pixs);
			   			*/
			   			
			     		Graphics2D g2Dest = temp.createGraphics();	
//			     		ImageManager.save("c:\\"+imagetype+"_1.png", temp,"png");
//				    	ImageManager.save("c:\\"+imagetype+"_1.jpg", temp,"jpg");
			   			
				    	for(Component panelComp : ArtiMarkFrame.panelWatermarkView.getComponents()){
				    		if(panelComp.isVisible()){
				    			panelComp.paint(g2Dest);
				    		}
				    	}
				    	g2Dest.dispose();
				
//				    	ImageManager.save("c:\\"+imagetype+"_2.png", temp,"png");
//				    	ImageManager.save("c:\\"+imagetype+"_2.jpg", temp,"jpg");	
				    	
				    	/*
				    	pixs= tempRaster.getPixels(0, 175,panelSize.width,50,pixs );
			   			Arrays.fill(pixs,0x00000000) ;		
			   			tempRaster.setPixels(0, 175,panelSize.width,50, pixs);
			   			*/			   
				    	
//				    	 bimg =  new BufferedImage(WaterMarkModel.transparentPngColorModel,
//			   					WaterMarkModel.transparentPngColorModel.createCompatibleWritableRaster(wmSize.width,wmSize.height)
//			   					,false,null);
				      	bimg = new BufferedImage(wmSize.width,wmSize.height,
				      			imagetype);
				  			   
				    	Graphics2D g2water = bimg.createGraphics();	 				    	
				    	g2water.drawImage(temp, 0, 0, wmSize.width,wmSize.height,wmSize.x, wmSize.y, wmSize.width+wmSize.x, wmSize.height+wmSize.y, null);
				        g2water.dispose();
				   
				    		    
//				    	ImageManager.save("c:\\"+imagetype+"_3.png", bimg,"png");
//				    	ImageManager.save("c:\\"+imagetype+"_3.jpg", bimg,"jpg");
// 
					    	   

			   			ArtiMarkFrame.transBackofPanel.setVisible(true);
					    ArtiMarkFrame.switchXslider.setVisible(true);
					    ArtiMarkFrame.switchYslider.setVisible(true);			   
					    ArtiMarkFrame.panelWatermarkView.repaint();	
			return bimg;        
		}
		
		
	
	
	
	public static Image getWaterMark() {
		return waterMarkFileImage;
	}
	

	
	private static void setWaterMark(Image waterMark) {
		WaterMarkModel.waterMarkFileImage = (BufferedImage) waterMark;		
		//transparentPngColorModel=WaterMarkModel.waterMarkFileImage.getColorModel();	
	}
	
	public static Float getVisibleKoeff() {
		return visibleKoeff;
	}
	public static void setVisibleKoeff(Float visibleKoeff) {
		WaterMarkModel.visibleKoeff = visibleKoeff;
	}

	public static String getImagesFindDirectory() {
		return imagesFindDirectory;
	}
	public static void setImagesFindDirectory(String imagesFindDirectory) {
		WaterMarkModel.imagesFindDirectory = imagesFindDirectory;
	}
	public static String getImageOutputDirectoryName() {
		return imageOutputDirectoryName;
	}
	public static void setImageOutputDirectoryName(String imageOutputDirectoryName) {
		WaterMarkModel.imageOutputDirectoryName = imageOutputDirectoryName;
		
	}
	public static boolean isJpgTreatment() {
		return jpgTreatment;
	}
	public static void setJpgTreatment(boolean jpgTreatment) {
		WaterMarkModel.jpgTreatment = jpgTreatment;
	}
	public static boolean isPngTreatment() {
		return pngTreatment;
	}
	public static void setPngTreatment(boolean pngTreatment) {
		WaterMarkModel.pngTreatment = pngTreatment;
	}
	public static boolean isGifTreatment() {
		return gifTreatment;
	}
	public static void setGifTreatment(boolean gifTreatment) {
		WaterMarkModel.gifTreatment = gifTreatment;
	}
	public static String getWaterMarkPath() {
		return waterMarkPath;
	}
	public static void setWaterMarkPath(String waterMarkPath) {
		WaterMarkModel.waterMarkPath = waterMarkPath;
		setWaterMark(ImageManager.fromFileOpener.open(waterMarkPath));		
	}


	static public boolean isCompressXDistanceFromLeft() {
		return isCompressXDistanceFromLeft;
	}
	static public void setCompressXDistanceFromLeft(boolean newIsCompressXDistanceFromLeft) {
		isCompressXDistanceFromLeft = newIsCompressXDistanceFromLeft;
	}
	static public boolean isCompressYDistanceFromTop() {
		return isCompressYDistanceFromTop;
	}
	static public void setCompressYDistanceFromTop(boolean newIsCompressYDistanceFromTop) {
		 isCompressYDistanceFromTop = newIsCompressYDistanceFromTop;
	}
	
	static  public double getCompressXDistance() {
		return compressXDistance;
	}
	
	 static  public void setCompressXDistance(double newCompressXDistance) {
		compressXDistance = newCompressXDistance;
	}
	 
	static public double getCompressYDistance() {
		return compressYDistance;
	}

	static public void setCompressYDistance(double newCompressYDistance) {
		compressYDistance = newCompressYDistance;
	}

	public static int getLimitXBigImageSizeForWMResize() {
		return limitXBigImageSizeForWMResize;
	}

	public static void setLimitXBigImageSizeForWMResize(
			int limitXBigImageSizeForWMResize) {
		WaterMarkModel.limitXBigImageSizeForWMResize = limitXBigImageSizeForWMResize;
	}

	public static int getLimitYBigImageSizeForWMResize() {
		return limitYBigImageSizeForWMResize;
	}

	public static void setLimitYBigImageSizeForWMResize(
			int limitYBigImageSizeForWMResize) {
		WaterMarkModel.limitYBigImageSizeForWMResize = limitYBigImageSizeForWMResize;
	}
}
