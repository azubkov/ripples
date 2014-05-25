package com.artit.ripples;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
/**Class to look for the images in your catalog (wo subcatalogues)&nbsp;
 * Available extensions: jpg bmp gif*/
public class ImagesFinder {
	
	private String dirName;//=new File("").getAbsolutePath();
	private List<String> jpgFiles = new ArrayList<String>();
	private List<String> pngFiles = new ArrayList<String>();
	private List<String> gifFiles = new ArrayList<String>();
	
	
	
	public List<String> getFilenames(boolean jpg,boolean png,boolean gif){
		List<String> allFiles = new ArrayList<String>();

		if(jpg)allFiles.addAll(jpgFiles);
		if(png)allFiles.addAll(pngFiles);
		if(gif)allFiles.addAll(gifFiles);
	
		return allFiles;		
	};
	

	public String getRandomFilename(boolean jpg,boolean png,boolean gif){
		 List<String> from=getFilenames(jpg,png,gif);	
		 if(from.size()==0){
			 return null;
		 }
		 String ret=from.get( (int) (Math.random()*from.size()));
	
		 
		return 		ret	;
	};
	
	
	public void setDirectory(String newDir){

		dirName=newDir;
		refreshFilenames();		
	}
	public String getDirectory( ){
		return dirName;		
	}
	/**GETTING IMAGE NAMES IN dir*/
	private void refreshFilenames(){
		
		
		
		File dir=new File(dirName);//.getAbsolutePath()
		dirName=dir.getPath();
		dirName+=System.getProperty("file.separator");
	
		/*filenames=*/
		jpgFiles = new ArrayList<String>();

	    for(String fileName:dir.list(
				   new FilenameFilter(){public boolean accept(File dir, String name)
				   		{
						   int pointPosition=name.lastIndexOf(".")+1;
						   if(pointPosition==0)return false;
						   String extension=name.substring(pointPosition);
						   extension=extension.toLowerCase();
						 /*  System.out.println("extension "+extension);*/	
						   if(extension.equals("jpg") || extension.equals("jpeg") ){
							   return true;
						   }
						   return false;
				   		}
				   }   ) 
		)
		{	    	
	    	jpgFiles.add(fileName);	
		}
		/*{
			String[] fileList = dir.list( 
			new FilenameFilter() {
			   public boolean accept(File dir, String name) {
				   int pointPosition = name.lastIndexOf(".")+1;
				   if(pointPosition == 0) {
					   return false;		 						    
				   }
		 		   String extension=name.substring(pointPosition);
	
		 		   extension.toLowerCase();
				   if(extension.equals("jpg") || extension.equals("jpeg") ){
					   return true;								  
				   }
				   return false;
		   		}
			   }
			);
			jpgFiles = Arrays.asList(fileList);
		}	*/
		
		pngFiles = new ArrayList<String>();
	    for(String fileName:dir.list(
				   new FilenameFilter(){public boolean accept(File dir, String name)
				   		{
						   int pointPosition=name.lastIndexOf(".")+1;
						   if(pointPosition==0)return false;
						   String extension=name.substring(pointPosition);
						   extension=extension.toLowerCase();
						 /*  System.out.println("extension "+extension);*/	
						   if(extension.equals("png") )return true;
						   return false;
				   		}
				   }   ) 
		)
		{	    	
	    	pngFiles.add(fileName);	
		}
	    
	    gifFiles = new ArrayList<String>();
	    for(String fileName:dir.list(
				   new FilenameFilter(){public boolean accept(File dir, String name)
				   		{
						   int pointPosition=name.lastIndexOf(".")+1;
						   if(pointPosition==0)return false;
						   String extension=name.substring(pointPosition);
						   extension=extension.toLowerCase();
						 /*  System.out.println("extension "+extension);*/	
						   if(extension.equals("gif") )return true;
						   return false;
				   		}
				   }   ) 
		)
		{
	    	
	
		gifFiles.add(fileName);
		}
	  /*
	   * 
	   * without the full path!
	   * Mary n Me 023.gif
		Mary n Me 026.gif
		Mary n Me 025.gif
	   * 
	   * */		
		}/*refreshFilenames*/
	public List<String> getJPGfiles() {
		return jpgFiles;
	}

	public List<String> getPNGfiles() {
		return pngFiles;
	}

	public List<String> getGIFfiles() {
		return gifFiles;
	}
	
	
}
