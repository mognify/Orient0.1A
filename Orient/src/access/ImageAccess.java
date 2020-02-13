package access;

import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import control.ControlEngine;
import orient.Orient;
 
public class ImageAccess implements ClipboardOwner{
	
	public static String resultImgName = "TemplateMatchingOutput.jpg";
	public static double threshold = .9;
	
	/*public static void preloadTemplates() {
		for(String instruction : ControlEngine.instructions) {
			if(instruction.matches("(?s).*\\M\\b.*\\m\\b.*"))
				
		}
	}*/
 
	public static void loadLibraries() {
		Orient.println("loadLibraries()");

	    try {
	        InputStream in = null;
	        File fileOut = null;
	        String osName = System.getProperty("os.name");
	        //String opencvpath = System.getProperty("user.dir");
	        String opencvpath = Orient.getWorkingDirectory() + "\\Orient";
	        if(osName.startsWith("Windows")) {
	            int bitness = Integer.parseInt(System.getProperty("sun.arch.data.model"));
	            if(bitness == 32) {
	                opencvpath+="\\opencv\\x86\\";
	            }
	            else if (bitness == 64) { 
	                opencvpath+="\\opencv\\x64\\";
	            } else { 
	                opencvpath+="\\opencv\\x86\\"; 
	            }           
	        } 
	        else if(osName.equals("Mac OS X")){
	            opencvpath +="mac users should get spanked";
	        }
	        opencvpath=opencvpath.replace("opencv","opencv\\build\\java");
	        System.out.println(opencvpath);
	        System.load(opencvpath + "opencv_java411.dll");
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to load opencv native library", e);
	    }
	}
	
    public static Point screenSearch(String targetName) throws IOException {
    	Orient.println("screenSearch(" + targetName + ")");
    	//System.setProperty("java.library.path", "C:\\Users\\Levi\\Desktop\\Orient\\opencv\\build\\java");
    	//System.loadLibrary("opencv_java411");
        //System.load("C:\\Users\\Levi\\Desktop\\Orient\\opencv\\build\\java\\x64\\opencv_java411");
        
        Mat source=null;
        Mat template=null;
        String filePath=Orient.workingDirectory;
        //Load image file
        try {
            screenshot();
        }catch(CvException cve) {
        	System.out.println("\n\tCouldn't take screenshot");
        }
        String screenshotPath = filePath+"\\Orient\\screenshot.jpg";
        Orient.print(screenshotPath);
        source=Imgcodecs.imread(screenshotPath);
        String targetPath = filePath+"\\Orient\\"+targetName;
        Orient.print(targetPath);
        template=Imgcodecs.imread(targetPath);
    
        
        Mat outputImage=new Mat();    
        int machMethod =  Imgproc.TM_SQDIFF_NORMED;
        //Template matching method
        Imgproc.matchTemplate(source, template, outputImage, machMethod);
 
        MinMaxLocResult mmr = Core.minMaxLoc(outputImage);
        Orient.print("" + mmr.maxVal);
        if(mmr.maxVal < threshold) return null;
        Point matchLoc=mmr.maxLoc;
        //Draw rectangle on result image
        Imgproc.rectangle(source, matchLoc, new Point(matchLoc.x + template.cols(),
                matchLoc.y + template.rows()), new Scalar(0, 255, 0));
        
        Imgcodecs.imwrite(filePath+"\\Orient\\"+resultImgName, source);

        return matchLoc;
    }
    
	public static void screenshot() {
		 { 
			 Orient.println("screenshot()");
		        try { 
		            Thread.sleep(120); 
		            Robot r = new Robot(); 
		  
		            // It saves screenshot to desired path 
		            String path = Orient.workingDirectory + "\\Orient\\screenshot.jpg"; 
		            Rectangle capture = new Rectangle(0, 0, 0, 0);
		            for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
		                capture = capture.union(gd.getDefaultConfiguration().getBounds());
		            }
		            // Used to get ScreenSize and capture image 
		            /*Rectangle capture =  
		            new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());*/ 
		            BufferedImage image = r.createScreenCapture(capture); 
		            ImageIO.write(image, "jpg", new File(path)); 
		            System.out.println("\nScreenshot saved to: " + path); 
		        } 
		        catch (AWTException | IOException | InterruptedException ex) { 
		            System.out.println(ex); 
		        } 
		    } 
	}
    
    public static Image getImageFromClipboard()
    {
        Orient.println("getImageFromClipboard()");
      Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
      if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor))
      {
        try
        {
          return (Image) transferable.getTransferData(DataFlavor.imageFlavor);
        }
        catch (UnsupportedFlavorException | IOException e)
        {
          // handle this as desired
          e.printStackTrace();
        }
      }
      else
      {
        System.err.println("getImageFromClipboard: That wasn't an image!");
      }
      return null;
    }
    
    public static Mat bufferedImageToMat(BufferedImage bi) {
    	Orient.println("bufferedImageToMat(BufferedImage)");
    	  Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
    	  byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
    	  mat.put(0, 0, data);
    	  return mat;
    	}
    
    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    /*
     * assumes jpg if unspecified
     */
	public static Point match(String substring) throws IOException {
		// image to find should come in as the name of the file including extension(?) - optional
		String fileName = extensionCheck(substring);
		// the name of the file must be there,
		// File targetFile;
		// if extension is not provided then the program should search for any file by that name

		Point resultPoint = screenSearch(fileName);
		
		return resultPoint;
	}

	public static Point waitMatch(String substring) throws IOException {
		Point matchFound = null;
		while(matchFound == null) {
			matchFound = match(substring);
		}
		
		return matchFound;
	}
	
	private static String extensionCheck(String substring) {
		Orient.println("extensionCheck(" + substring + ")");
		String fileName = "";
		
		if(substring.contains(".")) {
			//targetFile = new File(Orient.workingDirectory + substring); 
			fileName = substring;
		}else {
			fileName = substring + ".jpg";
		}
		
		return fileName;
	}
	
	public static Point getCenterTM(Point matchLoc, String targetName) throws IOException {
		Orient.println("getCenterTM(Point{" + matchLoc.x + "," + matchLoc.y + "}, " + targetName + ")");
		//Point matchLoc = waitMatch(targetName);

        String targetPath = Orient.workingDirectory+"\\Orient\\"+extensionCheck(targetName);
        File targetFile = new File(targetPath);
        Orient.print(targetFile.getAbsolutePath());
		BufferedImage bimg = ImageIO.read(targetFile);
		int w = bimg.getWidth()/2;
		int h = bimg.getHeight()/2;
		
		Point targetCenter = new Point(matchLoc.x+w, matchLoc.y+h);
		Orient.print(targetCenter.toString());
		
		return targetCenter;
	}

	@Override
	public void lostOwnership(Clipboard arg0, Transferable arg1) {
		Orient.println("lostOwnership");
	}
 
}

/*
 * Direction
 * Reactivity
 * Vicinity
 * Mimicry
 * */
