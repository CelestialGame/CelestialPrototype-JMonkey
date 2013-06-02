package com.celestial.Gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;

public class StretchedImage{

	public static Image stretch(Image image, Insets insets, Dimension dim, int hints){
		//debugcode
		//System.out.println(dim); 

		//load image into bufferedImage
		BufferedImage bi = toBufferedImage(image, hints); 

		//create 2d bufferedImage array to hold the 9 images
		Image[][] img = new Image[3][3]; 

		//split Image into 9 subsections
		img[0][0] = bi.getSubimage(0, 0, insets.left, insets.top);
		img[0][1] = bi.getSubimage(insets.left, 0, bi.getWidth() - insets.left - insets.right, insets.top);
		img[0][2] = bi.getSubimage(bi.getWidth() - insets.right, 0, insets.right, insets.top);
		img[1][0] = bi.getSubimage(0, insets.top, insets.left, bi.getHeight() - insets.top - insets.bottom);
		img[1][1] = bi.getSubimage(insets.left, insets.top, bi.getWidth() - insets.left - insets.right, bi.getHeight() - insets.top - insets.bottom);
		img[1][2] = bi.getSubimage(bi.getWidth() - insets.right, insets.top, insets.right, bi.getHeight() - insets.top - insets.bottom);
		img[2][0] = bi.getSubimage(0, bi.getHeight() - insets.bottom, insets.left, insets.bottom);
		img[2][1] = bi.getSubimage(insets.left, bi.getHeight() - insets.bottom, bi.getWidth() - insets.left - insets.right, insets.bottom);
		img[2][2] = bi.getSubimage(bi.getWidth() - insets.right, bi.getHeight() - insets.bottom, insets.right, insets.bottom);

		//determine the width and height of the sections that will be stretched
		//only the center section will have both dimensions changed
		int w = dim.width - insets.left - insets.right;
		int h = dim.height - insets.top - insets.bottom;

		//Stretch the proper sections 
		img[0][1] = img[0][1].getScaledInstance(w, img[0][1].getHeight(null), hints);
		img[1][0] = img[1][0].getScaledInstance(img[1][0].getWidth(null), h, hints);
		img[1][1] = img[1][1].getScaledInstance(w, h, hints);
		img[1][2] = img[1][2].getScaledInstance(img[1][2].getWidth(null), h, hints);
		img[2][1] = img[2][1].getScaledInstance(w, img[2][1].getHeight(null), hints);

		//for loop is debug code
		//for(int i = 0; i < 3; i++){ 
		//  for(int j = 0; j < 3; j++){
		//      System.out.println(i + " " + j + " " + img[i][j].getWidth() + "," + img[i][j].getHeight());
		//  }
		//}

		//create a new bufferedImage to hold the final image
		BufferedImage finalImage = new BufferedImage(dim.width, dim.height, hints);
		Graphics g = finalImage.getGraphics();
		//draw the peices to the final image
		g.drawImage(img[0][0], 0, 0, null);
		g.drawImage(img[0][1], insets.left, 0, null);
		g.drawImage(img[0][2], dim.width - insets.right, 0, null);
		g.drawImage(img[1][0], 0, insets.top, null);
		g.drawImage(img[1][1], insets.left, insets.top, null);
		g.drawImage(img[1][2], dim.width - insets.right, insets.top, null);
		g.drawImage(img[2][0], 0, dim.height - insets.bottom, null);
		g.drawImage(img[2][1], insets.left, dim.height - insets.bottom, null);
		g.drawImage(img[2][2], dim.width - insets.right, dim.height - insets.bottom, null);   

		return (Image)finalImage;
	}

	// This method returns a buffered image with the contents of an image
	public static BufferedImage toBufferedImage(Image image, int hints) {

		BufferedImage bi = new BufferedImage(image.getWidth(null), image.getHeight(null), hints);
		bi.getGraphics().drawImage(image, 0, 0, null);

		return bi;
	}
}
