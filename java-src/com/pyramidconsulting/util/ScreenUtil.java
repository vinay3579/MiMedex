package com.pyramidconsulting.util;
import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

public class ScreenUtil {

	private static final Logger logger = Logger.getLogger(ScreenUtil.class);
	public static void captureScreen(final File file, final String fileType) {
		try{
		final boolean isCaptured = captureScreenToClipBoard();
		file.getParentFile().mkdirs();
		try { Thread.sleep(500); } catch (final InterruptedException e) {}
		if(isCaptured) {
			final Image image = getClipboard();
			saveImageToFile(image, file, fileType);
		}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	public static boolean captureScreenToClipBoard() {
		try{
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (final AWTException e) {
			logger.error("Could not get Robot");
			return false;
		}
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_PRINTSCREEN);
		robot.keyRelease(KeyEvent.VK_PRINTSCREEN);
		robot.keyRelease(KeyEvent.VK_ALT);
		logger.debug("Screen captured into clipboard");
		return true;
		}
		catch(Exception e)
		{
			System.out.println(e);
			return false;
		}
	}


	private static void saveImageToFile(final Image image, final File file, final String fileType) {
		try {
			final BufferedImage bufferedImage = toBufferedImage(image);
			ImageIO.write(bufferedImage, fileType, file);
		} catch(final IOException e) {
			System.out.println("Write error for " + file.getPath() +
					": " + e.getMessage());
		}
	}


	public static Image getClipboard() {
		final Transferable trans = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		Image image = null;
		try {
			if (trans != null && trans.isDataFlavorSupported(DataFlavor.imageFlavor)) {
				image = (Image)trans.getTransferData(DataFlavor.imageFlavor);
			}
		} catch (final UnsupportedFlavorException e) {
			image = null;
		} catch (final IOException e) {
			image = null;
		}
		return image;
	}

	private static BufferedImage toBufferedImage(final Image src) {
		final int w = src.getWidth(null);
		final int h = src.getHeight(null);
		final int type = BufferedImage.TYPE_INT_RGB;
		final BufferedImage dest = new BufferedImage(w, h, type);
		final Graphics2D g2 = dest.createGraphics();
		g2.drawImage(src, 0, 0, null);
		g2.dispose();
		return dest;
	}
}