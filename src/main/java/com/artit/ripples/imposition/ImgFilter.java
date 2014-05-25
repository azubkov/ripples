package com.artit.ripples.imposition;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface ImgFilter {
    /** @return path for output img*/
    String convertName(String sourcePath);

    Image convertImage(Image sourceImage);        
}
