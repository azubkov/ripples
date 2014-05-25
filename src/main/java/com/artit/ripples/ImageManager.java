package com.artit.ripples;

import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
public class ImageManager {
	static public ImagesFinder imagesFinder=new ImagesFinder();		
	static public OpenerFromFile fromFileOpener=new OpenerFromFile();
	
	static public ImageSaver jpgSaver=new ImageSaver(){
		public void save(String fullpath,Image imgToSave){
			 try {		           
				 	File file = new File(fullpath);		            	
		            file.delete();
		            file.mkdirs();
		           	ImageIO.write((BufferedImage)imgToSave, "jpg", file);
		           } catch (IOException e) {}			
		};		
	};
	
	static public ImageSaver pngSaver=new ImageSaver(){
		public void save(String fullpath,Image imgToSave){
			 try {		           
				 	File file = new File(fullpath);		            	
		            file.delete();
		            file.mkdirs();
		           	ImageIO.write((BufferedImage)imgToSave, "png", file);
		           } catch (IOException e) {}			
		};		
	};
	
	static public ImageSaver gifSaver=new ImageSaver(){
		public void save(String fullpath,Image imgToSave){
			 try {		           
				 	File file = new File(fullpath);		            	
		            file.delete();
		            file.mkdirs();
		           	ImageIO.write((BufferedImage)imgToSave, "gif", file);
		           } catch (IOException e) {}			
		};		
	};
	
	
//	static public ImageModifier modifierWaterMarker=new ImageModifier(){
//		public Image modify(Image imgStorage){	
//
//			Graphics2D g2=(Graphics2D)imgStorage.getGraphics();		
//	        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,WaterMarkModel.getVisibleKoeff());//WarteringModel
//	        g2.setComposite(ac); 
//	        Point sourcePanelCoor=ArtiMarkFrame.panelWatermarkView.getLocation();
//	        
//	   //     Point leftUpCoors=WaterMarkModel.getCoorsOfWaterPanel();
//	   //     ArtiMarkFrame.panelWatermarkView.setLocation(leftUpCoors.x,leftUpCoors.y);
//	        
//	       // ArtiMarkFrame.panelWatermarkView.set
////	        ArtiMarkFrame.panelWatermarkView.
//	    /*    if(imgStorage.getWidth(null)<(leftUpCoors.x+WaterMarkModel.getWaterMark().getWidth(null))){
//	        	leftUpCoors.x=imgStorage.getWidth(null)-WaterMarkModel.getWaterMark().getWidth(null);   	
//	        }
//	        if(imgStorage.getHeight(null)<(leftUpCoors.y+WaterMarkModel.getWaterMark().getHeight(null))){
//	        	leftUpCoors.y=imgStorage.getHeight(null)-WaterMarkModel.getWaterMark().getHeight(null);   	
//	        }
//	        */
//	       // g2.drawImage(WaterMarkModel.getWaterMark(),leftUpCoors.x , leftUpCoors.y,null);
//	        ArtiMarkFrame.panelWatermarkView.paint(g2);
//	        g2.dispose();
//	        return imgStorage;			
//		};		
//	};
	
	
	/*commands*/
	static public Image open(ImageOpener imageOpener,String fullpath){
		return imageOpener.open(fullpath);
	};
	
//	static public Image modify(ImageModifier imageModifier,Image imgStorage){
//		return imageModifier.modify(imgStorage);
//	};
	
	static public void save(ImageSaver imageSaver,String fullpath,Image imgToSave){
		imageSaver.save(fullpath, imgToSave);
	};
	

	static  public Image resizeImg(Image img,int w ,int h){		    
		    BufferedImage bimg = new BufferedImage(w,h, 
   			        BufferedImage.TYPE_INT_ARGB);
		    	
		    	
		    Graphics2D g2Dest = bimg.createGraphics();
		 //   g2Dest.clearRect(0, 0,w,h);	 		    
		     // g2Dest.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            //            RenderingHints.VALUE_ANTIALIAS_ON);
		    g2Dest.drawImage(img, 0, 0, w,h,0, 0, img.getWidth(null),img.getHeight(null), null);
		    g2Dest.dispose();		   	
		    return bimg;
	}

	
	public static BufferedImage resizeImage(BufferedImage image, int width, int height) {

		ColorModel cm = image.getColorModel();

		WritableRaster raster = cm.createCompatibleWritableRaster(width, height);

		boolean isRasterPremultiplied = cm.isAlphaPremultiplied();

		BufferedImage target = new BufferedImage(cm, raster, isRasterPremultiplied, null);
		Graphics2D g2 = target.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		double scalex = (double) target.getWidth()/ image.getWidth();
		double scaley = (double) target.getHeight()/ image.getHeight();

		AffineTransform xform = AffineTransform.getScaleInstance(scalex, scaley);
		g2.drawRenderedImage(image, xform);
		g2.dispose();
		return target;
	}

	
	
	

	//just a helper
	static public  Image getTransparentImage(Image image, Color color)
	    {
	         Image img=Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(),  new TransparentColorFilter(color)));  
	         
	         BufferedImage bimg = new BufferedImage(image.getWidth(null),image.getHeight(null), 
	   			        BufferedImage.TYPE_INT_ARGB);
	         bimg.getGraphics().drawImage(img, 0, 0, image.getWidth(null), image.getHeight(null), null);
	         return bimg;
	    }


	static public void save(String fullpath,Image imgToSave,String formatName){
		 try {
	           
			 	File file = new File(fullpath);
	            	
	            	file.delete();
	            	file.mkdirs();
	           	ImageIO.write((BufferedImage)imgToSave, formatName, file);
	           } catch (IOException e) {}
		
		
		
	};	


static public BufferedImage getImageFromComponent(Component comp){
		BufferedImage temp =  new BufferedImage(comp.getWidth(),comp.getHeight(),
				BufferedImage.TYPE_INT_ARGB);		
		Graphics2D g2Dest = temp.createGraphics();	
		comp.paint(g2Dest);
		g2Dest.dispose();	
		return temp;
}
	
	
	
	
	
	
	
}
