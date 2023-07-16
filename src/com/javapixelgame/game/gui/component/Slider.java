package com.javapixelgame.game.gui.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;

import com.javapixelgame.game.api.graphics.Texture;
import com.javapixelgame.game.resourcehandling.GameFont;
import com.javapixelgame.game.resourcehandling.TextureID;

@SuppressWarnings("serial")
public class Slider extends JSlider {
	
	public static final int RAW_VALUE = 0;
	public static final int PERCENTAGE_VALUE = 1;

	private ImageIcon focused;
	private ImageIcon unfocused;
	private ImageIcon overlay;
	private ImageIcon background;
	protected JLabel label = new JLabel();
	private String title = "";
	private int valueVariant = 0;
	private boolean titleVisible = true;
	private String unit = "";
	
	public Slider(String text, int width) {
		setTitle(text);
		prepare(width);
	}
	
	private void displayTitle() {
		if(!isTitleVisible()) {
			label.setText("");
			return;
		}
		
		String value = "";
		switch(getValueVariant()) {
		case RAW_VALUE:
			value = Integer.toString(getValue())+(getUnit().length() > 0 ? " "+getUnit():"");
			break;
		case PERCENTAGE_VALUE:
			value = Integer.toString(getValue()*100/getMaximum()) +"%";
			break;
		default:
			value = Integer.toString(getValue())+(getUnit().length() > 0 ? " "+getUnit():"");
			break;
		}
		String title = getTitle();
		
		label.setText((title.length() > 0 ? title+": ": "")+value);
	}
	
	public void update() {
		displayTitle();
	}

	private void prepare(int width) {
		setForeground(new Color(0,0,0,0));
		setBackground(new Color(0, 0, 0, 0));
		setOpaque(false);
		setFocusable(false);
		setOrientation(JSlider.HORIZONTAL);
		setLayout(null);
		setBorder(null);

		Texture f = new Texture(TextureID.BUTTON_FOCUSED);
		Texture uf = new Texture(TextureID.BUTTON);

		setSize(width, uf.getHeightOnRatio(width));
		setPreferredSize(getSize());
		setPaintTrack(false);


		focused = f.getIcon();
		unfocused = uf.getIcon();
		background = new Texture(TextureID.BUTTON_EMPTY).getIcon();
		overlay = unfocused;
		setUI(new SliderUI(this, 20, (int)(getHeight()*0.8)));

		label.setBounds(0, 0, getWidth(), getHeight());
		label.setFont(GameFont.getRainyHearts(Font.BOLD, getHeight()*0.7f));
		label.setHorizontalAlignment(SwingUtilities.CENTER);
		label.setOpaque(false);
		label.setForeground(Color.white);
		
		add(label);
		
		addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
//				Slider s = (Slider) e.getSource();
				displayTitle();
			}
		});
		
		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				onChange();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				overlay = unfocused;
				repaint();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				overlay = focused;
				repaint();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}
	
	public void changeSize(int width, int height) {
		setSize(width, height);
		setPreferredSize(getSize());
		label.setBounds(0, 0, getWidth(), getHeight());
	}
	
	public void changeSliderSize(int width, int height) {
		setUI(new SliderUI(this, width, height));
	}
	
	public void onChange() {}

	@Override
	protected void paintComponent(Graphics g) {

		Graphics2D graphics = (Graphics2D) g;

		graphics.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);

		super.paintComponent(graphics);

	}

	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
		displayTitle();
	}

	public int getValueVariant() {
		return valueVariant;
	}



	public void setValueVariant(int valueVariant) {
		this.valueVariant = valueVariant;
		displayTitle();
	}

	public boolean isTitleVisible() {
		return titleVisible;
	}



	public void setTitleVisible(boolean titleVisible) {
		this.titleVisible = titleVisible;
		displayTitle();
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	private class SliderUI extends BasicSliderUI {
		/**
		 * 
		 * @param b      - Slider where the UI is applied to
		 * @param width  - width of the slider
		 * @param height - height of the slider
		 */
		public SliderUI(JSlider b, int width, int height) {
			super(b);
			thumbwidth = width;
			thumbheight = height;
		}

		private int thumbwidth;
		private int thumbheight;

		@Override
		protected Dimension getThumbSize() {
			return new Dimension(thumbwidth, thumbheight);
		}

		@Override
		public void paintThumb(Graphics g) {
			Graphics2D gd = (Graphics2D) g;
			gd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//			int v = 10;
//			int aspect = overlay.getIconWidth() / overlay.getIconHeight();
//			int w = aspect * thumbheight;
			gd.drawImage(overlay.getImage(), thumbRect.x, thumbRect.y, thumbwidth / 2 + thumbRect.x,
					thumbheight + thumbRect.y, 0, 0, thumbwidth/2, overlay.getIconHeight(), slider);
			gd.drawImage(
					overlay.getImage(), 
					thumbRect.x + thumbRect.width / 2, 
					thumbRect.y,
					thumbwidth  + thumbRect.x, 
					thumbheight + thumbRect.y,
					overlay.getIconWidth() - thumbwidth/2, 
					0, 
					overlay.getIconWidth(),
					overlay.getIconHeight(), 
					slider
					);
//			gd.setColor(Color.white);
//			gd.drawRect(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
//			gd.drawRect(thumbRect.x+thumbRect.width/2, thumbRect.y, thumbRect.width/2, thumbRect.height);
		}

	}
}