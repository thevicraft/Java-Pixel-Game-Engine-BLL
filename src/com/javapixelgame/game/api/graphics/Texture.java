package com.javapixelgame.game.api.graphics;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

import javax.swing.ImageIcon;

import com.javapixelgame.game.log.Console;
import com.javapixelgame.game.resourcehandling.AnimatedTextureID;
import com.javapixelgame.game.resourcehandling.Images;
import com.javapixelgame.game.resourcehandling.TextureID;
import com.javapixelgame.game.resourcehandling.TextureLoader;
import com.javapixelgame.game.resourcehandling.world.WorldPackage;
import com.javapixelgame.game.util.ImageUtil;
import com.javapixelgame.game.util.ResourceUtil;

public class Texture implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	transient private Image image;
	transient private ImageIcon imageicon;
	transient private Image mirrorImage;

	protected int width;
	protected int height;
	protected double aspect;
	protected boolean animated = false;

//	private String name;
	private TextureID id;
	private AnimatedTextureID animated_id;

	transient private List<Image> images = new ArrayList<>();
	transient private List<Image> mirrored = new ArrayList<>();
	transient private List<ImageIcon> icons = new ArrayList<>();
	transient private List<Image> imgQualityCache = new ArrayList<>();
	transient private List<Image> mirroredQualityCache = new ArrayList<>();

	private boolean bufferVariant = false;
//	public Texture(String name) {
//		this.name = name;
//		setAnimated(true);
//		loadAnimatedTexture(name);
//	}

	public Texture(TextureID img) {
		id = img;
		loadTexture(img);
	}

	public Texture(AnimatedTextureID name) {
		this.animated_id = name;
		setAnimated(true);
		loadAnimatedTexture(name.toString());
	}

	public Texture(AnimatedTextureID name, String variant_buffer) {
		bufferVariant = true;
		this.animated_id = name;
		setAnimated(true);
		loadAnimatedTexture(name.toString());
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
		oos.writeObject(isAnimated());
		if (isAnimated())
			oos.writeObject(get_animated_id());
		else
			oos.writeObject(get_unanimated_id());
		oos.writeObject(width);
		oos.writeObject(height);
	}

	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
		if ((boolean) ois.readObject()) {
			loadAnimatedTexture(((AnimatedTextureID) ois.readObject()).toString());
		} else {
			loadTexture((TextureID) ois.readObject());
		}
		int width = (int) ois.readObject();
		int height = (int) ois.readObject();
		optimize(width, height);
	}

	private void loadTexture(TextureID img) {
		imageicon = Images.getPicture(img);
		image = getIcon().getImage();
		setAnimated(false);
		setWidth(getIcon().getIconWidth());
		setHeight(getIcon().getIconHeight());
		mirrorImage = mirror(imageicon);

		aspect = (double) getWidth() / (double) getHeight();

	}

	private void loadAnimatedTexture(String name) {

		images = new ArrayList<>();
		mirrored = new ArrayList<>();
		icons = new ArrayList<>();
		setImgQualityCache(new ArrayList<>());
		setMirroredQualityCache(new ArrayList<>());

//		File file = null;
//		try {
////			file = new File(Texture.class.getResource("/textures/" + name).getFile());
//			file = ResourceUtil.getResourceFile("/textures/" + name);
//		} catch (Exception e1) {
//			Console.error("unable to locate folder of animated " + name);
//			e1.printStackTrace();
//		}

		if (isBufferVariant() /*&& file.isDirectory()*/) {

			Scanner scanner = null;

			List<String> lines = new ArrayList<>();

			// File txtConfig = new File(Texture.class.getResource("/textures/" + name +
			// "/declaration.txt").toURI());// new
			InputStream txtConfig = WorldPackage.class.getResourceAsStream("/textures/" + name + "/declaration.txt");
			scanner = new Scanner(txtConfig);
			while (scanner.hasNext()) {

				String line = scanner.nextLine();
				if (line.length() > 0) {
					lines.add(line);
				}
			}
			scanner.close();
			lines.forEach(line -> {
				ImageIcon icon = new ImageIcon(Texture.class.getResource("/textures/" + name + "/" + line));
				if (icon.getIconWidth() > 0) {
					icons.add(icon);
					images.add(icon.getImage());
					mirrored.add(mirror(icon));
				} else {
					icons.add(Images.unknown);
					images.add(Images.unknown.getImage());
					errorCouldNotBeFound(line, "/textures/" + name + "/" + line);
				}
			});
			if (lines.size() == 0) {
				icons.add(Images.unknown);
				images.add(Images.unknown.getImage());
			}
			lines.clear();

			setWidth(icons.get(0).getIconWidth());
			setHeight(icons.get(0).getIconHeight());
			aspect = (double) getWidth() / (double) getHeight();

			itIcons = icons.listIterator();
			itImages = images.listIterator();
			itMirrored = mirrored.listIterator();

			getImgQualityCache().addAll(images);
			getMirroredQualityCache().addAll(mirrored);
			return;
		}
		if (!isBufferVariant()) {
			images = new ArrayList<>();
			mirrored = new ArrayList<>();
			icons = new ArrayList<>();

			images.addAll(getImgQualityCache());
			mirrored.addAll(getMirroredQualityCache());

			for (int i = 0; i < images.size(); i++) {
				icons.add(i, new ImageIcon(images.get(i)));
			}
			setWidth(icons.get(0).getIconWidth());
			setHeight(icons.get(0).getIconHeight());
			aspect = (double) getWidth() / (double) getHeight();

			itIcons = icons.listIterator();
			itImages = images.listIterator();
			itMirrored = mirrored.listIterator();
		}
	}

	public void optimize(int width, int height) {
		if (isAnimated()) {
			setWidth(width);
			setHeight(height);
			for (int i = 0; i < images.size(); i++) {
				images.set(i, ImageUtil.scale(getImgQualityCache().get(i), width, height, BufferedImage.TYPE_INT_ARGB));
				icons.set(i, new ImageIcon(images.get(i)));
				mirrored.set(i,
						ImageUtil.scale(getMirroredQualityCache().get(i), width, height, BufferedImage.TYPE_INT_ARGB));
			}
			itIcons = icons.listIterator();
			itImages = images.listIterator();
			itMirrored = mirrored.listIterator();
			return;
		}

		image = ImageUtil.scale(Images.getPicture(get_unanimated_id()).getImage(), width, height,
				BufferedImage.TYPE_INT_ARGB);
		imageicon = new ImageIcon(image);
		mirrorImage = mirror(imageicon);
		setAnimated(false);
		setWidth(getIcon().getIconWidth());
		setHeight(getIcon().getIconHeight());
		aspect = (double) getWidth() / (double) getHeight();
	}

	public int getWidthOnRatio(int height) {
		return (int) (height * aspect);
	}

	public int getHeightOnRatio(int width) {
		return (int) (width / aspect);
	}

	transient private ListIterator<ImageIcon> itIcons = null;
	transient private ListIterator<Image> itImages = null;
	transient private ListIterator<Image> itMirrored = null;

	public ImageIcon getIcon() {
		if (isAnimated()) {
			if (!itIcons.hasNext()) {
				while (itIcons.hasPrevious()) {
					itIcons.previous();
				}
			}

			return itIcons.next();
		}
		return imageicon;
	}

	public Image getImage() {
		if (isAnimated()) {
			if (!itImages.hasNext()) {
				while (itImages.hasPrevious()) {
					itImages.previous();
				}
			}
			return itImages.next();
		}
		return image;
	}

	public Image getMirroredImage() {
		if (isAnimated()) {
			if (!itMirrored.hasNext()) {
				while (itMirrored.hasPrevious()) {
					itMirrored.previous();
				}
			}
			return itMirrored.next();
		}
		return mirrorImage;
	}

	public ImageIcon getDisplayIcon() {
		if (isAnimated())
			return icons.get(0);
		return imageicon;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public double getAspect() {
		return aspect;
	}

	public boolean isAnimated() {
		return animated;
	}

	public void setAnimated(boolean animated) {
		this.animated = animated;
	}

	protected void setHeight(int height) {
		this.height = height;
	}

	protected void setWidth(int width) {
		this.width = width;
	}

	private static void errorCouldNotBeFound(String fileName, String path) {
		System.err.println("<===================>");
		System.err.println("Error: " + fileName + " could not be located in " + path);
	}

	public static BufferedImage mirror(ImageIcon icon) {
		BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(),
				BufferedImage.TYPE_INT_ARGB);
		icon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);

		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-icon.getIconWidth(), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		bufferedImage = op.filter(bufferedImage, null);
		return bufferedImage;
	}

	public List<Image> getImgQualityCache() {
		if (isBufferVariant())
			return imgQualityCache;

//		Console.output(TextureLoader.get(animated_id).getImgQualityCache().size());
		return TextureLoader.get(animated_id).getImgQualityCache();
	}

	public List<Image> getMirroredQualityCache() {
		if (isBufferVariant())
			return mirroredQualityCache;

		return TextureLoader.get(animated_id).getMirroredQualityCache();
	}

	public void setImgQualityCache(List<Image> imgQualityCache) {
		this.imgQualityCache = imgQualityCache;
	}

	public void setMirroredQualityCache(List<Image> mirroredQualityCache) {
		this.mirroredQualityCache = mirroredQualityCache;
	}

	public TextureID get_unanimated_id() {
		return id;
	}

	public AnimatedTextureID get_animated_id() {
		return animated_id;
	}

	public boolean isBufferVariant() {
		return bufferVariant;
	}
}
