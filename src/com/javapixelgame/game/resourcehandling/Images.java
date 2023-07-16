package com.javapixelgame.game.resourcehandling;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import java.awt.Image;
import java.io.InputStream;
import java.util.ArrayList;

import java.util.List;
import javax.swing.JButton;

import com.javapixelgame.game.handling.ConfigHandler;
import com.javapixelgame.game.log.Console;

/**
 * Class for Image Handling / image loading
 * 
 * @author thevicraft
 * @category Handling
 */
public class Images {

	private static List<ImageIcon> imageList = new ArrayList<ImageIcon>();

	private static List<String> picsValues = new ArrayList<String>();

	public static final String imageFileFormat = "png";

	private static boolean imagesLoaded = false;

	public static ImageIcon unknown;

	/**
	 * loads the resource images from Pictures enum
	 * 
	 * @param window  - corresponding window
	 * @param loading - corresponding JProcessBar that is filled up while loading
	 * @author thevicraft
	 */
	public static void initImages(/* , Loader loading */) {
		unknown = imageFromFileName("loading.png");

		Exception error = null;
		if (ConfigHandler.getConfig(ConfigHandler.debug).equals("true"))
			Console.output("Loading Images ...");
		try {
			for (TextureID d : TextureID.values()) {
				picsValues.add(d.toString().toLowerCase() + "." + imageFileFormat);
				if (ConfigHandler.getConfig(ConfigHandler.debug).equals("true"))
					System.out.println("-- " + d.toString().toLowerCase() + "." + imageFileFormat);
			}

			picsValues.forEach(iterator -> {
				imageList.add(imageFromFileName(iterator));
			});

		} catch (Exception e) {
			error = e;
			Console.error("Failed to load images.");
			Console.error("An error occured whilst trying to load images.");
		}
		if (error == null) {
			if (ConfigHandler.getConfig(ConfigHandler.debug).equals("true"))
				Console.output("Successfully loaded " + imageList.size() + " images.");
			imagesLoaded = true;
		}

	}
	
	public static void unloadAll() {
		imagesLoaded = false;

		imageList.clear();
		imageList = new ArrayList<>();
		
		picsValues.clear();
		picsValues = new ArrayList<>();
		
		unknown = null;
		
		System.gc();
	}

	/**
	 * Returns <b>True</b> if  {@link Images.initImages()}  has been executed
	 * 
	 * @return imagesLoaded - True/False Boolean
	 * @author thevicraft
	 */
	public static boolean isLoaded() {
		return imagesLoaded;
	}

	/**
	 * Returns a loaded ImageIcon from enum value
	 * 
	 * @param icon - enum value of pictures
	 * @return ImageIcon
	 * @author thevicraft
	 */
	public static ImageIcon getPicture(TextureID icon) {

		String picture = icon.toString().toLowerCase();

		int index = picsValues.indexOf(picture + "." + imageFileFormat);

		return imageDefaultInResources(index);

	}

	// ---------------------SCALE
	// PICTURES--------------------------------------------------
	/**
	 * Scales a selected ImageIcon
	 * 
	 * @param icon   - selected Image that will be scaled
	 * @param width  - width of the scaled image
	 * @param height - height of the scaled image
	 * @return ImageIcon - new scaled image
	 * @author thevicraft
	 */
	public static ImageIcon scaleImageIcon(ImageIcon icon, int width, int height) {
		Image img = icon.getImage();
		Image newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(newimg);
	}

	// ---------------------------SCALE DEFAULT
	// PICTURES-------------------------------------
	/**
	 * Scales an ImageIcon from default loaded images
	 * 
	 * @param icon   - enum value of default loaded images
	 * @param width  - width of the scaled image
	 * @param height - height of the scaled image
	 * @return ImageIcon
	 * @author thevicraft
	 */
	public static ImageIcon scaleImageIconFromDefault(TextureID icon, int width, int height) {
		Image img = getPicture(icon).getImage();
		Image newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(newimg);
	}

	private static ImageIcon imageDefaultInResources(int imageId) {
		return imageList.get(imageId);
	}

	/**
	 * Returns ImageIcon from file name, in case it is not loaded or not accessable
	 * at the moment Returns a default image Icon if file name is null
	 * 
	 * @param fileName - file name of the image that will be loaded
	 * @return dummy - Image that is loaded from file Name
	 * @author thevicraft
	 */
	public static ImageIcon imageFromFileName(String fileName) {
		try {
			InputStream is = Images.class.getResourceAsStream("/textures/" + fileName);
			return new ImageIcon(ImageIO.read(is));
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Sets icon on button from loaded images
	 * 
	 * @param container - button that gets the icon
	 * @param image     - enum image value that will be transformed into ImageIcon
	 *                  and put on the button
	 * @author thevicraft
	 */
	@Deprecated
	public static void setButtonIcon(JButton container, TextureID image) {
		container.setIcon(Images.scaleImageIconFromDefault(image, (int) container.getPreferredSize().getHeight(),
				(int) container.getPreferredSize().getHeight()));
	}
}
