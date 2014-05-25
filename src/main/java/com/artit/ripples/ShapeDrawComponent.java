package com.artit.ripples;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RectangularShape;

import javax.swing.JComponent;


public class ShapeDrawComponent extends JComponent {

	private Float visibleKoeff = 1f;
	private RectangularShape shape;
	private Color shapeColor = new Color(105, 105, 105);


	public ShapeDrawComponent() {
		shape = new java.awt.geom.Ellipse2D.Double();
		//shape.setFrame(0, 0, 0, 0);
	}

	public void paintComponent(Graphics g) {
		if (shape == null) {
			return;
		}
		
		shape.setFrame(0,0,this.getWidth(),this.getHeight());
		Graphics2D g2 = (Graphics2D) g;
		
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				visibleKoeff);// WarteringModel
		g2.setComposite(ac);

		g2.setColor(shapeColor);
		// g2.draw(shape);
		g2.fill(shape);
	}

	public RectangularShape getShape() {
		return shape;
	}

	public void setShape(RectangularShape shape) {
	//	shape.setFrame(this.shape.getFrame());
		this.shape = shape;
		this.repaint();
	}

	public Float getVisibleKoeff() {
		return visibleKoeff;
	}

	public void setVisibleKoeff(Float visibleKoeff) {
		this.visibleKoeff = visibleKoeff;
	}

	public Color getShapeColor() {
		return shapeColor;
	}

	public void setShapeColor(Color shapeColor) {
		this.shapeColor = shapeColor;
	}
}

/*class ImageReturnPanel extends JPanel{
	BufferedImage panelImg	
	
}*/

