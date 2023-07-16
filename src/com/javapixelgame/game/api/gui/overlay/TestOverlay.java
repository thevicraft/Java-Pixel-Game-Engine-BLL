package com.javapixelgame.game.api.gui.overlay;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.util.Random;

import javax.swing.SwingConstants;

import com.javapixelgame.game.api.entity.car.Engine;
import com.javapixelgame.game.gui.component.Button;
import com.javapixelgame.game.gui.component.GaugeChart;
import com.javapixelgame.game.gui.component.Slider;

@SuppressWarnings("serial")
public class TestOverlay extends AbstractOverlay{

	public TestOverlay(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub
		setDisableKeyInputWhenOpen(false);
	}

	@Override
	public void onWorldTick(int tick) {
		// TODO Auto-generated method stub
		repaint();
		if(tick % 2 == 0) {
			gauge.setValueWithAnimation(engine.getRPM());
			
		}

//		if(tick != 1)return;
		
//		gauge.setValue(new Random().nextInt(gauge.getMaximum()));
	}

	@Override
	public void onOpen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void paintOverlay(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}
	
	private GaugeChart gauge;
	private Slider gas_pedal;
	
	private Button on;
	private Button off;
	private Engine engine;
	
	@Override
	protected void init() {
		setComponentAlignment(GridBagConstraints.NONE);
		setGuiAlignment(SwingConstants.RIGHT);
		
		engine = new Engine(8000);
		
		gauge = new GaugeChart(200,200);
		gauge.setColor1(Color.green);
		gauge.setColor2(Color.red);
		gauge.setMaximum(engine.getMaxRPM());
		gauge.setValueVariant(GaugeChart.RAW_VALUE);
		gauge.setTextAppendix("RPM");
		
		gas_pedal = new Slider("gas", getWidth());
		gas_pedal.setMaximum(engine.getMaxRPM());
		gas_pedal.setValueVariant(Slider.PERCENTAGE_VALUE);
		
		gas_pedal.addChangeListener(l -> {
			engine.setGas((float)gas_pedal.getValue() / (float)gas_pedal.getMaximum());
		});
		
		on = new Button("ON", getWidth());
		off = new Button("OFF", getWidth());
		
		on.addActionListener(a -> {
			engine.start();
		});
		
		off.addActionListener(a -> {
			engine.stop();
		});
		
		addComponent(gauge, 1, 1);
		addComponent(gas_pedal, 1, 2);
		
		addComponent(on, 1, 3);
		addComponent(off, 1, 4);
	}

}
