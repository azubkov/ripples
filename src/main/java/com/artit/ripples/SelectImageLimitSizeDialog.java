package com.artit.ripples;


import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowEvent;

import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import javax.swing.*;

public class SelectImageLimitSizeDialog extends JDialog {
	static final int DG_WIDTH=555;
	static final int DG_HEIGHT=735;
	
	private JButton fOKButton;
	private JButton fCancelButton;
	static private JFrame fFrame;
	
	private JPanel panelWatermarkView;
	private ImageComponent watermarkImgComp;
	private Image nonCompressedWatermarkImage;	
	private TransparentBackground transBackofPanel;
	
	private JLabel theMessageLabel;
	
	private JTextField textFieldLimitImageX;
	private JTextField textFieldLimitImageY;
	
	private SelectImageLimitSizeDialog(Frame theParent){
		super(theParent,true);
		
		this.addWindowListener(new WindowAdapter(){
			  public void windowClosing(WindowEvent e) {
					setVisible(false);				  
			  }			
		});
		
		setSize(ArtiMarkFrame.convertX(DG_WIDTH),ArtiMarkFrame.convertY(DG_HEIGHT) );
		this.setLocation(
				25/*+ArtiMarkFrame.getInstance().getWidth()/2-this.getWidth()/2*/,
				25/*+ArtiMarkFrame.getInstance().getHeight()/2-this.getHeight()/2*/
				);
		
		this.setTitle("The best image size");
	 	
		this.setLayout(null);	
		
		panelWatermarkView = 
			new JPanel()
		{			
				public void paint(Graphics g){
					this.paintComponents(g);
				}			
		}
		;
		
		panelWatermarkView.setBounds(ArtiMarkFrame.convertX(25), ArtiMarkFrame.convertY(25),ArtiMarkFrame. convertX(500),ArtiMarkFrame.convertY(525));
		panelWatermarkView.setVisible(true);
		
		this.add(panelWatermarkView);
     	
     	panelWatermarkView.setLayout(null);
     	
    	theMessageLabel=new JLabel(
     			"<html><P>Now give us the size of image where you watermark looks good. Normally is your favorite photo's size which you put watermark by hands</P><BR><BR></html>");
     	
     	theMessageLabel.setBounds(ArtiMarkFrame.convertX(25), ArtiMarkFrame.convertY(550),ArtiMarkFrame. convertX(525),ArtiMarkFrame.convertY(100));
		
		this.add(theMessageLabel);
		
		watermarkImgComp = new ImageComponent();

		
		/*setWatermarkImage(WaterMarkModel.getWaterMarkImage());*/
				
		watermarkImgComp.setSize(panelWatermarkView.getWidth(),panelWatermarkView.getHeight());
		watermarkImgComp.setLocation(0,00);
		watermarkImgComp.setVisible(true);
		
		panelWatermarkView.add(watermarkImgComp);	
     		
		transBackofPanel = new TransparentBackground();
		panelWatermarkView.add(transBackofPanel);
			
		transBackofPanel.setBounds(0,0,panelWatermarkView.getWidth(),panelWatermarkView.getHeight());
		transBackofPanel.setVisible(true);
		
		textFieldLimitImageX = new JTextField(((Integer)WaterMarkModel.getLimitXBigImageSizeForWMResize()).toString(), 7);
		textFieldLimitImageX.setBounds(ArtiMarkFrame.convertX(190), ArtiMarkFrame.convertY(620), ArtiMarkFrame.convertX(70),ArtiMarkFrame.convertY( 30));

		
		this.add(textFieldLimitImageX);
		
		textFieldLimitImageY = new JTextField(((Integer)WaterMarkModel.getLimitYBigImageSizeForWMResize()).toString(), 7);
		textFieldLimitImageY.setBounds(ArtiMarkFrame.convertX(290), ArtiMarkFrame.convertY(620), ArtiMarkFrame.convertX(70),ArtiMarkFrame.convertY( 30));

		
		this.add(textFieldLimitImageY);
		/*
		 * addonFolderTField.addKeyListener(new KeyAdapter(){ public void
		 * keyTyped(KeyEvent e) {
		 * WaterMarkModel.setImageOutputDirectoryName(addonFolderTField.getText());
		 * System.out.println(WaterMarkModel.getImageOutputDirectoryName()); }
		 * });
		 */
		/*textFieldLimitImageX.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				WaterMarkModel
						.setImageOutputDirectoryName(addonFolderTField
								.getText());
			}
		});

		addonFolderTField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WaterMarkModel
						.setImageOutputDirectoryName(addonFolderTField
								.getText());
			};
		});
		*/
		
		
		fOKButton=new JButton("OK");
		fOKButton.setBounds(ArtiMarkFrame.convertX(25), ArtiMarkFrame.convertY(660),ArtiMarkFrame. convertX(200),ArtiMarkFrame.convertY(30));
//				System.out.println(" fOKButton "+fOKButton.getX()+" "
//						+fOKButton.getY()+" "
//						+fOKButton.getWidth() +" "
//						+fOKButton.getHeight()+" ");
		fOKButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){				
				WaterMarkModel.setLimitXBigImageSizeForWMResize(Integer.decode(textFieldLimitImageX.getText()));
				WaterMarkModel.setLimitYBigImageSizeForWMResize(Integer.decode(textFieldLimitImageY.getText()));
	     		setVisible(false);			
			}		
		});
		this.add(fOKButton);
		
		fCancelButton=new JButton("Cancel");
		fCancelButton.setBounds(ArtiMarkFrame.convertX(320), ArtiMarkFrame.convertY(660),ArtiMarkFrame. convertX(200),ArtiMarkFrame.convertY(30));
		fCancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
			setVisible(false);			
			}		
		});
		this.add(fCancelButton);
//		fCancelButton.setLocation(0, 0);
//		panelWatermarkView.add(fCancelButton);
		//		
		
		
			
		
		
		
		
		
		
	};
	
	public void setWatermarkImage(Image img){
		//ImageManager.save("c:\\aaa.jpg", img, "jpg");
		int imageWidth=panelWatermarkView.getWidth();
		int imageHeight=panelWatermarkView.getHeight();
		double stratchFactorWidth=(double)imageWidth/img.getWidth(null);
		double stratchFactorHeight=(double)imageHeight/img.getHeight(null);
		double stratchFactor=Math.min(stratchFactorWidth, stratchFactorHeight);	
		
		watermarkImgComp.setAppearanceImg(
				ImageManager.resizeImage( (BufferedImage)img,
						(int)(img.getWidth(null)*stratchFactor),
						(int)(img.getHeight(null)*stratchFactor) 
				)			 
				);		
		watermarkImgComp.setLocation(
				panelWatermarkView.getWidth()/2-watermarkImgComp.getWidth()/2, 
				panelWatermarkView.getHeight()/2-watermarkImgComp.getHeight()/2
				);

	};
	
	static public SelectImageLimitSizeDialog makeDialog(JFrame owner){
		fFrame=owner;
		if(fFrame==null){
			fFrame=new JFrame();	
		}
		
		SelectImageLimitSizeDialog theResult=new SelectImageLimitSizeDialog(fFrame);
		//fFrame.setSize(1200,200);
		return theResult;
		
		//theResult.getSize().width,theResult.getSize().height
	}
	
	
	public void setVisibleAndRefresh(boolean b){
		if(WaterMarkModel.getWaterMark() != null){
			ArtiMarkFrame.selectImageLimitSizeDg.setWatermarkImage(WaterMarkModel.getWaterMark());
		}
		this.textFieldLimitImageX.setText(
				((Integer)WaterMarkModel.getLimitXBigImageSizeForWMResize()).toString()
		);
		this.textFieldLimitImageY.setText(
				((Integer)WaterMarkModel.getLimitYBigImageSizeForWMResize()).toString()
		);
		super.setVisible(b);		
	}

}