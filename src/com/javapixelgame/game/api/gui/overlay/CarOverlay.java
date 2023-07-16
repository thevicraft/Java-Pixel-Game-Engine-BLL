package com.javapixelgame.game.api.gui.overlay;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.javapixelgame.game.api.entity.car.Car;
import com.javapixelgame.game.api.entity.car.Engine;
import com.javapixelgame.game.api.gui.PlayerMiscBar.HealthBar;
import com.javapixelgame.game.gui.AbstractGUI;
import com.javapixelgame.game.gui.component.Button;
import com.javapixelgame.game.gui.component.GaugeChart;
import com.javapixelgame.game.gui.component.Slider;
import com.javapixelgame.game.handling.GameHandler;
import com.javapixelgame.game.log.Console;
import com.javapixelgame.game.util.GameUtil;

@SuppressWarnings("serial")
public class CarOverlay extends AbstractOverlay {

	private GaugeChart rpm_gauge;
	private GaugeChart velocity_gauge;
	private Slider gas_pedal;
	private Slider light_slider;

	private KeyTurner key;

	private Button on;
	private Button off;
	private Button eject;

	protected Engine engine;
	protected Car car;

	private HealthBar healthbar;

	public static final CarOverlay parse(AbstractOverlay a) {
		if (a instanceof CarOverlay)
			return (CarOverlay) a;
		new ClassCastException().printStackTrace();
		return null;
	}

	public CarOverlay(Car car, int width, int height) {
		super(width, height);

		this.car = car;
		this.engine = car.getEngine();
		setDisableKeyInputWhenOpen(false);
		setFocusWhenOpen(false);
		setCloseOnKey(false);

		setComponentAlignment(GridBagConstraints.NONE);
		setGuiAlignment(SwingConstants.RIGHT);

		rpm_gauge = new GaugeChart(getWidth() / 2 - 10, getWidth() / 2 - 10);
		rpm_gauge.setColor1(Color.green);
		rpm_gauge.setColor2(Color.red);
		rpm_gauge.setMaximum(engine.getMaxRPM());
		rpm_gauge.setValueVariant(GaugeChart.RAW_VALUE);
		rpm_gauge.setTextAppendix("RPM");
		rpm_gauge.setBackground(Color.black);
		rpm_gauge.setForeground(Color.white);

		velocity_gauge = new GaugeChart(getWidth() / 2 - 10, getWidth() / 2 - 10);
		velocity_gauge.setColor1(Color.white);
		velocity_gauge.setColor2(Color.white);
		velocity_gauge.setMaximum((int) GameUtil.MPStoKMH(car.getMaxSpeedMeterPerTick() * 20d));
		velocity_gauge.setValueVariant(GaugeChart.RAW_VALUE);
		velocity_gauge.setTextAppendix("km/h");
		velocity_gauge.setBackground(Color.black);
		velocity_gauge.setForeground(Color.white);

		gas_pedal = new Slider("", getWidth() - 10);
		gas_pedal.setMaximum(engine.getMaxRPM());
		gas_pedal.setValueVariant(Slider.PERCENTAGE_VALUE);
		gas_pedal.setOrientation(SwingConstants.VERTICAL);

		gas_pedal.addChangeListener(l -> {
			engine.setGas((float) gas_pedal.getValue() / (float) gas_pedal.getMaximum());
		});

		gas_pedal.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {

				if (e.getWheelRotation() < 0) {
					// more gas
					gas_pedal.setValue(gas_pedal.getValue() + gas_pedal.getMaximum() / 30);
					return;
				}
				gas_pedal.setValue(gas_pedal.getValue() - gas_pedal.getMaximum() / 30);
			}
		});

		gas_pedal.setValue((int) (engine.getGas() * gas_pedal.getMaximum()));

		light_slider = new Slider("light", getWidth() - 10);
		light_slider.setMaximum(2);
		light_slider.setOrientation(SwingConstants.VERTICAL);
		light_slider.addChangeListener(l -> {
			car.setLightState(light_slider.getValue());
		});

		light_slider.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {

				if (e.getWheelRotation() < 0) {
					// more gas
					light_slider.setValue(light_slider.getValue() + 1);
					return;
				}
				light_slider.setValue(light_slider.getValue() - 1);
			}
		});

		light_slider.setValue(car.getLightState());

		AbstractGUI pedals = new AbstractGUI(getWidth() - 10, gas_pedal.getHeight() * 2) {

			@Override
			protected void paintOverlay(Graphics2D g) {
			}

			@Override
			protected void init() {
				gas_pedal.changeSize(getWidth() / 3, getHeight());
				gas_pedal.changeSliderSize(gas_pedal.getWidth() - 5, gas_pedal.getHeight() / 2);
				addComponent(gas_pedal, 2, 1);

				light_slider.changeSize(getWidth() / 3, getHeight());
				light_slider.changeSliderSize(light_slider.getWidth() - 5, light_slider.getHeight() / 2);
				addComponent(light_slider, 1, 1);

//				addComponent(key, 3, 1);
			}
		};

		key = new KeyTurner(gas_pedal.getHeight(), engine);
//		on = new Button("ON", getWidth() - 10);
//		off = new Button("OFF", getWidth() - 10);
//
//		on.addActionListener(a -> {
//			engine.start();
//		});
//
//		off.addActionListener(a -> {
//			engine.stop();
//		});

		eject = new Button("eject", getWidth() - 10);
		eject.addActionListener(a -> {
			if (GameHandler.getWorld().getPlayer().isRiding()
					&& GameHandler.getWorld().getPlayer().getVehicle() instanceof Car)
				GameHandler.getWorld().getPlayer().getVehicle().eject();
		});

		AbstractGUI gauges = new AbstractGUI(getWidth() - 10, getWidth() / 2 - 10) {

			@Override
			protected void paintOverlay(Graphics2D g) {
			}

			@Override
			protected void init() {
				addComponent(rpm_gauge, 1, 1);
				addComponent(velocity_gauge, 2, 1);
			}
		};

		healthbar = new HealthBar(getWidth() - 10, eject.getHeight() / 2);
		healthbar.setMaxLife(car.getMaxHealthPoints());
		healthbar.updateLife(car.getHealthPoints());
		healthbar.setAttributesVisible(false);

		rpm_gauge.setValue(engine.getRPM());
		velocity_gauge.setValue((int) GameUtil.getVelocityKMH(car.getDrivenVelocity()));

		addComponent(healthbar, 1, 0);

		addComponent(gauges, 1, 1);
		addComponent(pedals, 1, 2);

//		addComponent(on, 1, 3);
//		addComponent(off, 1, 4);
		addComponent(getSeparator(healthbar.getWidth(), healthbar.getHeight()), 1, 3);
		addComponent(key, 1, 4);
		addComponent(getSeparator(healthbar.getWidth(), healthbar.getHeight()), 1, 5);
		addComponent(eject, 1, 6);
	}

	@Override
	public void onWorldTick(int tick) {
		// TODO Auto-generated method stub
//		repaint();
		if (tick % 2 == 0) {
			rpm_gauge.setValueWithAnimation(engine.getRPM());
			velocity_gauge.setValueWithAnimation((int) GameUtil.getVelocityKMH(car.getDrivenVelocity()));
			healthbar.updateLife(car.getHealthPoints());
		}

	}

	public void applyGas(double value) {
		gas_pedal.setValue((int) ((engine.getGas() + value) * gas_pedal.getMaximum()));
	}

	@Override
	public void onOpen() {
	}

	@Override
	public void onClose() {
	}

	@Override
	protected void paintOverlay(Graphics2D g) {
	}

	@Override
	protected void init() {
	}

	public static class KeyTurner extends JPanel {

		private Point2D _mouseDragPosition = new Point2D.Double();

		public static final double off_state = 135;
		public static final double starting_state = 270;

		public static final double on_state = 180;

		private double keyRotation = 0;

		private Color focus = Color.LIGHT_GRAY;

		public KeyTurner(int size, Engine engine) {
			setSize(size, size);
			setPreferredSize(getSize());
			setOpaque(false);

			if (engine.isRunning())
				keyRotation = on_state;
			else
				keyRotation = off_state;

			addMouseMotionListener(new MouseAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
//			        setCursor(new Cursor(Cursor.HAND_CURSOR));
//			        spinStop();

					Point center = new Point(getWidth() / 2, getHeight() / 2);
//			        _mouseDragPosition = e.getPoint();
					/*
					 * Use the equation for angle between two vectors: vector 1 between last
					 * position of mouse and center of circle vector 2 between current position of
					 * mouse and center of circle ("k" is direction coefficient)
					 */
					Point2D mousePos = new Point2D.Double(e.getX(), e.getY());
					double k1 = (_mouseDragPosition.getY() - center.getY())
							/ (_mouseDragPosition.getX() - center.getX());
					double k2 = (mousePos.getY() - center.getY()) / (mousePos.getX() - center.getX());
					double _delta = Math.toDegrees(Math.atan((k2 - k1) / (1 + k2 * k1)));
					if (!Double.isNaN(_delta))
						keyRotation += _delta;
					_mouseDragPosition = mousePos;

					if (keyRotation > starting_state)
						keyRotation = starting_state;
					if (keyRotation < off_state)
						keyRotation = off_state;
					if (keyRotation == off_state)
						engine.stop();
				}
			});

			addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent e) {

					if (keyRotation < starting_state) {
						keyRotation = off_state;
						return;
					}

					if (keyRotation == starting_state) {
						keyRotation = on_state;
						engine.start();
						return;
					}

				}

				@Override
				public void mousePressed(MouseEvent e) {

					if (keyRotation == on_state) {
						keyRotation = off_state;
						engine.stop();
					}
				}

				@Override
				public void mouseExited(MouseEvent e) {
					focus = Color.LIGHT_GRAY;
					setCursor(Cursor.getDefaultCursor());
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					focus = Color.black;
					setCursor(new Cursor(Cursor.HAND_CURSOR));
				}

			});

		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D gr = (Graphics2D) g;
			gr.setColor(Color.GRAY);
			gr.fillOval(0, 0, getWidth(), getHeight());

			gr.setColor(Color.DARK_GRAY);
			gr.fillOval(getWidth() / 4, getHeight() / 4, getWidth() / 2, getHeight() / 2);

			gr.setFont(gr.getFont().deriveFont(getHeight() / 6));
			gr.setColor(Color.white);

			gr.rotate(Math.toRadians(keyRotation), getWidth() / 2, getHeight() / 2);

			gr.setColor(Color.LIGHT_GRAY);
			gr.fillRect(getWidth() / 6, getHeight() / 2 - getHeight() / 12, getWidth() - getWidth() / 3,
					getHeight() / 6);
			gr.setColor(Color.black);
			gr.fillRect(getWidth() / 5, getHeight() / 2 - getHeight() / 16, getWidth() / 2 - getWidth() / 4,
					getHeight() / 8);

			gr.setColor(focus);
			gr.drawRect(getWidth() / 6, getHeight() / 2 - getHeight() / 12, getWidth() - getWidth() / 3,
					getHeight() / 6);

		}
	}

}