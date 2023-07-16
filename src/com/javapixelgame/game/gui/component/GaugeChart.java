package com.javapixelgame.game.gui.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

import com.javapixelgame.game.handling.ConfigHandler;
import com.javapixelgame.game.log.Console;

@SuppressWarnings("serial")
public class GaugeChart extends JPanel {

	public static final int NO_VALUE = -1;
	public static final int RAW_VALUE = 0;
	public static final int PERCENTAGE_VALUE = 1;

	private int valueVariant = 0;
	private String textAppendix = "";

	public int getValueVariant() {
		return valueVariant;
	}

	public void setValueVariant(int valueVariant) {
		this.valueVariant = valueVariant;
	}

	public int getGaugeSize() {
		return gaugeSize;
	}

	public void setGaugeSize(int gaugeSize) {
		this.gaugeSize = gaugeSize;
		repaint();
	}

	public Color getColor1() {
		return color1;
	}

	public void setColor1(Color color1) {
		this.color1 = color1;
		repaint();
	}

	public Color getColor2() {
		return color2;
	}

	public void setColor2(Color color2) {
		this.color2 = color2;
		repaint();
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		if (animator.isRunning()) {
			animator.stop();
		}
		if (value < 0) {
			value = 0;
		}
		this.value = value;
		lastAnimate = getValueFixed();
		repaint();
	}

	public int getMaximum() {
		return maximum;
	}

	public void setMaximum(int maximum) {
		this.maximum = maximum;
		repaint();
	}

	private int gaugeSize = 15;
	private Color color1 = new Color(51, 71, 216);
	private Color color2 = new Color(237, 63, 63);
	private int value;
	private int maximum = 100;
	private final Animator animator;
	private float targetAnimate;
	private float currentAnimate;
	private float lastAnimate;

	public GaugeChart(int width, int height) {
//		setFont(new JLabel().getFont().deriveFont(0, 20));
//		setPreferredSize(new Dimension(250, 220));
		setSize(width, height);
		setPreferredSize(getSize());
		setFont(new JLabel().getFont().deriveFont(0, getWidth() * 0.08f));
		setForeground(new Color(60, 60, 60));
		setOpaque(true);
		setDoubleBuffered(true);
		
		setGaugeSize((int) (getWidth() * 0.06f));
		
		TimingTarget target = new TimingTargetAdapter() {
			@Override
			public void timingEvent(float fraction) {
				currentAnimate = (targetAnimate - lastAnimate) * fraction;
				currentAnimate += lastAnimate;
				value = (int) currentAnimate;
				repaint();
			}

			@Override
			public void end() {
				lastAnimate = currentAnimate;
			}
		};
		animator = new Animator(250, target);
		animator.setResolution(0);
		animator.setAcceleration(0.5f);
		animator.setDeceleration(0.5f);
	}

	@Override
	public void paintComponent(Graphics grphcs) {
		super.paintComponent(grphcs);

		Graphics2D g2 = (Graphics2D) grphcs;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

//        g2.clearRect(0, 0, getWidth(), getHeight());

		float spaceBot = 0.15f; // space bot 15%
		float width = getWidth();
		float height = getHeight();
		height += (height * spaceBot);
		float size = Math.min(width, height) - (gaugeSize + 5); // 5 is margin
		float centerSize = 20;
		float x = (width - size) / 2;
		float y = (height - size) / 2;
		float centerX = width / 2;
		float centerY = height / 2;
		float angleStart = -35;
		g2.setColor(new Color(240, 240, 240));
		g2.fill(new Ellipse2D.Float(centerX - centerSize / 2, centerY - centerSize / 2, centerSize, centerSize));
		g2.setStroke(new BasicStroke(gaugeSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
		Shape s = new Arc2D.Double(x, y, size, size, angleStart, 250, Arc2D.OPEN);
		g2.draw(s);
		float angle = getAngleOfValues();
		if (angle > 0) {
			s = new Arc2D.Double(x, y, size, size, angleStart + 250 - angle, angle, Arc2D.OPEN);
			GradientPaint gra = new GradientPaint(0, 0, color1, width, 0, color2);
			g2.setPaint(gra);
			g2.draw(s);
		}
		g2.setStroke(new BasicStroke(1f));
		// Create line
		float len = 20;
		float space = 250 / len;
		float start = angleStart + 7;
		float angleSize = (size / 2f) - gaugeSize;
		g2.setColor(new Color(200, 200, 200));
		for (int i = 1; i <= len; i++) {
			Loca p = getLocation(start, angleSize);
			g2.fill(new Ellipse2D.Float(centerX + p.x - 2, centerY - p.y - 2, 4, 4));
			start += space;
		}
		// Create pointer
		float ang = angleStart + 250 - angle;
		g2.setColor(Color.red);
		Path2D p = new Path2D.Double();
		Loca end = getLocation(ang, angleSize);
		Loca right = getLocation(ang - 90, 5);
		Loca left = getLocation(ang + 90, 5);
		p.moveTo(centerX + left.x, centerY - left.y);
		p.lineTo(centerX + end.x, centerY - end.y);
		p.lineTo(centerX + right.x, centerY - right.y);
		g2.fill(p);
		g2.fill(new Ellipse2D.Float(centerX - 5, centerY - 5, 10, 10));
		g2.setColor(new Color(240, 240, 240));
		g2.fill(new Ellipse2D.Float(centerX - 2, centerY - 2, 4, 4));
		drawText(g2, centerX, centerY, angleSize);
	}

	private void drawText(Graphics2D g2, float x, float y, float size) {
		g2.setColor(getForeground());
		String text = "";
		if (getValueVariant() == PERCENTAGE_VALUE) {
			float n = ((float) getValueFixed()) / ((float) maximum) * 100f;
			text = String.valueOf((int) n) + "%";
		} else if (getValueVariant() == RAW_VALUE) {
			text = String.valueOf(getValueFixed());
		}

		if (getTextAppendix().length() > 0) {
			text = text + " " + getTextAppendix();
		}

		FontMetrics ft = g2.getFontMetrics();
		Rectangle2D r2 = ft.getStringBounds(text, g2);
		g2.drawString(text, (float) (x - r2.getWidth() / 2f), (float) (y + size - r2.getHeight()));
	}

	private float getAngleOfValues() {
		float max = maximum;
		float v = getValueFixed();
		float n = v / max * 100f;
		float angle = n * 250 / 100f;
		return angle;
	}

	private int getValueFixed() {
		return value > maximum ? maximum : value;
	}

	private Loca getLocation(float angle, float size) {
		double x = Math.cos(Math.toRadians(angle)) * size;
		double y = Math.sin(Math.toRadians(angle)) * size;
		return new Loca((float) x, (float) y);
	}

	public void setValueWithAnimation(int value) {
		if(ConfigHandler.getConfig(ConfigHandler.animate_gauges).equals("false")) {
			setValue(value);
			return;
		}
		if(this.value == value)return;
		if (value < 0) {
			value = 0;
		}
		this.targetAnimate = value > maximum ? maximum : value;
		if (animator.isRunning()) {
			animator.stop();
		}
		animator.start();
	}

	public String getTextAppendix() {
		return textAppendix;
	}

	public void setTextAppendix(String textAppendix) {
		this.textAppendix = textAppendix;
	}

	private class Loca {

		public Loca(float x, float y) {
			this.x = x;
			this.y = y;
		}

		private float x;
		private float y;
	}
}
