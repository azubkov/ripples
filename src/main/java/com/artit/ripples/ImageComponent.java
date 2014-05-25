package com.artit.ripples;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JComponent;


public class ImageComponent extends JComponent {

	private Image appearanceImg;
	private Float visibleKoeff = 1f;

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;


		if (appearanceImg == null) {
			return;
		}
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				this.getVisibleKoeff());// WarteringModel
		g2.setComposite(ac);

	//g2.fillRect(20, 20, 100, 100);

		g2.drawImage(appearanceImg, 0, 0, appearanceImg.getWidth(null),
				appearanceImg.getHeight(null), null);
	}

	public Image getAppearanceImg() {
		return appearanceImg;
	}

	public void setAppearanceImg(Image appearanceImg) {
		this.appearanceImg = appearanceImg;
		this.setSize(appearanceImg.getWidth(null),appearanceImg.getHeight(null));
	}

	public void setBounds(int x, int y, int w, int h) {
		super.setBounds(x, y, w, h);
	}

	public Float getVisibleKoeff() {
		return visibleKoeff;
	}

	public void setVisibleKoeff(Float visibleKoeff) {
		this.visibleKoeff = visibleKoeff;
		this.repaint();
	};
}