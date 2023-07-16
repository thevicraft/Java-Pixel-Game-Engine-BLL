package com.javapixelgame.game.test;

import java.awt.Color;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.javapixelgame.game.resourcehandling.Images;
import com.javapixelgame.game.resourcehandling.TextureID;
import com.javapixelgame.game.util.ImageUtil;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

public class GLTest implements GLEventListener {

	public float rotation;

	@Override
	public void init(GLAutoDrawable arg0) {
		final GL2 gl = arg0.getGL().getGL2();

		gl.glClearColor(0, 0, 0, 1);

		gl.glEnable(GL2.GL_TEXTURE_2D);

		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);

		// allows transparent textures
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();

		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		// Clear The Screen And The Depth Buffer
		gl.glLoadIdentity(); // Reset The View

//		gl.glViewport(100, 100, 100, 100);

//		// triangle rotation
//		gl.glRotatef(rotation, 0.0f, 1.0f, 0.0f);
//
//		gl.glBegin(GL2.GL_TRIANGLES);
//		gl.glBegin(GL2.GL_POLYGON);
//		gl.glColor3f(0, 1, 1);
//		gl.glVertex2d(-0.60, -0.50);
//		gl.glVertex2d(0, 0.50);
//		gl.glVertex2d(0.60, -0.50);
//		gl.glEnd();
//
//		gl.glFlush();
//		// Assign the angle

		drawImage(ImageUtil.toBufferedImage(Images.getPicture(TextureID.ICON).getImage(), Transparency.TRANSLUCENT), gl,
				0, 0, 200, 200, null, rotation);

		drawImage(ImageUtil.toBufferedImage(Images.getPicture(TextureID.ICON).getImage(), Transparency.TRANSLUCENT), gl,
				100, 0, 200, 200, null, rotation);

		rotation += 1f;

		gl.glFlush();

//		gl.glEnable(GL2.GL_LIGHTING);
//		gl.glEnable(GL2.GL_LIGHT0);
//		gl.glEnable(GL2.GL_DEPTH_TEST);

//		float[] ambientLight = { 0f, 0f, 1f, 0f };
//		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);
//
//		float[] specularLight = { 1f, 0f, 0f, 0f };
//		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specularLight, 0);
//
//		float[] diffuseLight = { 1f, 0f, 0f, 0f };
//		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0);
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {

	}

	@Override
	public void dispose(GLAutoDrawable arg0) {

	}

	public static void drawImage(BufferedImage i, GL2 gl, int displayX, int displayY) {
		drawImage(i, gl, displayX, displayY, i.getWidth(), i.getHeight(), null, 0);
	}

	public static void drawImage(BufferedImage i, GL2 gl, int displayX, int displayY, int displayWidth,
			int displayHeight) {
		drawImage(i, gl, displayX, displayY, displayWidth, displayHeight, null, 0);
	}

	public static void drawImage(BufferedImage i, GL2 gl, int displayX, int displayY, int displayWidth,
			int displayHeight, Color color) {
		drawImage(i, gl, displayX, displayY, displayWidth, displayHeight, color, 0);
	}

	public static void drawImage(BufferedImage i, GL2 gl, int displayX, int displayY, int displayWidth,
			int displayHeight, Color color, float rotation) {
		// make the texture
		gl.glViewport(displayX, gc.getHeight() - displayY - displayHeight, displayWidth, displayHeight);
		float x = 0;
		float y = 0;
		float width = 2;
		float height = 2;

		gl.glClearColor(0, 0, 0, 1);


		// texture from
		Texture t = AWTTextureIO.newTexture(gp, i, true);


		// remove blur of scaling interpolation
		t.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);

		// -----------------------

		gl.glBindTexture(GL2.GL_TEXTURE_2D, t.getTextureObject());

		gl.glTranslatef(x, y, 0);
		gl.glRotatef(rotation, 0, 1, 0);

		if (color != null)
			gl.glColor4f(color.getRed() / 255, color.getGreen() / 255, color.getBlue() / 255, color.getAlpha() / 255);
		else
			gl.glColor4f(1, 1, 1, 1);


		gl.glBegin(GL2.GL_QUADS);

		gl.glTexCoord2f(0, 1);
		gl.glVertex2f(-width / 2, -height / 2);

		gl.glTexCoord2f(1, 1);
		gl.glVertex2f(width / 2, -height / 2);

		gl.glTexCoord2f(1, 0);
		gl.glVertex2f(width / 2, height / 2);

		gl.glTexCoord2f(0, 0);
		gl.glVertex2f(-width / 2, height / 2);

		gl.glEnd();
		gl.glFlush();

		gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);

//		gl.glRotatef(rotation, 0, 0, 1);
		gl.glRotatef(-rotation, 0, 1, 0);
		gl.glTranslatef(-x, -y, 0);
	}

	private static final GLProfile gp = GLProfile.get(GLProfile.GL2);

	private static FPSAnimator animator;

	private static GLCanvas gc;

	public static void mainTest() {

		GLCapabilities cap = new GLCapabilities(gp);

		gc = new GLCanvas(cap);
		GLTest bl = new GLTest();
		gc.addGLEventListener(bl);
		gc.setSize(400, 400);

		final JFrame frame = new JFrame("JOGL Line");
		frame.add(gc);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(500, 400);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		animator = new FPSAnimator(gc, 400, true);
		animator.start();

	}

}
