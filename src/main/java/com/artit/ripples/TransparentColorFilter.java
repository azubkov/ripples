package com.artit.ripples;


import java.awt.Color;
import java.awt.image.RGBImageFilter;


public class TransparentColorFilter extends RGBImageFilter
{
    private Color color;

    public TransparentColorFilter(Color color)
    {
        super();
        this.color = color;
    }

    public int filterRGB(int x, int y, int rgb)
    {
        if ((rgb | 0xFF000000) == (color.getRGB() | 0xFF000000))
        {
            return 0x00FFFFFF & rgb;
        }
        else
        {
            return rgb;
        }
    }
}
