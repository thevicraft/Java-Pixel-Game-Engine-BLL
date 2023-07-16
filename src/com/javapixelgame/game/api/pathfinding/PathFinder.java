package com.javapixelgame.game.api.pathfinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.javapixelgame.game.api.entity.NPC;
import com.javapixelgame.game.api.world.Tile;
import com.javapixelgame.game.handling.GameHandler;

public class PathFinder implements Serializable{
	private static final long serialVersionUID = 1L;
	private int maxCol;
	private int maxRow;

	public Node[][] node;
	public List<Node> openList = new ArrayList<>();
	public List<Node> pathList = new ArrayList<>();
	public Node startNode, goalNode, currentNode;
	public boolean goalReached = false;
	public int step = 0;

	public PathFinder(NPC npc) {
		initNodes(npc);
	}

	public void initNodes(NPC npc) {
		maxCol = npc.getWorld().getModel().columns();
		maxRow = npc.getWorld().getModel().rows();
		node = new Node[maxCol][maxRow];

		int col = 0;
		int row = 0;

		while (col < maxCol && row < maxRow) {

			node[col][row] = new Node(col, row);

			col++;
			if (col == maxCol) {
				col = 0;
				row++;
			}

		}

	}

	public void resetNodes() {
		int col = 0;
		int row = 0;

		while (col < maxCol && row < maxRow) {

			node[col][row].open = false;
			node[col][row].checked = false;
			node[col][row].solid = false;

			col++;
			if (col == maxCol) {
				col = 0;
				row++;
			}

		}

		openList.clear();
		pathList.clear();
		goalReached = false;
		step = 0;

	}

	public void setNodes(int startCol, int startRow, int goalCol, int goalRow, NPC npc) {
		resetNodes();
		
		// Set start and goal node
		
		startNode = node[startCol][startRow];
		currentNode = startNode;
		goalNode = node[goalCol][goalRow];
		openList.add(currentNode);
		
		int col = 0;
		int row = 0;

		while (col < maxCol && row < maxRow) {

			Tile tile = GameHandler.getWorld().getModel().getSquareTile(col,row);
			// checks the tile on the npc's tile collision properties
			node[col][row].solid = npc.collide(tile);
			
			getCost(node[col][row]);
			
			col++;
			if (col == maxCol) {
				col = 0;
				row++;
			}

		}
	}
	
	private void getCost(Node node) {
		// get g cost, the distance from the start node
				int xDistance = Math.abs(node.col - startNode.col);
				int yDistance = Math.abs(node.row - startNode.row);

				node.gCost = xDistance + yDistance;

				// get h cost, the distance from the goal node

				xDistance = Math.abs(node.col - goalNode.col);
				yDistance = Math.abs(node.row - goalNode.row);

				node.hCost = xDistance + yDistance;

				// get f cost, the total cost
				node.fCost = node.gCost + node.hCost;
	}
	
	public boolean search() {
		
		while(goalReached == false && step < 500) {

			
			int col = currentNode.col;
			int row = currentNode.row;

			currentNode.checked = true;
			openList.remove(currentNode);

			// open the up node
			if (row - 1 >= 0)
				openNode(node[col][row - 1]);
			// open the left node
			if (col - 1 >= 0)
				openNode(node[col - 1][row]);
			// open the down node
			if (row + 1 < maxRow)
				openNode(node[col][row + 1]);
			// open the right node
			if (col + 1 < maxCol)
				openNode(node[col + 1][row]);

			int bestNodeIndex = 0;
			int bestNodefCost = 999;

			for (int i = 0; i < openList.size(); i++) {
				
				// check if this node's f cost is better
				if(openList.get(i).fCost < bestNodefCost) {
					bestNodeIndex = i;
					bestNodefCost = openList.get(i).fCost;
				}
				// if f cost is equal, check g cost
				else if(openList.get(i).fCost == bestNodefCost) {
					if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
						bestNodeIndex = i;
					}
				}
			}
			
			if(openList.size() == 0) {
				break;
			}
			
			// after the loop, we get the best node which is our next step
			currentNode = openList.get(bestNodeIndex);
			
			if(currentNode == goalNode) {
				goalReached = true;
				trackThePath();
			}
			step ++;
			
		}
		return goalReached;
	}
	
	private void trackThePath() {
		
		Node current = goalNode;
		
		while(current != startNode) {
			
			pathList.add(0,current);
			current = current.parent;
			
		}
		
	}

	public void openNode(Node node) {
		
		if(node.open == false && node.checked == false && node.solid == false) {
			
			node.open = true;
			node.parent = currentNode;
			openList.add(node);
			
		}
		
	}

}
