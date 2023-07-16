package com.javapixelgame.game.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.javapixelgame.game.keyboard.SingleKeyListener;

@SuppressWarnings("serial")
public class DemoPanel extends JPanel {

	final int maxCol = 15;
	final int maxRow = 10;
	final int nodeSize = 70;
	final int screenWidth = nodeSize * maxCol;
	final int screenHeight = nodeSize * maxRow;

	Node[][] node = new Node[maxCol][maxRow];
	Node startNode, goalNode, currentNode;
	List<Node> openList = new ArrayList<>();
	List<Node> checkedList = new ArrayList<>();

	boolean goalReached = false;

	public DemoPanel() {
		setPreferredSize(new Dimension(screenWidth, screenHeight));
		setBackground(Color.black);
		setLayout(new GridLayout(maxRow, maxCol));
		
		this.addKeyListener(new SingleKeyListener.Pressed(KeyEvent.VK_ENTER) {

			@Override
			public void pressed(KeyEvent e) {
				// TODO Auto-generated method stub
				autoSearch();
//				search();
			}
			
		});
		
		this.setFocusable(true);

		int col = 0;
		int row = 0;

		while (col < maxCol && row < maxRow) {

			node[col][row] = new Node(col, row);
			this.add(node[col][row]);
			col++;

			if (col == maxCol) {
				col = 0;
				row++;
			}

		}

		// set start and goal node

		setStartNode(3, 6);
		setGoalNode(11, 3);

		setSolidNode(10, 2);
		setSolidNode(10, 3);
		setSolidNode(10, 4);
		setSolidNode(10, 5);
		setSolidNode(10, 6);
		setSolidNode(10, 7);
		setSolidNode(6, 2);
		setSolidNode(7, 2);
		setSolidNode(8, 2);
		setSolidNode(9, 2);
		setSolidNode(11, 7);
		setSolidNode(12, 7);
		setSolidNode(6, 1);

		setCostOnNodes();

	}

	private void setStartNode(int col, int row) {
		node[col][row].setAsStart();
		startNode = node[col][row];
		currentNode = startNode;
	}

	private void setGoalNode(int col, int row) {
		node[col][row].setAsGoal();
		goalNode = node[col][row];
	}

	private void setSolidNode(int col, int row) {
		node[col][row].setAsSolid();
	}

	private void setCostOnNodes() {
		int col = 0;
		int row = 0;

		while (col < maxCol && row < maxRow) {

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

		if (node != startNode && node != goalNode) {
			node.setText("<html>F: " + node.fCost + "<br>G: " + node.gCost + "</html>");
		}

	}
	
	int step = 0;
	
	public void autoSearch() {
		while(goalReached == false && step < 300) {
			
			search();
			step++;
			
		}
	}

	public void search() {

		if (goalReached == false) {

			int col = currentNode.col;
			int row = currentNode.row;

			currentNode.setAsChecked();
			checkedList.add(currentNode);
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
			
			// after the loop, we get the best node which is our next step
			
			currentNode = openList.get(bestNodeIndex);
			
			if(currentNode == goalNode) {
				goalReached = true;
				trackThePath();
			}
			
		}

	}

	private void openNode(Node node) {
		if (node.open == false && node.checked == false && node.solid == false) {
			// if the node is not opened yet, add it to the open list
			node.setAsOpen();
			node.parent = currentNode;
			openList.add(node);

		}
	}
	
	private void trackThePath() {
		// backtrack and draw the best path
		Node current = goalNode;
		
		while(current != startNode) {
			
			current = current.parent;
			if(current != startNode) {
				current.setAsPath();
			}
			
		}
		
	}

}
