package com.javapixelgame.game.api.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.javapixelgame.game.Main;
import com.javapixelgame.game.api.Command;
import com.javapixelgame.game.api.Tickable;
import com.javapixelgame.game.gui.component.TextField;
import com.javapixelgame.game.handling.GameHandler;
import com.javapixelgame.game.resourcehandling.GameFont;

@SuppressWarnings("serial")
public class Chat extends JPanel implements Tickable {

	private JTextPane[] lines;
	private Font font;
	private int yBorder = 2;
	private int lineHeight;

	private ChatMessage[] messages;

	private List<String> lastContent = new ArrayList<>();
	private int cycle = -1;

	private TextField inputField;

	private boolean open;

	private KeyListener send = new KeyListener() {

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				sendContent();
			}
		}
	};
	private KeyListener close = new KeyListener() {

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				close();
			}
		}
	};

	private Color lineBackground = new Color(0, 0, 0, 100);

	private Color textColor = new Color(255, 215, 0);

	private SimpleAttributeSet attributeSet = new SimpleAttributeSet();

	public Chat(int width, int height, int chatlines) {
		setSize(width, height);
		setLayout(null);
		setOpaque(false);
		setDoubleBuffered(true);
		setFocusable(false);

		lines = new JTextPane[chatlines];
		messages = new ChatMessage[chatlines];
		lineHeight = (height - yBorder * (chatlines)) / (chatlines + 1);
		font = new Font("System", Font.BOLD, (int) (lineHeight * 0.8));

		inputField = new TextField(width, lineHeight);
		inputField.setLocation(0, 0);

		inputField.setBounds(0, 0, width, lineHeight);

		inputField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {

				if (!isOpen())
					return;

				if (e.getKeyCode() == KeyEvent.VK_UP) {
					cycle--;
					if(cycle <0)cycle = 0;
					fillText();
				}

				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					cycle++;
					if(cycle >= lastContent.size())cycle = lastContent.size()-1;
					fillText();
				}

				if (inputField.getText().startsWith("/"))
					inputField.setForeground(Color.cyan);
				else
					inputField.setForeground(textColor);
			}

			private void fillText() {
				
//				Console.output("-----------"+cycle);
//				lastContent.forEach(e->Console.output(e));
				
				if (cycle >= 0 && cycle < lastContent.size())
					inputField.setText(lastContent.get(cycle));
			}
		});

		inputField.addKeyListener(send);
		inputField.addKeyListener(close);
		inputField.setVisible(false);

		add(inputField);

		for (int i = 0; i < lines.length; i++) {

			lines[i] = new JTextPane() {
				@Override
				public void paintComponent(Graphics g) {
					g.setColor(getBackground());
					Rectangle r = g.getClipBounds();
					g.fillRect(r.x, r.y, r.width, r.height);
					super.paintComponent(g);
				}
			};
			lines[i].setFocusable(false);
			lines[i].setEditable(false);
			lines[i].getCaret().deinstall(lines[i]);

			lines[i].setOpaque(false);
			lines[i].setBackground(lineBackground);

			lines[i].setText("");

			lines[i].setBounds(0, (i + 1) * lineHeight + (i + 1) * yBorder,
					(int) (lines[i].getText().length() * font.getSize()), lineHeight);
			lines[i].setFont(GameFont.getRainyHearts(Font.PLAIN, lines[i].getHeight() * 0.8f));

			add(lines[i]);
		}
	}

	public void sendContent() {
		if (inputField.getText().length() > 0) {
			if ((lastContent.size() > 0 ? !lastContent.get(lastContent.size() - 1).equals(inputField.getText()):true))
				lastContent.add(inputField.getText());
			if (inputField.getText().startsWith("/")) {
				Command.execute(inputField.getText());
				close();
				return;
			}
			sendMessage(
					new ChatMessage(GameHandler.getWorld().getPlayer().getRegistry().getName(), inputField.getText()));

		}
		close();
	}

	public void sendMessage(ChatMessage cm) {
		if (cm.getChatOutput().length() > 0) {
			ChatMessage[] buffer = new ChatMessage[messages.length];

			buffer[0] = cm;
			for (int i = 0; i < messages.length - 1; i++) {
				buffer[i + 1] = messages[i];
			}

			messages = buffer;

		}
	}

	public void sendRawTest(String text) {
		sendMessage(new ChatMessage("", text));
	}

	public void timeClear() {
		for (int i = 0; i < messages.length; i++) {
			if (messages[i] != null && !messages[i].shouldDisplay())
				messages[i] = null;
		}
	}

	public void displayMessages() {
		for (int i = 0; i < lines.length; i++) {
			if (messages[i] != null) {
				lines[i].setVisible(true);

				lines[i].setCharacterAttributes(attributeSet, true);

				Document doc = lines[i].getStyledDocument();
				try {
					doc.remove(0, doc.getLength());
				} catch (BadLocationException e1) {
				}
				StyleConstants.setBold(attributeSet, true);
				StyleConstants.setForeground(attributeSet, Color.red);
				if (messages[i].getSender() != "") {
					try {
						doc.insertString(doc.getLength(), messages[i].getSender(), attributeSet);
					} catch (BadLocationException e) {
					}
				}
				StyleConstants.setForeground(attributeSet, textColor);
				try {
					doc.insertString(doc.getLength(),
							(messages[i].getSender().length() > 0 ? ": " : "") + messages[i].getMessage(),
							attributeSet);
				} catch (BadLocationException e) {
				}

				lines[i].setBounds(0, (i + 1) * lineHeight + (i + 1) * yBorder,
						(int) (lines[i].getText().length() * font.getSize()), lineHeight);

			} else {
				lines[i].setVisible(false);
			}
		}
	}

	public void open() {
		open = true;
		cycle = lastContent.size();
		SwingUtilities.invokeLater(() -> {
			inputField.setText("/");
			inputField.setVisible(true);
			inputField.setEnabled(true);
			inputField.requestFocusInWindow();
		});

	}

	public void close() {
		open = false;
		SwingUtilities.invokeLater(() -> {
			inputField.setText("");
			inputField.setVisible(false);
			inputField.setEnabled(false);

			Main.game.requestFocusInWindow();
		});
	}

	@Override
	public void onWorldTick(int tick) {
		SwingUtilities.invokeLater(() -> {
			displayMessages();
		});
		timeClear();
	}

	@Override
	public void onRandomTick() {
	}

	public boolean isOpen() {
		return open;
	}

}
