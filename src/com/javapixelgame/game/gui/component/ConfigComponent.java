package com.javapixelgame.game.gui.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JToolTip;

import com.javapixelgame.game.handling.ConfigHandler;
import com.javapixelgame.game.resourcehandling.GameFont;

public final class ConfigComponent {

	private ConfigComponent() {
	}

	@SuppressWarnings("serial")
	public static class SwitchButton extends Button {
		private String value = "";
		private String[] values;
		private String name;
		private int pos = 0;
		private int category;

		public SwitchButton(int category, int width, String[] values, ActionListener changeNotifier) {
			super(ConfigHandler.getName(category), width);
			this.name = ConfigHandler.getName(category);
			this.category = category;
			this.values = values;
			if (values.length == 0) {
				new Exception("Array must have at least 1 element!").printStackTrace();
				return;
			}
			value = values[0];

			if (changeNotifier != null)
				addActionListener(changeNotifier);

			addActionListener((ActionEvent e) -> {
				nextValue();
			});
			setFont(GameFont.getRainyHearts(Font.BOLD, getHeight() * 0.7f));
			loadValueFromConfig();
		}

		private void loadValueFromConfig() {
			String v = ConfigHandler.getConfig(category);
			for (int i = 0; i < values.length; i++) {
				if (v.equals(values[i])) {
					pos = i;
					value = values[pos];
					update();
					return;
				}
			}
		}

		public void nextValue() {
			pos++;
			if (pos >= values.length)
				pos = 0;
			value = values[pos];
			update();
		}

		public void update() {
			setText(name + ": " + value);
			ConfigHandler.setConfig(category, value);
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
			update();
		}

		public int getCategory() {
			return category;
		}

		@Override
		public JToolTip createToolTip() {
			return (new ToolTip(this, Color.DARK_GRAY, Color.white, getFont().getSize2D() * 0.75f));
		}

	}

	@SuppressWarnings("serial")
	public static class ConfigSlider extends Slider {
		private int category;
		private ActionListener changeNotifier;

		public ConfigSlider(int category, int min_value, int max_value, int width, ActionListener changeNotifier) {
			super(ConfigHandler.getName(category), width);
			this.category = category;
			setMinimum(min_value);
			setMaximum(max_value);
			setValue(Integer.parseInt(ConfigHandler.getConfig(category)));
			update();
			this.changeNotifier = changeNotifier;
		}

		@Override
		public void onChange() {
			ConfigHandler.setConfig(category, Integer.toString(getValue()));
			if(changeNotifier != null)
				changeNotifier.actionPerformed(null);
		}

		public int getCategory() {
			return category;
		}

		@Override
		public JToolTip createToolTip() {
			return (new ToolTip(this, Color.DARK_GRAY, Color.white, label.getFont().getSize2D() * 0.75f));
		}
	}
	
	@SuppressWarnings("serial")
	public static class ActionButton extends Button {
		

		public ActionButton(String text, int width, ActionListener onClick, ActionListener changeNotifier) {
			super(text, width);
						
			if (onClick != null)
				addActionListener(onClick);
			
			if (changeNotifier != null)
				addActionListener(changeNotifier);

			setFont(GameFont.getRainyHearts(Font.BOLD, getHeight() * 0.7f));
		}

		@Override
		public JToolTip createToolTip() {
			return (new ToolTip(this, Color.DARK_GRAY, Color.white, getFont().getSize2D() * 0.75f));
		}

	}
	

	@SuppressWarnings("serial")
	public static class ToolTip extends JToolTip {

		public ToolTip(JComponent component, Color background, Color foreground, float size) {
			super();
			setComponent(component);
			setBackground(background);
			setForeground(foreground);
//			setSize(getSize().width*2,getSize().height*2);
			setFont(GameFont.getRainyHearts(Font.PLAIN, size));
			
		}

	}
}
