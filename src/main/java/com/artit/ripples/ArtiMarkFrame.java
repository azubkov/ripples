package com.artit.ripples;

import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;


public class ArtiMarkFrame extends JFrame/* implements Runnable*/{
	static public ArtiMarkFrame frame;
	
	//Thread processingLabelPaintThread;

	public static final int FRAME_WIDTH = 720;
	public static final int FRAME_HEIGHT = 900;
	
	static public TransparentBackground transBackofPanel;
	static public JPanel panelWatermarkView;
	static public ImageComponent watermarkImgComp;
	static public JTextField currentWatermarkTField;
	static public ShapeDrawComponent figureImgComp;
	
	static public JFileChooser watermarkFChooser;
	
	static public JFileChooser dirictoryInputFChooser;
	static public  JTextField currentFolderTField ;
	
	static public JSlider switchXslider;
	static public JSlider switchYslider;
	
	static public JDialog colorChooserDialog;
	static public CoorDefDialog coordinatesDefinerDialog;
	static public JColorChooser colorChooserCreator;	
	static public JButton butSelectColor ;
	
	static public JButton butExit ;
	static public JButton butBeginWMarking ;
	
	static public SelectImageLimitSizeDialog selectImageLimitSizeDg;
//	static public JLabel processingLabel;
	
	  

	static {

		widthScreenKoeff=((double)WaterMarkModel.screenXNow)/((double)WaterMarkModel.screenXOnCreate);
		heightScreenKoeff=((double)WaterMarkModel.screenYNow)/((double)WaterMarkModel.screenYOnCreate);
	
		
		frame = new ArtiMarkFrame();
		
	}

	static public ArtiMarkFrame getInstance() {
		return frame;
	};
	static private final double widthScreenKoeff;
	
	static private final double heightScreenKoeff;

	static public int convertX(int sourceWidth){		
		return (int)(sourceWidth*widthScreenKoeff);
	}
	
	static public int convertY(int sourceHeight){		
		return (int)(sourceHeight*heightScreenKoeff);
	}
	
	private ArtiMarkFrame() {		
		super();

		setSize(convertX(FRAME_WIDTH),convertY(FRAME_HEIGHT) );		
		this.setLocation(WaterMarkModel.screenXNow / 7, WaterMarkModel.screenYNow / 14);
		this.setTitle("Watermark - Artimark");
		// this.setResizable(false);
		// Image image=kit.getImage("icon.png");
		// this.setIconImage(image);
		// UIUIUIUIUIUIUI

		this.setLayout(null);
		this.setFont(new Font("Serif", Font.BOLD, 15));
		// Panel panel = new Panel();
		// panel.setLayout(null);
		// panel.setBounds(0,0,FRAME_WIDTH*2,FRAME_HEIGHT*2);
		// this.add(panel);

		JMenuBar menuBar = MenuBarCreator.createMenu();
		this.setJMenuBar(menuBar);
		
		

		/** ***********************extension checkboxes******************** */
		{
			selectImageLimitSizeDg =SelectImageLimitSizeDialog.makeDialog(this);
			//aboutDg.setVisible(false);
			
			JLabel rashFileLabel = new JLabel("File extensions for processing:",javax.swing.JLabel.LEFT);
			rashFileLabel.setBounds(convertX(25), convertY(15), convertX(200),convertY( 15));
			this.add(rashFileLabel);

			final JCheckBox jpgChBox = new JCheckBox("*.jpg *.jpeg");
			jpgChBox.setSelected(WaterMarkModel.isJpgTreatment());
			jpgChBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					WaterMarkModel.setJpgTreatment(jpgChBox.isSelected());
				};
			});
			this.add(jpgChBox);
			jpgChBox.setBounds(convertX(30), convertY(40), convertX(90),convertY( 15));


			final JCheckBox pngChBox = new JCheckBox("*.png");
			pngChBox.setSelected(WaterMarkModel.isPngTreatment());
			pngChBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					WaterMarkModel.setPngTreatment(pngChBox.isSelected());
				};
			});
			this.add(pngChBox);
			pngChBox.setBounds(convertX(130), convertY(40), convertX(90),convertY( 15));

			final JCheckBox gifChBox = new JCheckBox("*.gif");
			gifChBox.setSelected(WaterMarkModel.isGifTreatment());
			gifChBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					WaterMarkModel.setGifTreatment(gifChBox.isSelected());
				};
			});
			this.add(gifChBox);
			gifChBox.setBounds(convertX(230), convertY(40), convertX(90),convertY( 15));
		}
		/** ***********************target dir name*************** */
		{
			JLabel addonFolderName = new JLabel("Create subdirectory:", javax.swing.JLabel.LEFT);
			addonFolderName.setBounds(convertX(420), convertY(15), convertX(200),convertY( 15));
			this.add(addonFolderName);

			final JTextField addonFolderTField = new JTextField(WaterMarkModel.getImageOutputDirectoryName(), 30);
			addonFolderTField.setBounds(convertX(420), convertY(35), convertX(275),convertY( 25));

			
			this.add(addonFolderTField);
			/*
			 * addonFolderTField.addKeyListener(new KeyAdapter(){ public void
			 * keyTyped(KeyEvent e) {
			 * WaterMarkModel.setImageOutputDirectoryName(addonFolderTField.getText());
			 * System.out.println(WaterMarkModel.getImageOutputDirectoryName()); }
			 * });
			 */
			addonFolderTField.addFocusListener(new FocusAdapter() {
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
		}
		/** ***********************name of target directory *************** */
		{
			JLabel treatFolderName = new JLabel("Processed directory:",javax.swing.JLabel.LEFT);
			treatFolderName.setBounds(convertX(25), convertY(75), convertX(200),convertY( 25));
			this.add(treatFolderName);

			/*
			 * Graphics2D g2= (Graphics2D)this.getGraphics(); Font
			 * f=g2.getFont(); java.awt.font.FontRenderContext
			 * frContext=g2.getFontRenderContext(); Rectangle2D
			 * strBounds=f.getStringBounds(selectFolderLabel.getText(),
			 * frContext);
			 */

			dirictoryInputFChooser = new JFileChooser();
			dirictoryInputFChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
			JButton butDirectoryInputReview = new JButton( ArtiActions.findDirectoryInput );
			this.add(butDirectoryInputReview);
			butDirectoryInputReview.setBounds(convertX(655), convertY(100), convertX(30),convertY( 25));
			
			currentFolderTField = new JTextField(WaterMarkModel.getImagesFindDirectory(), 30);
			currentFolderTField.setBounds(convertX(25), convertY(100), convertX(630),convertY( 25));
			this.add(currentFolderTField);
			currentFolderTField.setEditable(false);
		}
		/** ***********************area put the watermark*************** */
		{   
			panelWatermarkView = 
			new JPanel(){
				public void paint(Graphics g){
					this.paintComponents(g);
				}			
			};
			panelWatermarkView.setBounds(convertX(25), convertY(150), convertX(670),convertY( 400));
     		this.add(panelWatermarkView);
		
			watermarkImgComp = new ImageComponent();
			watermarkImgComp.setAppearanceImg(WaterMarkModel.getWaterMark());
			
			
			panelWatermarkView.setLayout(null);
			watermarkImgComp.setLocation(
					panelWatermarkView.getWidth()/2-watermarkImgComp.getWidth()/2, 
					panelWatermarkView.getHeight()/2-watermarkImgComp.getHeight()/2
					);
			panelWatermarkView.add(watermarkImgComp);	
		

			switchYslider = new JSlider(JSlider.VERTICAL, 0, panelWatermarkView
					.getHeight(), panelWatermarkView.getHeight() / 2);
			switchYslider.setBounds(0, 0, convertX(40), panelWatermarkView.getHeight());
			switchYslider.setInverted(true);
			panelWatermarkView.add(switchYslider);
			switchYslider.addChangeListener(ArtiActions.switchYforWaterShape);
			
			switchYslider.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {					
					if (e.getKeyCode() == KeyEvent.VK_SHIFT && ((ChangeListener)switchYslider.getChangeListeners()[0]).equals(ArtiActions.switchYforWaterShape) ==true) {
						/* switch the scroller listeners */					
						switchYslider.removeChangeListener(ArtiActions.switchYforWaterShape);
						switchYslider.setValue((int)figureImgComp.getHeight());
						switchYslider.addChangeListener(ArtiActions.selectHeightforWaterShape);						
					}
				}

				/**
				 * Invoked when a key has been released.
				 */
				public void keyReleased(KeyEvent e) {				
					if (e.getKeyCode() == KeyEvent.VK_SHIFT && ((ChangeListener)switchYslider.getChangeListeners()[0]).equals(ArtiActions.selectHeightforWaterShape) ==true) {						
						switchYslider.removeChangeListener(ArtiActions.selectHeightforWaterShape);
						switchYslider.setValue((int)figureImgComp.getY()+(int)figureImgComp.getHeight()/2);
						switchYslider.addChangeListener(ArtiActions.switchYforWaterShape);					
					}
				}

			});

			switchXslider = new JSlider(0, panelWatermarkView.getWidth(),
					panelWatermarkView.getWidth() / 2);
			switchXslider.setBounds(0, 0, panelWatermarkView.getWidth(),convertY( 40));
			// switchXslider.setInverted(true);
			panelWatermarkView.add(switchXslider);
			switchXslider.addChangeListener(ArtiActions.switchXforWaterShape);

			
			
			switchXslider.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {					
					if (e.getKeyCode() == KeyEvent.VK_SHIFT && ((ChangeListener)switchXslider.getChangeListeners()[0]).equals(ArtiActions.switchXforWaterShape) ==true) {
						/* switch the scroller listeners */					
						switchXslider.removeChangeListener(ArtiActions.switchXforWaterShape);
						switchXslider.setValue((int)figureImgComp.getWidth());
						switchXslider.addChangeListener(ArtiActions.selectWidthforWaterShape);						
					}
				}

				/**
				 * Invoked when a key has been released.
				 */
				public void keyReleased(KeyEvent e) {				
					if (e.getKeyCode() == KeyEvent.VK_SHIFT && ((ChangeListener)switchXslider.getChangeListeners()[0]).equals(ArtiActions.selectWidthforWaterShape) ==true) {						
						switchXslider.removeChangeListener(ArtiActions.selectWidthforWaterShape);
						switchXslider.setValue((int)figureImgComp.getX()+(int)figureImgComp.getWidth()/2);
						switchXslider.addChangeListener(ArtiActions.switchXforWaterShape);					
					}
				}

			});
			
			JLabel transparencyLabel = new JLabel("the Label:",javax.swing.JLabel.LEFT);
			transparencyLabel.setBounds(convertX(25), convertY(575), convertX(100),convertY( 15));
			this.add(transparencyLabel);

			final JSlider transparencySlider = new JSlider(0, 100,
					(int) (watermarkImgComp.getVisibleKoeff() * 100));
			transparencySlider.setBounds(convertX(350), convertY(570), convertX(345),convertY( 50));
			transparencySlider.setPaintTicks(true);
			// transparencySlider.setSnapToTicks(true);
			transparencySlider.setPaintLabels(true);
			transparencySlider.setMajorTickSpacing(10);

			Dictionary<Integer, Component> labelTadle = new Hashtable<Integer, Component>();
			labelTadle.put(00, new JLabel("0"));
			labelTadle.put(10, new JLabel("1"));
			labelTadle.put(20, new JLabel("2"));
			labelTadle.put(30, new JLabel("3"));
			labelTadle.put(40, new JLabel("4"));
			labelTadle.put(50, new JLabel("5"));
			labelTadle.put(60, new JLabel("6"));
			labelTadle.put(70, new JLabel("7"));
			labelTadle.put(80, new JLabel("8"));
			labelTadle.put(90, new JLabel("9"));
			labelTadle.put(100, new JLabel("10"));

			transparencySlider.setLabelTable(labelTadle);
			this.add(transparencySlider);

			transparencySlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					watermarkImgComp.setVisibleKoeff((float) transparencySlider
							.getValue() / 100);
					panelWatermarkView.repaint();
				};
			});

			currentWatermarkTField = new JTextField(WaterMarkModel.getWaterMarkPath() , 30);
			currentWatermarkTField.setBounds(convertX(100), convertY(570), convertX(200),convertY( 25));
			currentWatermarkTField.setEditable(false);
			this.add(currentWatermarkTField);

			JButton butWatermarkReview = new JButton( ArtiActions.findWatermarkFile );
			this.add(butWatermarkReview);
			butWatermarkReview.setBounds(convertX(300), convertY(570), convertX(30),convertY( 25));

			watermarkFChooser = new JFileChooser();
			javax.swing.filechooser.FileNameExtensionFilter filter = new javax.swing.filechooser.FileNameExtensionFilter(
					"Images ", "jpg", "jpeg", "png", "gif");
			watermarkFChooser.setFileFilter(filter);
			/* javax.swing.filechooser.FileNameExtensionFilter */
			
		}
		/** ***********************area for label's background*************** */
		{
			figureImgComp = new ShapeDrawComponent();

			figureImgComp.setBounds(panelWatermarkView.getWidth()/2-convertX(50), 
					panelWatermarkView.getHeight()/2-convertY(50), convertX(100), convertY(100));
			
			
			panelWatermarkView.add(figureImgComp);
			transBackofPanel = new TransparentBackground();
			panelWatermarkView.add(transBackofPanel);
			
			transBackofPanel.setBounds(0,0,panelWatermarkView.getWidth(),panelWatermarkView.getHeight());

			JLabel transparencyLabel = new JLabel("The shape:",
					javax.swing.JLabel.LEFT);
			transparencyLabel.setBounds(convertX(25), convertY(640), convertX(100),convertY( 15));

			this.add(transparencyLabel);

			final JSlider transparencyFigureSlider = new JSlider(0, 100,
					(int) (figureImgComp.getVisibleKoeff() * 100));
			transparencyFigureSlider.setBounds(convertX(350), convertY(635), convertX(345),convertY( 50));

			transparencyFigureSlider.setPaintTicks(true);
			transparencyFigureSlider.setPaintLabels(true);
			transparencyFigureSlider.setMajorTickSpacing(10);

			Dictionary<Integer, Component> labelTadle = new Hashtable<Integer, Component>();
			labelTadle.put(00, new JLabel("0"));
			labelTadle.put(10, new JLabel("1"));
			labelTadle.put(20, new JLabel("2"));
			labelTadle.put(30, new JLabel("3"));
			labelTadle.put(40, new JLabel("4"));
			labelTadle.put(50, new JLabel("5"));
			labelTadle.put(60, new JLabel("6"));
			labelTadle.put(70, new JLabel("7"));
			labelTadle.put(80, new JLabel("8"));
			labelTadle.put(90, new JLabel("9"));
			labelTadle.put(100, new JLabel("10"));

			transparencyFigureSlider.setLabelTable(labelTadle);
			this.add(transparencyFigureSlider);

			transparencyFigureSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					figureImgComp.setVisibleKoeff((float) transparencyFigureSlider.getValue() / 100);
					panelWatermarkView.repaint();
				};
			});

			JComboBox selectShapeCombo = new JComboBox();
			/* Arc2D, Ellipse2D, Rectangle2D, RoundRectangle2D */
			final String arc = "none",
						 ellipse = "ellipsis", 
						 rect = "rectangle", 
						 roundrect = "rounded rectangle";
			selectShapeCombo.addItem(arc);
			selectShapeCombo.addItem(ellipse);
			selectShapeCombo.addItem(rect);
			selectShapeCombo.addItem(roundrect);
			selectShapeCombo.setBounds(convertX(100), convertY(640), convertX(200),convertY( 25));

			selectShapeCombo.setSelectedItem(ellipse);
			this.add(selectShapeCombo);

			selectShapeCombo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String selected = (String) ((JComboBox) e.getSource())
							.getSelectedItem();
					RectangularShape shape = null;
					if (selected.equals(arc)) {
						shape = new java.awt.geom.Arc2D.Double();
					}
					if (selected.equals(ellipse)) {
						shape = new java.awt.geom.Ellipse2D.Double();
					}
					if (selected.equals(rect)) {
						shape = new java.awt.geom.Rectangle2D.Double();
					}
					if (selected.equals(roundrect)) {
						shape = new java.awt.geom.RoundRectangle2D.Double();
						( (java.awt.geom.RoundRectangle2D.Double)shape).archeight=50;
						( (java.awt.geom.RoundRectangle2D.Double)shape).arcwidth =50;
					}
					figureImgComp.setShape(shape);
					panelWatermarkView.repaint();
				};
			});

			ArtiMarkFrame.colorChooserCreator=new JColorChooser();
			ArtiMarkFrame.colorChooserCreator.getSelectionModel().addChangeListener(new ChangeListener(){
				public void stateChanged(ChangeEvent e){
					ArtiMarkFrame.figureImgComp.setShapeColor(ArtiMarkFrame.colorChooserCreator.getColor());					
					ArtiMarkFrame.figureImgComp.repaint();
					butSelectColor.repaint();
				};
			});
			ArtiMarkFrame.colorChooserDialog=new JDialog(ArtiMarkFrame.frame,false);
			ArtiMarkFrame.colorChooserDialog.add(ArtiMarkFrame.colorChooserCreator);
			ArtiMarkFrame.colorChooserDialog.pack();
			
			butSelectColor = new JButton( ArtiActions.selectShapeColor ){
		    	public void paintComponent(Graphics g) {
		    		Graphics2D g2=(Graphics2D)g;
		    		g2.setColor(figureImgComp.getShapeColor());		    		
		    		g2.fillRect(0, 0, g2.getClipBounds().width, g2.getClipBounds().height);
		    	}
		    };


			this.add(butSelectColor);
			butSelectColor.setBounds(convertX(300), convertY(640), convertX(30),convertY( 25));



		}

		
		 /*************************buttons : coors, start, cancel*************** */
		
		{
			JButton butCoorSelect = new JButton(ArtiActions.selectCoors);
			this.add(butCoorSelect);
			butCoorSelect.setBounds(convertX(25), convertY(690), convertX(300),convertY( 40));
			
			coordinatesDefinerDialog=new CoorDefDialog(this);
			
			JLabel coorsLabel = new JLabel("Position: ",
					javax.swing.JLabel.LEFT);
			coorsLabel.setBounds(convertX(350), convertY(700), convertX(80),convertY( 25));
			this.add(coorsLabel);
			
			final JLabel coorsXLabel = new JLabel("",
					javax.swing.JLabel.LEFT);
			this.add(coorsXLabel);
			
			JTextField xCoorTField = new JTextField(((Float)(float)WaterMarkModel.getCompressXDistance()).toString(), 7){
				public void paint(Graphics g){					
					this.setText( ((Float)(float)WaterMarkModel.getCompressXDistance()).toString() );
				//	coorsXLabel.setText(WaterMarkModel.isCompressXDistanceFromLeft() ?"on the left":"on the right");
					super.paint(g);
				}				
			};
			xCoorTField.setBounds(convertX(450), convertY(700), convertX(40),convertY( 25));
			coorsXLabel.setBounds(convertX(495), convertY(700), convertX(60),convertY( 25));
			
			xCoorTField.setEditable(false);
			this.add(xCoorTField);
			
			final JLabel coorsYLabel = new JLabel("",
					javax.swing.JLabel.LEFT);
			this.add(coorsYLabel);
			
			JTextField yCoorTField = new JTextField( ((Float)(float)WaterMarkModel.getCompressYDistance()).toString() , 7){
				public void paint(Graphics g){					
					this.setText( ((Float)(float)WaterMarkModel.getCompressYDistance()).toString() );
				//	coorsYLabel.setText(WaterMarkModel.isCompressYDistanceFromTop()?"on the top":"on the bottom");
					super.paint(g);
				}				
			};
			yCoorTField.setBounds(convertX(570), convertY(700), convertX(40),convertY( 25));
			coorsYLabel.setBounds(convertX(615), convertY(700), convertX(60),convertY( 25));
			yCoorTField.setEditable(false);
			this.add(yCoorTField);
			
			//coordinatesDefinerDialog.setSize(fullscreenFotoImg.getAppearanceImg().getWidth(null), fullscreenFotoImg.getAppearanceImg().getHeight(null)+30);
			
			
			butBeginWMarking = new JButton(ArtiActions.BeginWMarking);
			this.add(butBeginWMarking);
			butBeginWMarking.setBounds(convertX(25), convertY(760), convertX(300),convertY( 45));


			butExit = new JButton(ArtiActions.exit);
			this.add(butExit);
			butExit.setBounds(convertX(390), convertY(760), convertX(300),convertY( 45));

//			processingLabel = new JLabel("Choose the target dir...",
//					javax.swing.JLabel.LEFT);
//
//			processingLabel.setBounds(convertX(25), convertY(810), convertX(300),convertY( 35));
//			this.add(processingLabel);
		}

		/* drawing the image */

	}
	
//	public  void run(){
//		 Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
//		 while(Thread.currentThread() == processingLabelPaintThread){
//			 ArtiMarkFrame.processingLabel.repaint();
//		 }    		 
//	 };

}

class TransparentBackground extends JComponent {

	private int rectWidth = 16;
	private int rectHeight = 16;
	private Color lightColor = new Color(255, 255, 255);
	private Color darkColor = new Color(205, 205, 205);

	public void paint(Graphics g) {

		// g=(Graphics2D)g;

		int width = g.getClipBounds().width;
		int height = g.getClipBounds().height;

		for (int i = 0; i < height; i += 16) {
			for (int j = 0; j < width; j += 16) {
				if ((j / 16 + i / 16) % 2 == 0) {
					g.setColor(lightColor);
				} else {
					g.setColor(darkColor);
				}
				g.fillRect(j, i, rectWidth, rectHeight);
			}
		}
	}
}

class MenuBarCreator{
	static public JMenuBar createMenu(){
		/*
		 * JMenuItem exitItem=new JMenuItem("exit"); fileMenu.add(exitItem);
		 */
		
		JMenuBar menuBar = new JMenuBar();		
		  JMenu fileMenu = new JMenu("File");
		  menuBar.add(fileMenu);
		  {
			  fileMenu.add( ArtiActions.findWatermarkFile );
			  fileMenu.add( ArtiActions.findDirectoryInput );
			  
			  fileMenu.addSeparator();
			  JMenuItem exitItem = fileMenu.add(ArtiActions.exit);			  
		  }
		  JMenu editMenu = new JMenu("Edit");
		  menuBar.add(editMenu);
		  {
			  editMenu.add( ArtiActions.selectShapeColor  );
			  editMenu.add( ArtiActions.selectCoors );			  
			  editMenu.addSeparator();			  		  
		  }
		  JMenu aboutMenu = new JMenu(ArtiActions.aboutDgShow/*"About box"*/);
		 menuBar.add(aboutMenu);
		
		
		return menuBar;		
	};	
}
/*
class AboutDialog extends JDialog{
	public AboutDialog(JFrame owner){
		super(owner,"About",true);
		add(new JLabel("mArt"));
		

		JButton ok = new JButton("Ok");
		ok.addActionListener(
				new ActionListener(){
				    public void actionPerformed(ActionEvent e){
				    	setVisible(false);				    	
				    };					
				}
		);
		
		JPanel panel= new JPanel();		
		panel.add(ok);
		add(panel,BorderLayout.SOUTH);
		setSize(ArtiMarkFrame.convertX(500),ArtiMarkFrame.convertY(400));
		owner.setSize(ArtiMarkFrame.convertX(500),ArtiMarkFrame.convertY(400));
	}	
	
};*/
