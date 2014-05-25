package com.artit.ripples;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.*;

import java.awt.geom.*;

public class CoorDefDialog extends JDialog  implements MouseListener, MouseMotionListener
{	
	private JButton ok;
	
	private JPanel panelFullscreenView ;
	private JPanel panelWmark;
	
	private ImageComponent imageCompFullscreen;
	private ImageComponent imageWmark;
	private ShapeDrawComponent shapeWmark;
	private JLabel labelWMCoors;


	private TransparentBackground transparentBackground;
	private double stretchKoeffBigImage = 1;

	private boolean setCoorPanelWmark=false;
	

	   
    private Point pressedAtComponentCoor=new Point(0,0);
    private Point pressedWhenCoor=new Point(0,0);
    
	private double stratchWaterMarkFactor;
    
	public CoorDefDialog(JFrame owner){
		super(owner,"Watermark location",true);
		//this.setUndecorated(true);

		ok=new JButton("Yes");		
		this.setLayout(null);
		this.add(ok);
		ok.addActionListener(new ActionListener(){
			  public void actionPerformed(ActionEvent e){				  
				  CoorDefDialog.this.setVisible(false);
			  };	
		});
		
		labelWMCoors=new JLabel("Drag Move/Doubleclick");		
		this.add(labelWMCoors);
		
		panelFullscreenView = new JPanel();
		panelFullscreenView.setLayout(null);
		
		panelWmark = new JPanel(){
			public void paint(Graphics g){
				this.paintComponents(g);
			}			
		};
		panelWmark.setLayout(null);
		
		shapeWmark = new ShapeDrawComponent();
		imageWmark = new ImageComponent();		
		imageCompFullscreen = new ImageComponent();
		
	 	transparentBackground=new TransparentBackground();		
		//this.add(transparentBackground);
	 	
		panelWmark.add(this.imageWmark );
		panelWmark.add(this.shapeWmark );
		panelFullscreenView.add(panelWmark);
		panelFullscreenView.add(imageCompFullscreen);
		
		//this.add(panelWmark);
		this.add(panelFullscreenView);
		this.add(transparentBackground);	
		
		panelFullscreenView.setVisible(true);
		panelWmark.setVisible(true);
		shapeWmark.setVisible(true);
		imageWmark.setVisible(true);		
		imageCompFullscreen.setVisible(true);
		transparentBackground.setVisible(true);

		
		/*Mouse setup*/
		addMouseMotionListener(this);
		addMouseListener(this);
	}



	public void setImageCompFullscreen(Image imageFullscreen) {
		double widthStrech=1;double heightStrech=1;
		if(WaterMarkModel.getLimitXBigImageSizeForWMResize()>
		imageFullscreen.getWidth(null)){		
			widthStrech=(double)imageFullscreen.getWidth(null)/WaterMarkModel.getLimitXBigImageSizeForWMResize();
		}
		if(WaterMarkModel.getLimitYBigImageSizeForWMResize()>
		imageFullscreen.getHeight(null)){		
			widthStrech=(double)imageFullscreen.getHeight(null)/WaterMarkModel.getLimitYBigImageSizeForWMResize();
		}
		stratchWaterMarkFactor=Math.min(widthStrech, heightStrech);
		
		Dimension screenSize = new Dimension( WaterMarkModel.screenXNow,WaterMarkModel.screenYNow);
		screenSize.width-=50;
		screenSize.height-=30;
		screenSize.height-=ArtiMarkFrame.convertY(50);
		Dimension imageSize=new Dimension(imageFullscreen.getWidth(this),
										  imageFullscreen.getHeight(this));		
		stretchKoeffBigImage=1;
		if(imageSize.width>=screenSize.width || imageSize.height>=screenSize.height  ){			
			double widthStretchKoeff=1;
			while( screenSize.width < ( imageSize.width/ widthStretchKoeff ) ){				
				widthStretchKoeff+=0.01;				
			}			
			double heightStretchKoeff=1;
			while( screenSize.height < ( imageSize.height / heightStretchKoeff ) ){				
				heightStretchKoeff+=0.01;				
			}			
			stretchKoeffBigImage=Math.max(widthStretchKoeff, heightStretchKoeff);
		}
			{
				   BufferedImage bimg;				   
				   bimg =    new BufferedImage((int)(imageSize.width/stretchKoeffBigImage),(int)(imageSize.height/stretchKoeffBigImage),
			   					BufferedImage.TYPE_INT_ARGB);					   
				   Graphics2D g2  = bimg.createGraphics();	
		       	   g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	                            RenderingHints.VALUE_ANTIALIAS_ON);
	        	   g2.drawImage(imageFullscreen, 0, 0, bimg.getWidth(), bimg.getHeight(), 0, 0, imageSize.width, imageSize.height, null);   
	               g2.dispose();
	               imageCompFullscreen.setAppearanceImg(bimg);
			}
			    		
		
		
		/*roll out 25 pix from each side*/

     	imageCompFullscreen.setBounds(0,0, 
     			imageCompFullscreen.getAppearanceImg().getWidth(null),
     			imageCompFullscreen.getAppearanceImg().getHeight(null));
	
		panelFullscreenView.setBounds(0, 0, imageCompFullscreen.getWidth(), imageCompFullscreen.getHeight());
		transparentBackground.setBounds(0,0,imageCompFullscreen.getWidth(), imageCompFullscreen.getHeight());
      	
		
		this.setSize(imageCompFullscreen.getWidth()+5, imageCompFullscreen.getHeight()+60);
		this.setLocation(25, 5);		
		ok.setBounds(ArtiMarkFrame.convertX(25), imageCompFullscreen.getHeight()+5, ArtiMarkFrame.convertX(150), ArtiMarkFrame.convertY(20));		
		labelWMCoors.setBounds(ArtiMarkFrame.convertX(250), imageCompFullscreen.getHeight()+5, ArtiMarkFrame.convertX(550), ArtiMarkFrame.convertY(20));		
	}
	
	
	private void setPanelWmarkLocation(int x,int y){		
		this.panelWmark.setLocation(x,y);
		
//		Point centerOfpanelWmark= new Point(
//				x+panelWmark.getWidth()/2,
//				y+panelWmark.getHeight()/2
//		);
//		
//		WaterMarkModel.setCompressXDistance((double)(centerOfpanelWmark.x )/this.panelFullscreenView.getWidth());
//		WaterMarkModel.setCompressYDistance((double)(centerOfpanelWmark.y )/this.panelFullscreenView.getHeight());

		if(x>(this.panelFullscreenView.getWidth()-x-panelWmark.getWidth() ) ){
			WaterMarkModel.setCompressXDistanceFromLeft(false);
			WaterMarkModel.setCompressXDistance((double)(this.panelFullscreenView.getWidth()-x-panelWmark.getWidth() )/this.panelFullscreenView.getWidth());
		}else{
			WaterMarkModel.setCompressXDistanceFromLeft(true);
			WaterMarkModel.setCompressXDistance((double)(x )/this.panelFullscreenView.getWidth());			
		}
		
		if(y>(this.panelFullscreenView.getHeight()-y-panelWmark.getHeight() ) ){
			WaterMarkModel.setCompressYDistanceFromTop(false);
			WaterMarkModel.setCompressYDistance((double)(this.panelFullscreenView.getHeight()-y-panelWmark.getHeight() )/this.panelFullscreenView.getHeight());
		}else{
			WaterMarkModel.setCompressYDistanceFromTop(true);
			WaterMarkModel.setCompressYDistance((double)(y )/this.panelFullscreenView.getHeight());			
		}
		labelWMCoorsRefresh();
		
		 
	//	 WaterMarkModel.setCoorsOfWaterPanel( this.panelWmark.getLocation()) ;
		
	};
	
	private void labelWMCoorsRefresh(){
		 labelWMCoors.setText("("+pressedWhenCoor.x+", "+pressedWhenCoor.y+
				 ") ("+this.panelWmark.getX()+", "+this.panelWmark.getY()+
				 ") <> ("+this.panelWmark.getWidth()+", "+this.panelWmark.getHeight()+
				 ") [ "+((float)this.panelWmark.getWidth()/this.panelWmark.getHeight())+" ]"+
				 "    border % ("+((float)WaterMarkModel.getCompressXDistance()) +", "+((float)WaterMarkModel.getCompressYDistance())+")"
		 );		
	}
	
    public void mouseDragged(MouseEvent e) {
    	e.consume();  
    	if(this.panelFullscreenView.getMousePosition() != null){  
    		 if(setCoorPanelWmark==true){    			 
    			 this.setPanelWmarkLocation(this.panelFullscreenView.getMousePosition().x-pressedAtComponentCoor.x,this.panelFullscreenView.getMousePosition().y-pressedAtComponentCoor.y);
    		 }
    	}else{    		
//    		setCoorPanelWmark=false;
    	}
    }

    public void mouseMoved(MouseEvent e) {}

 
    
    public void mousePressed(MouseEvent e) {
        e.consume();/*this.panelWmark.getBounds().contains(e.getPoint())*/
        if(this.panelWmark.getMousePosition() != null  ){        	
        	setCoorPanelWmark=true;
        	
        	pressedAtComponentCoor.x = this.panelWmark.getMousePosition().x ;
        	pressedAtComponentCoor.y = this.panelWmark.getMousePosition().y;
        	
        	pressedWhenCoor.x=this.panelWmark.getX();
        	pressedWhenCoor.y=this.panelWmark.getY();
//        	System.out.println(" INPUT "+e.getPoint());
        }

    }

    public void mouseReleased(MouseEvent e) {
        e.consume();
        if(this.panelFullscreenView.getBounds().contains(e.getPoint())){     
        	setCoorPanelWmark=false;
        }
//        repaint();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

   public void mouseClicked(MouseEvent e) {
	   e.consume();
	   if(e.getClickCount()==2 && this.panelFullscreenView.getMousePosition() != null ){
		  	pressedWhenCoor.x=this.panelWmark.getX();
        	pressedWhenCoor.y=this.panelWmark.getY();        	
        	 this.setPanelWmarkLocation(this.panelFullscreenView.getMousePosition().x-panelWmark.getWidth()/2 ,this.panelFullscreenView.getMousePosition().y-panelWmark.getHeight()/2);
	   }

    };
    


/**Fullscreen image compression factor . sk>=1*/
	public double getStretchKoeffBigImage() {
		return stretchKoeffBigImage;
	}



private ImageComponent getImageWmark() {
	return imageWmark;
}



private void setImageWmarkFrom(ImageComponent newImageWmark) {	
	if(newImageWmark==null)return;
  	this.imageWmark.setAppearanceImg(
		ImageManager.resizeImage( (BufferedImage)newImageWmark.getAppearanceImg(),
					(int)(newImageWmark.getAppearanceImg().getWidth(null)/stretchKoeffBigImage),
					(int)(newImageWmark.getAppearanceImg().getHeight(null)/stretchKoeffBigImage) 
				)
		);
    this.imageWmark.setLocation((int)(newImageWmark.getX()/stretchKoeffBigImage), (int)(newImageWmark.getY()/stretchKoeffBigImage));
    this.imageWmark.setSize((int)(imageWmark.getAppearanceImg().getWidth(null)),(int)(imageWmark.getAppearanceImg().getHeight(null)));
    this.imageWmark.setVisibleKoeff(newImageWmark.getVisibleKoeff());
	if(this.shapeWmark.getShape() instanceof java.awt.geom.Arc2D.Double){
		this.shapeWmark.setBounds(this.imageWmark.getBounds());	
	}
}

private ShapeDrawComponent getShapeWmark() {
	return shapeWmark;
}

private void setShapeWmarkFrom(ShapeDrawComponent newShapeWmark) {
	if(newShapeWmark==null)return;
	/*		shape = new java.awt.geom.Arc2D.Double();
					}
					if (selected.equals(ellipse)) {
						shape = new java.awt.geom.Ellipse2D.Double();
					}
					if (selected.equals(rect)) {
						shape = new java.awt.geom.Rectangle2D.Double();
					}
					if (selected.equals(roundrect)) {
						shape = new java.awt.geom.RoundRectangle2D.Double();*/
	RectangularShape shape = null ;
	if(newShapeWmark.getShape() instanceof java.awt.geom.Arc2D.Double){		
		shape = new java.awt.geom.Arc2D.Double();
	}
	if(newShapeWmark.getShape() instanceof java.awt.geom.Ellipse2D.Double){		
		shape = new java.awt.geom.Ellipse2D.Double();
	}
	if(newShapeWmark.getShape() instanceof java.awt.geom.Rectangle2D.Double){		
		shape = new java.awt.geom.Rectangle2D.Double();
	}
	if(newShapeWmark.getShape() instanceof java.awt.geom.RoundRectangle2D.Double){		
		shape = new java.awt.geom.RoundRectangle2D.Double();
	}

	this.shapeWmark.setShape(shape );
	this.shapeWmark.setVisibleKoeff(newShapeWmark.getVisibleKoeff());
	this.shapeWmark.setShapeColor(newShapeWmark.getShapeColor());
	this.shapeWmark.setBounds((int)(newShapeWmark.getX()/stretchKoeffBigImage),(int)(newShapeWmark.getY()/stretchKoeffBigImage),(int)(newShapeWmark.getWidth()/stretchKoeffBigImage)+2,(int)(newShapeWmark.getHeight()/stretchKoeffBigImage)+2);

	if(shape instanceof java.awt.geom.Arc2D.Double){
		this.shapeWmark.setBounds(this.imageWmark.getBounds());	
	}
}

public JPanel getPanelWmark() {
	return panelWmark;
}



public void setPanelWmarkFrom(JPanel newPanelWmark) {	
	this.panelWmark.setSize((int)(newPanelWmark.getWidth()/this.stretchKoeffBigImage),
			(int)(newPanelWmark.getHeight()/this.stretchKoeffBigImage)) ;
	ShapeDrawComponent shapeComp=null;
	ImageComponent imageComp=null;
	Component comps[]=newPanelWmark.getComponents();	
	int fop=0;
	while((shapeComp==null || imageComp==null) && fop<comps.length )
	{	
		if(comps[fop] instanceof ShapeDrawComponent){
			shapeComp = (ShapeDrawComponent)comps[fop];			
		}
		if(comps[fop] instanceof ImageComponent){
			imageComp = (ImageComponent)comps[fop];			
		}		
		fop++;		
	}
	this.setShapeWmarkFrom(shapeComp);
	this.setImageWmarkFrom(imageComp);
	
	/*truncating the panel as small as possible*/
	int leftSpare=Math.min(this.imageWmark.getX(),this.shapeWmark.getX());
	int widthRequed=Math.max(this.imageWmark.getX()+this.imageWmark.getWidth(),this.shapeWmark.getX()+this.shapeWmark.getWidth());
	int topSpare=Math.min(this.imageWmark.getY(),this.shapeWmark.getY());
	int heightRequed=Math.max(this.imageWmark.getY()+this.imageWmark.getHeight(),this.shapeWmark.getY()+this.shapeWmark.getHeight());
	widthRequed-=leftSpare;
	heightRequed-=topSpare;
	this.imageWmark.setLocation(this.imageWmark.getX()-leftSpare ,this.imageWmark.getY()-topSpare);
	this.shapeWmark.setLocation(this.shapeWmark.getX()-leftSpare ,this.shapeWmark.getY()-topSpare);
	this.panelWmark.setSize(widthRequed, heightRequed);	 
	
	/*Cjatie watermarki*/
	
	
//	System.out.println("stratchFactor is set to "+stratchWaterMarkFactor);
	
	
	this.panelWmark.setSize(	
			(int)(this.panelWmark.getWidth()*stratchWaterMarkFactor),
			(int)(this.panelWmark.getHeight()*stratchWaterMarkFactor)
			);
	
	this.imageWmark.setSize(	
			(int)(this.imageWmark.getWidth()*stratchWaterMarkFactor),
			(int)(this.imageWmark.getHeight()*stratchWaterMarkFactor)
			);	
	
  	this.imageWmark.setAppearanceImg(
  			ImageManager.resizeImage( (BufferedImage)this.imageWmark.getAppearanceImg(),
  						(int)(this.imageWmark.getAppearanceImg().getWidth(null)*stratchWaterMarkFactor),
  						(int)(this.imageWmark.getAppearanceImg().getHeight(null)*stratchWaterMarkFactor) 
  					)
  			);
  	
	this.imageWmark.setLocation(	
			(int)(this.imageWmark.getX() *stratchWaterMarkFactor),
			(int)(this.imageWmark.getY()*stratchWaterMarkFactor)
			);
	
	this.shapeWmark.setSize(	
			(int)(this.shapeWmark.getWidth()*stratchWaterMarkFactor),
			(int)(this.shapeWmark.getHeight()*stratchWaterMarkFactor)
			);
	this.shapeWmark.getShape().setFrame (	shapeWmark.getShape().getX(),
			shapeWmark.getShape().getY(),
			(int)(this.shapeWmark.getShape().getWidth()*stratchWaterMarkFactor),
			(int)(this.shapeWmark.getShape().getHeight()*stratchWaterMarkFactor)
			);
	
	this.shapeWmark.setLocation(	
			(int)(this.shapeWmark.getX() *stratchWaterMarkFactor),
			(int)(this.shapeWmark.getY()*stratchWaterMarkFactor)
			);
	
	//limitXBigImageSizeForWMResize
//	System.out.println("isCompressXDistanceFromLeft "+WaterMarkModel.isCompressXDistanceFromLeft());
//	System.out.println("isCompressYDistanceFromTop "+WaterMarkModel.isCompressYDistanceFromTop());
//	
//	System.out.println("WaterMarkModel.getCompressXDistance() "+WaterMarkModel.getCompressXDistance());
//	System.out.println("WaterMarkModel.getCompressYDistance() "+WaterMarkModel.getCompressYDistance());
//	
//	
	
	
	
	int left=0,top=0;
	if(WaterMarkModel.isCompressXDistanceFromLeft()){
		left=(int)(WaterMarkModel.getCompressXDistance()*panelFullscreenView.getWidth());				
	}else{
		left=panelFullscreenView.getWidth()-panelWmark.getWidth()-(int)(WaterMarkModel.getCompressXDistance()*panelFullscreenView.getWidth());	
	}
	
	if(WaterMarkModel.isCompressYDistanceFromTop()){
		top=(int)(WaterMarkModel.getCompressYDistance()*panelFullscreenView.getHeight());				
	}else{
		top=panelFullscreenView.getHeight()-panelWmark.getHeight()-(int)(WaterMarkModel.getCompressYDistance()*panelFullscreenView.getHeight());	
	}
	/*Point centerOfpanelWmark= new Point(0,0);	
	centerOfpanelWmark.x=(int) (WaterMarkModel.getCompressXDistance()*this.panelFullscreenView.getWidth());
	centerOfpanelWmark.y=(int) (WaterMarkModel.getCompressYDistance()*this.panelFullscreenView.getHeight());
	
	int left = centerOfpanelWmark.x-this.panelWmark.getWidth()/2;
	int top = centerOfpanelWmark.y-this.panelWmark.getHeight()/2;	
	*/

//	System.out.println("widthRequed, heightRequed "+widthRequed+" "+heightRequed );
//	System.out.println("panelFullscreenView.getWidth() "+panelFullscreenView.getWidth());
//	System.out.println("panelWmark.getWidth() "+panelWmark.getWidth());
//	System.out.println("left "+left);
//	System.out.println("top "+top);
	
//	this.setPanelWmarkLocation(left,top);
	this.panelWmark.setLocation(left,top);
	labelWMCoorsRefresh();
}

/**please multiply with big-image-height to decompress*/

}



