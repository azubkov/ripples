package com.artit.ripples;


import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

class OpenerFromFile implements ImageOpener{
	public Image open(String fullpath){
//		System.out.println("opening ++"+fullpath);
		Image image = null;
		try {
			// Read from a file
			File file = new File(fullpath);
			image = ImageIO.read(file);
		}catch (IOException e) {
            e.printStackTrace();
        }
		return image;		
	};	
}
