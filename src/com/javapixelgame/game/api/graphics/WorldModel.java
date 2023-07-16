package com.javapixelgame.game.api.graphics;

import java.awt.Point;
import java.util.List;

import com.javapixelgame.game.api.world.Tile;

public class WorldModel {

	private List<List<Tile>> tiles;
	private int inLine = -1;
	public String name;

	public WorldModel(List<List<Tile>> tiles) {
		this.tiles = tiles;
	}

	public int rows() {
		return tiles.size();
	}
	
	public int columns() {
		try {
			return tiles.get(0).size();			
		} catch (Exception e) {}
		return -1;
	}

	public List<List<Tile>> getTiles() {
		return tiles;
	}

	public List<Tile> getRow(int index) {
		return tiles.get(index);
	}

	public boolean hasNextRow() {
		return inLine < tiles.size() - 1;
	}

	public List<Tile> nextRow() {
		inLine++;
		try {
			return tiles.get(inLine);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public void reset() {
		inLine = -1;
	}


	public Tile getTile(Point framePoint, int pixelPerImage) {
		int row = framePoint.y / pixelPerImage;
		int x = framePoint.x / pixelPerImage;
		int correctedRow = row < rows() ? row : rows()-1;
		int correctedX = x < getRow(correctedRow).size() ? x : getRow(correctedRow).size()-1;
		return getRow(correctedRow).get(correctedX);
		
	}
	
	public Point getTilePosition(Point mapPosition, int pixelPerImage) {
		int row = mapPosition.y / pixelPerImage;
		int x = mapPosition.x / pixelPerImage;
		int correctedRow = getCorrectedRow(row);
		int correctedX = getCorrectedCol(x, correctedRow);
		return new Point(correctedX, correctedRow);
		
	}
	
	public Point getMapPosition(int col_, int row_, int pixelPerImage) {
		int correctedRow = getCorrectedRow(row_);
		int correctedX = getCorrectedCol(col_, correctedRow);

		return new Point( correctedX * pixelPerImage, correctedRow * pixelPerImage);
		
	}
	
	public Tile getSquareTile(int tilePositionX, int tilePositionY) {
		int row = tilePositionY;
		int x = tilePositionX;
		int correctedRow = row < rows() ? row : rows()-1;
		int correctedX = x < getRow(correctedRow).size() ? x : getRow(correctedRow).size()-1;
		return getRow(correctedRow).get(correctedX);
		
	}
	
	private int getCorrectedRow(int row) {
		return row < rows() ? row : rows()-1;
	}
	private int getCorrectedCol(int col, int row) {
		return col < getRow(row).size() ? col : getRow(row).size()-1;
	}
}
