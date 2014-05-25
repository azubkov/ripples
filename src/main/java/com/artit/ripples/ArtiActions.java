package com.artit.ripples;


import com.artit.ripples.utils.ExecutorTask;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

import java.io.File;
import javax.swing.event.*;
import java.awt.*;
public class ArtiActions   {
	static AbstractAction exit=new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
			   System.exit(0);			   
			}	
	};
	static AbstractAction findWatermarkFile=new AbstractAction(){
		public void actionPerformed(ActionEvent e) {		
			
			ArtiMarkFrame.watermarkFChooser.setCurrentDirectory(new File("."));//from file!
			int result = ArtiMarkFrame.watermarkFChooser.showDialog(ArtiMarkFrame.getInstance(), "Choose");
			if(result == JFileChooser.APPROVE_OPTION){
				String name = ArtiMarkFrame.watermarkFChooser.getSelectedFile().getAbsolutePath();
				 
				WaterMarkModel.setWaterMarkPath(name);	
				ArtiMarkFrame.currentWatermarkTField.setText(name);
				
				ArtiMarkFrame.watermarkImgComp.setAppearanceImg(WaterMarkModel.getWaterMark());
				
				ArtiMarkFrame.watermarkImgComp.setLocation(
						ArtiMarkFrame.panelWatermarkView.getWidth()/2-ArtiMarkFrame.watermarkImgComp.getWidth()/2, 
						ArtiMarkFrame.panelWatermarkView.getHeight()/2-ArtiMarkFrame.watermarkImgComp.getHeight()/2
						);
				
				ArtiMarkFrame.panelWatermarkView.repaint();
				
				ArtiMarkFrame.selectImageLimitSizeDg.setLocation(
						ArtiMarkFrame.getInstance().getX()+ArtiMarkFrame.getInstance().getWidth()/2-ArtiMarkFrame.selectImageLimitSizeDg.getWidth()/2, 
						ArtiMarkFrame.getInstance().getY()+ArtiMarkFrame.getInstance().getHeight()/2-ArtiMarkFrame.selectImageLimitSizeDg.getHeight()/2
						);
				ArtiMarkFrame.selectImageLimitSizeDg.setEnabled(true);
				ArtiMarkFrame.selectImageLimitSizeDg.setVisibleAndRefresh(true);				
			}	

		}	
	};
	
	
		static AbstractAction findDirectoryInput=new AbstractAction(){
		public void actionPerformed(ActionEvent e) {		
//			ArtiMarkFrame.processingLabel.setText("");
			;
			ArtiMarkFrame.dirictoryInputFChooser.setCurrentDirectory(new File("."));//from file!
			int result = ArtiMarkFrame.dirictoryInputFChooser.showDialog(ArtiMarkFrame.getInstance(), "Choose");
			if(result == JFileChooser.APPROVE_OPTION){
				String name = ArtiMarkFrame.dirictoryInputFChooser.getSelectedFile().getAbsolutePath();
				/*change the view and model*/
				WaterMarkModel.setImagesFindDirectory (name);	
				ArtiMarkFrame.currentFolderTField.setText(name);
				ImageManager.imagesFinder.setDirectory(WaterMarkModel.getImagesFindDirectory());
			}
		}	
	};
	
	
	static AbstractAction selectCoors=new AbstractAction(){
		public void actionPerformed(ActionEvent e) {	
			ImageManager.imagesFinder.setDirectory(WaterMarkModel.getImagesFindDirectory());
			if(ImageManager.imagesFinder.getRandomFilename(WaterMarkModel.isJpgTreatment(),WaterMarkModel.isPngTreatment(),WaterMarkModel.isGifTreatment())==null){
				return;
			}
			
				Image img = ImageManager.fromFileOpener.open(ImageManager.imagesFinder.getDirectory()+ 
						ImageManager.imagesFinder.getRandomFilename(WaterMarkModel.isJpgTreatment(),WaterMarkModel.isPngTreatment(),WaterMarkModel.isGifTreatment()));
					ArtiMarkFrame.coordinatesDefinerDialog.setImageCompFullscreen(img);		
					ArtiMarkFrame.coordinatesDefinerDialog.setPanelWmarkFrom(ArtiMarkFrame.panelWatermarkView);
					ArtiMarkFrame.coordinatesDefinerDialog.setVisible(true);
							
		
		}	
	};
	
	static AbstractAction BeginWMarking=new AbstractAction(){
		public void actionPerformed(ActionEvent e) {

               ArtiMarkFrame.butBeginWMarking.setEnabled(false);
                 ArtiMarkFrame.transBackofPanel.setVisible(false);
        ArtiMarkFrame.switchXslider.setVisible(false);
        ArtiMarkFrame.switchYslider.setVisible(false);



            Main.massImgFilter.setExtensions("jpg", "jpeg", "png", "gif");
            Main.massImgFilter.setTargetDirectory(WaterMarkModel.getImagesFindDirectory());

            Main.massImgFilter.start(new ExecutorTask() {
                @Override
                protected void perform() {
                    System.err.println("completed");
                               ArtiMarkFrame.transBackofPanel.setVisible(true);
        ArtiMarkFrame.switchXslider.setVisible(true);
        ArtiMarkFrame.switchYslider.setVisible(true);
                    ArtiMarkFrame.butBeginWMarking.setEnabled(true);

                }
            });
}
		
		
	};
	
	
	static ChangeListener switchYforWaterShape=new ChangeListener(){
		public void stateChanged(ChangeEvent e) {	
			ArtiMarkFrame.figureImgComp.setLocation(
					ArtiMarkFrame.figureImgComp.getX(),
					( (JSlider) e.getSource()).getValue() - ArtiMarkFrame.figureImgComp.getHeight()/2);
			ArtiMarkFrame.panelWatermarkView.repaint();		}	
	};
	
	static ChangeListener switchXforWaterShape=new ChangeListener(){
		public void stateChanged(ChangeEvent e) {				
			ArtiMarkFrame.figureImgComp.setLocation(
					( (JSlider) e.getSource()).getValue() - ArtiMarkFrame.figureImgComp.getWidth()/2,
					ArtiMarkFrame.figureImgComp.getY());
			ArtiMarkFrame.panelWatermarkView.repaint();
		}	
	};
	
	static ChangeListener selectWidthforWaterShape=new ChangeListener(){
		public void stateChanged(ChangeEvent e) {
			ArtiMarkFrame.figureImgComp.setLocation(
					ArtiMarkFrame.figureImgComp.getX() + ArtiMarkFrame.figureImgComp.getWidth()/2-( (JSlider) e.getSource()).getValue()/2,
					ArtiMarkFrame.figureImgComp.getY());
			ArtiMarkFrame.figureImgComp.setSize(
					( (JSlider) e.getSource()).getValue(),
					ArtiMarkFrame.figureImgComp.getHeight());
			ArtiMarkFrame.panelWatermarkView.repaint();			
		}	
	};
	
	static ChangeListener selectHeightforWaterShape=new ChangeListener(){
		public void stateChanged(ChangeEvent e) {
			ArtiMarkFrame.figureImgComp.setLocation(
					ArtiMarkFrame.figureImgComp.getX(),
					ArtiMarkFrame.figureImgComp.getY() + ArtiMarkFrame.figureImgComp.getHeight()/2-( (JSlider) e.getSource()).getValue()/2);
			ArtiMarkFrame.figureImgComp.setSize(ArtiMarkFrame.figureImgComp.getWidth(),
					( (JSlider) e.getSource()).getValue());
			ArtiMarkFrame.panelWatermarkView.repaint();	
		}	
	};
	
	static AbstractAction selectShapeColor=new AbstractAction(){
		public void actionPerformed(ActionEvent e) {				
			ArtiMarkFrame.colorChooserDialog.setVisible(true);		
		}	
	};
	
	static AbstractAction aboutDgShow=new AbstractAction(){
		public void actionPerformed(ActionEvent e) {		
		//	ArtiMarkFrame.selectImageLimitSizeDg  .setEnabled(true);
			//ArtiMarkFrame.selectImageLimitSizeDg  .setVisible(true);	
			

			//JOptionPane.showConfirmDialog(ArtiMarkFrame.getInstance(), "message");
		//	ArtiMarkFrame.selectImageLimitSizeDg.setEnabled(true);
		//	ArtiMarkFrame.selectImageLimitSizeDg.setVisibleAndRefresh(true);
	
		}	
	};
	
	static{
		exit.putValue(Action.NAME , "Exit");
//		exit = null;
		findWatermarkFile.putValue(Action.NAME , "Watermark image...");
		selectShapeColor.putValue(Action.NAME , "Shape color");
//		switchYforWaterShape.putValue(Action.NAME , "Watermark image...");
		selectCoors.putValue(Action.NAME , "Choose watermark location...");
		BeginWMarking.putValue(Action.NAME , "Go! ");
		findDirectoryInput.putValue(Action.NAME , "Choose target directory...");
		aboutDgShow.putValue(Action.NAME , "About");
	}

}
