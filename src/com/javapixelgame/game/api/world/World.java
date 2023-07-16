package com.javapixelgame.game.api.world;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JPanel;

import org.omg.CORBA.BooleanSeqHelper;

import com.javapixelgame.game.Main;
import com.javapixelgame.game.api.BackgroundWorker;
import com.javapixelgame.game.api.GameTime;
import com.javapixelgame.game.api.Objective;
import com.javapixelgame.game.api.Player;
import com.javapixelgame.game.api.Tickable;
import com.javapixelgame.game.api.WorldTick;
import com.javapixelgame.game.api.coordinatesystem.CPoint;
import com.javapixelgame.game.api.coordinatesystem.CSystem;
import com.javapixelgame.game.api.entity.Collision;
import com.javapixelgame.game.api.entity.Entity;
import com.javapixelgame.game.api.graphics.Light;
import com.javapixelgame.game.api.graphics.Particle;
import com.javapixelgame.game.api.graphics.ParticleHandler;
import com.javapixelgame.game.api.graphics.WorldModel;
import com.javapixelgame.game.api.graphics.WorldPainter;
import com.javapixelgame.game.api.gui.Camera;
import com.javapixelgame.game.api.gui.Chat;
import com.javapixelgame.game.api.obstacle.Obstacle;
import com.javapixelgame.game.handling.ConfigHandler;
import com.javapixelgame.game.log.Console;
import com.javapixelgame.game.util.ImageUtil;

/**
 * Class {@link World} that provides the Game with a play area where every
 * action takes place, it extends {@link JPanel}, so it acts like one and has
 * nearly the same methods
 * 
 * @author thevicraft
 * @see Game
 */
@SuppressWarnings("serial")
public class World extends JPanel implements Tickable {

	private Graphics2D panel;
	public int map_width, map_height = 0;

	public double aspect_ratio;

	private CSystem coordinates;
	private Point origin;
	private Player player;

	private String saveName;

	private List<Objective> objects = new ArrayList<>();

	private List<Objective> renderObjects = new ArrayList<>();

	private List<Particle> particles = new ArrayList<>();

	public double widthToMapMeterAspectRatio;

	private WorldTick tick = new WorldTick(this, 0.1f);

	private WorldPainter painter;

	private WorldModel model;

//	private Chat chat;

	private ParticleHandler particleHandler = new ParticleHandler(this);

	private Camera camera;

	private GameTime time = new GameTime();

	private BufferedImage lightOverlay;

	private boolean hitboxes = false;

	private boolean daylightCycle = true;

	private List<Objective> remove1 = new ArrayList<>();
	private List<Objective> remove2 = new ArrayList<>();
	private List<Objective> add1 = new ArrayList<>();
	private List<Objective> add2 = new ArrayList<>();

	/**
	 * Constructs the play world of the Game
	 * 
	 * @param model         - {@link WorldModel} that is applied as the playing
	 *                      world
	 * @param pixelPerMeter - the definition of how big the map is displayed, it
	 *                      defines how many pixels per meter on the map it should
	 *                      scale
	 */
	public World(WorldModel model, int pixelPerMeter, int width, int height) {

		this.model = model;
		painter = new WorldPainter(this, model, pixelPerMeter);

		setSize(new Dimension(width, height));

		// DO NOT CHANGE THIS POINT!!!! OTHERWISE THE TILE DETECTING WILL NOT WORK
		origin = new Point(0, this.map_height); // places the origin at the bottom left corner to avoid negative virtual
		// coordinates
		coordinates = new CSystem(origin, 0, this.map_width, 0, this.map_height);

		setLayout(null);
		setDoubleBuffered(true);

		camera = new Camera(this);

		lightOverlay = ImageUtil.getFormatBufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
	}

	public World() {
	}

	public CSystem getCoords() {
		return coordinates;
	}

	public void addPlayer(Player player) {
		this.player = player;
		addObjective(player);
	}

	public Player getPlayer() {
		return player;
	}

	/**
	 * Adds entities from {@linkplain Entity} array to the World
	 * 
	 * @param entity - Entity array
	 * @see Entity
	 */
	public void addObjectives(Objective[] o) {
		add2.addAll(Arrays.asList(o));
	}

	/**
	 * Adds one entity to the World
	 * 
	 * @param entity - Entity that is added to the world
	 * @see Entity
	 */
	public void addObjective(Objective o) {
		add1.add(o);
	}

	/**
	 * Removes the entity from {@linkplain Entity} array in the world, and disables
	 * it's ticking behaviour
	 * 
	 * @param entity - Entity object
	 * @see {@link Entity} {@link Tickable}
	 */
	public void removeObjective(Objective o) {
		remove1.add(o);
	}

	/**
	 * Removes the entities from {@linkplain Entity} array in the world, and
	 * disables their ticking behaviour
	 * 
	 * @param entity - Entity array
	 * @see {@link Entity} {@link Tickable}
	 */
	public void removeObjectives(Objective[] o) {
		remove2.addAll(Arrays.asList(o));
	}

	/**
	 * Returns a {@link List} with entities in the given radius from the given Point
	 * 
	 * @param coordinate  - Point from where the radius counts
	 * @param meterRadius - the radius which is applied to the Point
	 * @return
	 */
	@Deprecated
	public List<Entity> getEntityInRadius(CPoint coordinate, double meterRadius) {
		List<Entity> returnList = new ArrayList<>();
		getGameObjectives().forEach(iterator -> {
			if (iterator != null && iterator instanceof Entity) {

				int radiusAdd = getPixelLength(meterRadius);
				Point p = getCoords().getAbsolutePoint(coordinate);
				Ellipse2D.Double search = new Ellipse2D.Double(p.x - radiusAdd, p.y - radiusAdd, radiusAdd * 2, radiusAdd * 2);
				if (iterator.getPosition() != null) {

					if (search.intersects(iterator.getCollision())) {
						returnList.add((Entity) iterator);
					}

				}
			}

		});

		return returnList;
	}

	/**
	 * Returns a {@link List} with objectives in the given radius from the given
	 * Point
	 * 
	 * @param coordinate  - Point from where the radius counts
	 * @param meterRadius - the radius which is applied to the Point
	 * @return
	 */
	@Deprecated
	public List<Objective> getObjectiveInRadius(CPoint coordinate, double meterRadius) {
		List<Objective> returnList = new ArrayList<>();
		getGameObjectives().forEach(iterator -> {
			if (iterator != null) {

				int radiusAdd = getPixelLength(meterRadius);
				Point p = getCoords().getAbsolutePoint(coordinate);
				Ellipse2D.Double search = new Ellipse2D.Double(p.x - radiusAdd, p.y - radiusAdd, radiusAdd * 2, radiusAdd * 2);
				if (iterator.getPosition() != null) {

					if (search.intersects(iterator.getCollision())) {
						returnList.add((Entity) iterator);
					}

				}
			}

		});

		return returnList;
	}

	public int getPixelLength(double meterLength) {
		return (int) (map_width * widthToMapMeterAspectRatio * meterLength);
	}

	public double getMeterLength(int pixelLength) {
		return pixelLength / (map_width * widthToMapMeterAspectRatio);
	}

	public CPoint getPixelPosition(CPoint meterPosition) {
		return new CPoint(getPixelLength(meterPosition.x), getPixelLength(meterPosition.y));
	}

	public CPoint getPixelPosition(int meterX, int meterY) {
		return new CPoint(getPixelLength(meterX), getPixelLength(meterY));
	}

	private Obstacle obstacleat = null;

	public Obstacle getObstacleAt(CPoint q) {
		obstacleat = null;
		Point p = getCoords().getAbsolutePoint(q);
		getGameObjectives().forEach(o -> {
			if (o instanceof Obstacle) {
				if (o.getCollision().contains(p))
					obstacleat = (Obstacle) o;

			}
		});

		return obstacleat;
	}

	private Entity entityat = null;

	public Entity getEntityAt(CPoint q, Entity notthis) {
		entityat = null;
		Point p = getCoords().getAbsolutePoint(q);
		getGameObjectives().forEach(e -> {
			if (e instanceof Entity && e != notthis) {
				if (e.getCollision().contains(p))
					entityat = (Entity) e;

			}
		});

		return entityat;
	}

	public List<Objective> getColliders(Collision c) {
		List<Objective> l = new ArrayList<>();
		getGameObjectives().forEach(o -> {
			if (!o.getCollision().equals(c) && o.getCollision().overlaps(c))
				l.add(o);
		});
		return l;
	}

	/**
	 * Returns <b>True</b> if the {@link Objective} is withing the render area on
	 * screen.
	 * </p>
	 * <b>This should be used as a condition on displaying any objectives.</b>
	 * </p>
	 * 
	 * @param o - Objective to test, if it should be rendered on screen.
	 * @return <b>boolean</b>
	 */
	public boolean isRendered(Objective o) {
		return o.getCollision().intersects(getCamera().getRenderArea());
	}

	/**
	 * If {@linkplain True} it starts to tick the {@link Tickable} components that
	 * had been added through
	 * {@code}{@link world.getTick().addTickingComponents(Tickable t)}, otherwise it
	 * will stop to tick the {@link Tickable} components
	 */
	public void setTicking(boolean b) {
		new Thread(()->{
			if (b)
				getTick().start();
			else
				getTick().stop();			
		}).start();

	}

	private boolean particlesAllowed = true;

	private void drawParticle(Particle particle) {
//		Image img = particle.returnTexture();
//		Point drawpos = getCoords().getAbsolutePoint(new CPoint(particle.getPosition().x - particle.getWidth() / 2,
//				particle.getPosition().y - particle.getHeight() / 2));
//
//		panel.drawImage(img, drawpos.x - getCamera().getRenderArea().x, (int) drawpos.y - getCamera().getRenderArea().y,
//				this);
		panel.drawImage(particle.returnTexture(), particle.getRenderCorner().x, particle.getRenderCorner().y, this);

	}

	private List<Particle> particleAdd = new ArrayList<>();
	private List<Particle> particleRemove = new ArrayList<>();

//	public void addParticle(Particle p) {
//		if (!ConfigHandler.getConfig(ConfigHandler.particles).equals("true"))
//			return;
//		particleAdd.add(p);
//	}

	public void addParticle(CPoint spawn, int direction, double spreadInMeter, int length, final int type) {
		if (!particlesAllowed)
			return;

		particleAdd.add(particleHandler.getType(spawn, direction, spreadInMeter, length, type));
	}

	@Override
	protected void paintComponent(Graphics g) {
		panel = (Graphics2D) g;

		panel.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		getPainter().paint(panel);

		panel.setColor(Color.red);
//		PerformanceUtil.start();
		getRenderObjects().forEach(o -> {
			drawGraphics(o);
		});
//		PerformanceUtil.stop(0,1);

		drawAllParticles();

		if (getTime().getLightLevel() >= 0.3f) {
			panel.drawImage(lightOverlay, 0, 0, this);
		}

		panel.dispose();
	}

	private void drawAllParticles() {
		if (!particlesAllowed)
			return;

		if (particleCache.isEmpty())
			return;

		particleCache.forEach(p -> {
			if (getCamera().getRenderArea().contains(getCoords().getAbsolutePoint(p.getPosition())))
				drawParticle(p);
		});

	}

	private void drawGraphics(Objective o) {

		panel.drawImage(o.getSprite(), o.getRenderCorner().x, o.getRenderCorner().y, this);

		if (!(o.isHitboxShown() || areHitboxesRendered()))
			return;

		panel.drawRect(-getCamera().getRenderArea().x + o.getCollision().x,
				-getCamera().getRenderArea().y + o.getCollision().y, o.getCollision().width, o.getCollision().height);

	}

	private void sortObjectives() {
		List<Objective> cache = new ArrayList<>();
		getGameObjectives().forEach(o -> {
			if (isRendered(o) && o.isVisible()) {
				cache.add(o);
				o.setRendered(true);
			} else {
				o.setRendered(false);
			}
		});

		Collections.sort(cache, new Comparator<Objective>() {

			@Override
			public int compare(Objective arg0, Objective arg1) {

				int result = Integer.compare(arg0.getCollision().getBounds().y + arg0.getCollision().getBounds().height,
						arg1.getCollision().getBounds().y + arg1.getCollision().getBounds().height);
				return result;
			}

		});
		renderObjects = new ArrayList<>();
		renderObjects = cache;
	}

	@Override
	public void onWorldTick(int tick) {

//		long start = System.nanoTime();

		particlesAllowed = Boolean.parseBoolean(ConfigHandler.getConfig(ConfigHandler.particles));

		if (remove1.size() > 0) {
			objects.removeAll(remove1);
			remove1 = new ArrayList<>();
		}
		if (remove2.size() > 0) {
			objects.removeAll(remove2);
			remove2 = new ArrayList<>();
		}
		if (add1.size() > 0) {
			objects.addAll(add1);
			add1 = new ArrayList<>();
		}
		if (add2.size() > 0) {
			objects.addAll(add2);
			add2 = new ArrayList<>();
		}

		if (!sorter.isAlive()) {
			sorter = new Thread(objectSorter);
			sorter.start();
		}

		if (!particle.isAlive() && particlesAllowed) {
			particle = new Thread(particleSpreader);
			particle.start();
		}

		if (!light.isAlive()) {
			light = new Thread(lightUpdater);
			light.start();
		}

//		System.out.println((System.nanoTime()-start)*1e-6);
	}

	private Thread particle = new Thread();
	private Thread sorter = new Thread();
	private Thread light = new Thread();

	private List<Particle> particleCache = new ArrayList<>();

	private Runnable particleSpreader = new Runnable() {

		@Override
		public void run() {
			try {
				particles.addAll(particleAdd);
				particleAdd.clear();
				particles.forEach(p -> {
					p.spread();
					if (p.getDuration() <= 0) {
						particleRemove.add(p);
					}
				});
				particles.removeAll(particleRemove);
				particleRemove.clear();
				particleCache = new ArrayList<>();
				particleCache.addAll(particles);
			} catch (Exception e) {
			}
			;
		}
	};

	private Runnable objectSorter = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			sortObjectives();
		}
	};

	private Runnable lightUpdater = new Runnable() {

		@Override
		public void run() {

			try {
				if (isDaylightCycle())
					getTime().nextTick();
				if (getTime().getLightLevel() >= 0.3f)
					updateLightOverlay();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	public void updateLightOverlay() {
//		long start = System.nanoTime();
		int width = getCamera().getRenderArea().width;
		int height = getCamera().getRenderArea().height;

		if (width == 0 || height == 0)
			return;
		// WICHTIG HIER AUF ARGB UND NICHT RGB, SONST KEINE TRANSPARENZ
		BufferedImage cache = ImageUtil.getFormatBufferedImage(width, height, Transparency.TRANSLUCENT);

		Graphics2D g = cache.createGraphics();

		g.setColor(new Color(0f, 0f, 0f, getTime().getLightLevel()));
		g.fillRect(0, 0, width, height);

		g.setComposite(AlphaComposite.DstOut);

		List<Objective> cacheObjects = new ArrayList<>();
		cacheObjects.addAll(getGameObjectives());
		cacheObjects.forEach(o -> {
			if (isRendered(o) && o.isLightEmitting() && o.getLightRange() > 0) {
				int range = getPixelLength(o.getLightRange()); // * 2;

				// center of image
				Point2D center = new Point2D.Float(o.getPosition().x - getCamera().getRenderArea().x,
						(int) o.getBounds().getCenterY() - getCamera().getRenderArea().y);
				// focus of radial gradient (centered)
				Point2D focus = center;
				// optionally set the focus to a side (instead of the center) to give direction
				// focus.setLocation(Math.cos(angle) * 3 * radius / 4 + radius, Math.sin(angle)
				// * 3 * radius / 4 + radius);

				// colors
				float[] dist = { 0f, 1f };
				Color[] colors = { new Color(0f, 0f, 0f, o.getLuminocity() <= 1f ? o.getLuminocity() : 1f),
						new Color(0, 0, 0, 0) };

				// set the paint
				RadialGradientPaint rgp = new RadialGradientPaint(center, range, focus, dist, colors,
						java.awt.MultipleGradientPaint.CycleMethod.NO_CYCLE);
				g.setPaint(rgp);

				// draw light
//				g.fillOval((int) center.getX() - range, (int) center.getY() - range, range * 2, range * 2);
				g.fill(Light.getLightingArea(o));
			}
		});
//		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

		g.dispose();

		lightOverlay = cache;
//		System.out.println((System.nanoTime() - start) * 1e-6);
	}

	@Override
	public void onRandomTick() {
	}

	/**
	 * Returns the {@link WorldTick} that is used to tick the entire game
	 * 
	 * @return tick
	 * @see WorldTick
	 */
	public WorldTick getTick() {
		return tick;
	}

	public WorldModel getModel() {
		return model;
	}

	public Chat getChat() {
		return Main.game.gp.chat;
	}

	public WorldPainter getPainter() {
		return painter;
	}

	public Camera getCamera() {
		return camera;
	}

	public boolean areHitboxesRendered() {
		return hitboxes;
	}

	public void renderHitboxes(boolean hitboxes) {
		this.hitboxes = hitboxes;
	}

	public GameTime getTime() {
		return time;
	}

	public boolean isDaylightCycle() {
		return daylightCycle;
	}

	public void setDaylightCycle(boolean daylightCycle) {
		this.daylightCycle = daylightCycle;
	}

	public List<Objective> getGameObjectives() {
		return objects;
	}

	public List<Objective> getRenderObjects() {
		return renderObjects;
	}

//	public ParticleHandler getParticleHandler() {
//		return particleHandler;
//	}

	public String getSaveName() {
		return saveName;
	}

	public void setSaveName(String saveName) {
		this.saveName = saveName;
	}
}