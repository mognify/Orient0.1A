package control;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.KeyStroke;

import orient.Orient;

public class KeyboardController implements ClipboardOwner{
	private static Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	
	protected static void paste() throws AWTException {
		Orient.print("Pasting...");
		
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		
		return;
	}
	
	protected static void backspace() throws AWTException{
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_BACK_SPACE);
		robot.keyRelease(KeyEvent.VK_BACK_SPACE);
		
		return;
	}
	
	protected static void backspace(int repeat) throws AWTException{
		for(; repeat > 0; repeat--) backspace();
		
		return;
	}

	/*public static void type(String substring) throws AWTException {
		// TODO Auto-generated method stub
		String temp = "";
		 Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
		    Transferable t = c.getContents(new KeyboardController());
		    if (t == null)
		        return;
		    try {
		        temp = ((String) t.getTransferData(DataFlavor.stringFlavor));
		    } catch (Exception e){
		        e.printStackTrace();
		    }//try
		    
		    StringSelection ss = new StringSelection(substring);
		    c.setContents(ss, (ClipboardOwner) new KeyboardController());
		    
		    paste();
		    
		    c.setContents(new StringSelection(temp), (ClipboardOwner) new KeyboardController());
		    
		    return;
	}*/
	
	public static void clipboardRegex(String regex) throws UnsupportedFlavorException, IOException {
		Orient.print("\nclipboardRegex(" + regex + ")\n");
		
		String temp = (String) clipboard.getData(DataFlavor.stringFlavor); 
		temp.replace(regex, " ");
		
		return;
	}

	public void paste(String substring) throws AWTException, InterruptedException, UnsupportedFlavorException, IOException {
		Orient.print("\npaste(" + substring + ")\n");
		//StringSelection stringSelection = new StringSelection(substring);

		Orient.print("\nBacking up clipboard...");
		String temp = "image deleted by Orient";
		if(!clipboardContainsImage()) {
			temp = (String) clipboard.getData(DataFlavor.stringFlavor); 
			Orient.print("Clipboard contents are: " + temp);
		}else {
			Orient.print("Clipboard contained an image. It's gone now.");
		}
		/*clipboard.getData(DataFlavor.stringFlavor);
		Orient.print("String temp = (String) clipboard.getData(DataFlavor.stringFlavor)");
		String temp = (String) clipboard.getData(DataFlavor.stringFlavor); */
		setClipboard(substring);
		paste();
		setClipboard(temp);
		
		Orient.print("\nLeaving paste(String)\n");
		return;
	}
	
private void setClipboard(String substring) throws UnsupportedFlavorException, IOException {
	Orient.print("\nsetClipboard(" + substring + ")");
	
	boolean good = false;
	StringSelection selectionString = new StringSelection(substring);
	while(!good) {
		Orient.print("Setting clipboard to \"" + selectionString + "\"...");
		clipboard.setContents(selectionString, this);

		Orient.print("Checking clipboard pre-paste...");
		clipboard.getData(DataFlavor.stringFlavor);
		String temp2 = (String) clipboard.getData(DataFlavor.stringFlavor); 
		Orient.print("Clipboard contents: " + temp2);
		
		if(!temp2.equals(substring)) {
			good = false;
			Orient.print("\tFAIL clipboard set attempt; retrying... ");
			}else{
				Orient.print("\n\tSUCCESS clipboard set! Contents: " + (String) clipboard.getData(DataFlavor.stringFlavor) + "\n");
				good = true;
			}
	}
	
	return;
	}

public static boolean clipboardContainsImage() {
	Orient.print("\nclipboardContainsImage()\n");
	
	for(DataFlavor df : clipboard.getAvailableDataFlavors()) {
		if(df.isFlavorTextType())
			return true;
	}
	return false;
}

	public static void copy() throws AWTException {
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_C);
		robot.keyRelease(KeyEvent.VK_C);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		
		return;
	}

	public static void f(String substring) throws AWTException {
		Robot robot = new Robot();
		switch(substring.charAt(0)) {
		case '1':
			robot.keyPress(KeyEvent.VK_F1);
			robot.keyRelease(KeyEvent.VK_F1);
			break;
		case '5':
			robot.keyPress(KeyEvent.VK_F5);
			robot.keyRelease(KeyEvent.VK_F5);
			break;
		case '7':
			robot.keyPress(KeyEvent.VK_F7);
			robot.keyRelease(KeyEvent.VK_F7);
			break;
		}
		
		return;
	}

	public static void enter() throws AWTException {
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		
		return;
	}

	public static void tab() throws AWTException {
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
		
		return;
	}

	public static void selectAll() throws AWTException {
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_A);
		robot.keyRelease(KeyEvent.VK_A);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		
		return;
	}

	public static void newTab() throws AWTException {
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_T);
		robot.keyRelease(KeyEvent.VK_T);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		
		return;
	}

	public static void keyStroke(String substring) throws AWTException {
		KeyStroke x = KeyStroke.getKeyStroke(substring);
		Robot robot = new Robot();
		int length = substring.length();
		for(int i = 0; i < length; i++) {
			Character c = substring.charAt(i);
			if(Character.isUpperCase(c)) robot.keyPress(KeyEvent.VK_SHIFT);
			int keyCode = c;
			robot.keyPress(keyCode);
		}
		
		robot.keyPress(KeyEvent.VK_DOWN);
		robot.keyRelease(KeyEvent.VK_DOWN);
	}

	public static void pressDownKey() throws AWTException {
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_DOWN);
		robot.keyRelease(KeyEvent.VK_DOWN);
		
		return;
	}

	@Override
	public void lostOwnership(Clipboard arg0, Transferable arg1) {
		Orient.println("lostOwnership()");
	}
}
