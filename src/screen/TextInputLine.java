package screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


public class TextInputLine extends InterfaceElement {
	public TextInputLine(Screen _screen, String _id, int _characters){
		type = "TextInputLine";
		acceptsKeyboardFocus = true;
		
		screen = _screen;
		rightBound = _characters;
		id = _id;
		w = 5 + (int)(_characters*8.1);
		h = 20;
	}

	int maxLength = 0;

	String defaultData = "";
	StringBuilder data = new StringBuilder(defaultData);
	StringBuilder drawText = new StringBuilder("");

	int cursorPlacement = 0;
	int scrolledTo = 0;
	int rightBound = 3;

	int blinkDelay = 20;
	int blinkTimer = 0;
	boolean displayBlink = false;
	
	public String restrictions = "";

	@Override
	public void handleKeyPressed(KeyEvent event){
		keystroke(TextInputLine.getCharacterFromKeystroke(event.getKeyCode(), restrictions));
	}
	
	public void keystroke(String key){
		if (key.equals("delete")){
			if (cursorPlacement > 0 && data.length() > 0){
				data.deleteCharAt(cursorPlacement-1);
				cursorPlacement--;
				if (cursorPlacement==scrolledTo){
					scrolledTo = Math.max(scrolledTo - rightBound, 0);
				}
			}
		} else if (key.equals("back delete")){
			if (cursorPlacement < data.length()){
				data.deleteCharAt(cursorPlacement);
			}
		} else if (key.equals("left")){
			cursorPlacement--;
			if (cursorPlacement < 0){
				cursorPlacement = 0;
			}
		} else if (key.equals("right")){
			cursorPlacement++;
			if (cursorPlacement > data.length()){
				cursorPlacement = data.length();
			}
		} else if (key.equals("down")){
			cursorPlacement = data.length();
		} else if (key.equals("up")){
			cursorPlacement = 0;
		} else if (key.equals("enter")){
			onReleased(true);
		} else if (key.equals("escape")){
			onReleased(false);
		} else if (key.equals("tab")){
			screen.nextTextBox();
		} else if (key.equals("shiftTab")){
			screen.previousTextBox();
		} else {
			if (key != "" && (maxLength < 1 || data.length() < maxLength)){
				data.insert(cursorPlacement, key);
				cursorPlacement++;
			}
		} 
		setDrawText();
	}

	@Override
	public void onReleased(boolean save){
		if (save){
			defaultData = data.toString();
		} else {
			data = new StringBuilder(defaultData);
		}		
		cursorPlacement = 0;
		scrolledTo = 0;
		setDrawText();
		selected = false;
		screen.handleElementReleased(this);
		screen.keyboardFocus = null;
	}

	protected void setDrawText(){
		if (cursorPlacement-scrolledTo>rightBound){
			scrolledTo = cursorPlacement-rightBound;
		} else if (cursorPlacement-scrolledTo < 0){
			scrolledTo = cursorPlacement;
		}
		resetBlink();
		drawText = new StringBuilder(data.substring(scrolledTo, Math.min(scrolledTo+rightBound,data.length())));
	}
	protected void resetBlink(){
		blinkTimer = 0;
		displayBlink = true;
	}

	@Override
	public void draw(Graphics2D g){
		if (selected) {
			g.setColor(new Color(0.75f,0.75f,0.75f));
		} else if (mousedOver){
			g.setColor(new Color(0.9f,0.9f,0.9f));
		} else {
			g.setColor(Color.white);
		}
		g.fillRect((int)x, (int)y, (int)w, (int)h);
		if (selected || mousedOver) {
			g.setColor(Color.black);
		} else {
			g.setColor(Color.darkGray);
		}
		g.drawRect((int)x, (int)y, (int)w, (int)h);

		g.setColor(Color.black);
		g.setFont(new Font("Courier", Font.PLAIN, 13));
		g.drawString(drawText.toString(), (int)x + 3, (int)y + (int)h - 6);
		if (selected) {
			if (displayBlink) {
				g.drawRect((int)x + 2 + (cursorPlacement - scrolledTo)*8, (int)y + (int)h - 15, 1, 10);
			}
		}
	}

	@Override
	public void update(){
		if (selected){
			if (blinkTimer < blinkDelay){
				blinkTimer++;
			} else {
				blinkTimer = 0;
				displayBlink = !displayBlink;
			}
		}
	}


	public String getText(){
		return data.toString();
	}
	public void setText(String _text){
		defaultData = _text;
		data = new StringBuilder(_text);
		setDrawText();
	}



	@Override
	public void handleMousePressed(MouseEvent event) {
		int px = event.getX();
		//int py = event.getY();
		cursorPlacement = (int)((px-x)/8.1 + scrolledTo);
		if (cursorPlacement > data.length()){
			cursorPlacement = data.length();
		}
		resetBlink();
	}

	@Override
	public void handleMouseReleased(MouseEvent event) {
		int px = event.getX();
		int py = event.getY();
		if (Function.within(x, y, w, h, px, py)){
			cursorPlacement = (int)((px-x)/8.1 + scrolledTo);
			if (cursorPlacement > data.length()){
				cursorPlacement = data.length();
			}
			resetBlink();
		}
	}














	//////////////////////////////////////////////////////////////////////////////


	public static boolean shiftDown = false;

	public static String getCharacterFromKeystroke(int key, String restrictions){
		if (restrictions == null){
			restrictions = "";
		}

		if (key == KeyEvent.VK_0){if (shiftDown){return ")";} else {return "0";}}
		else if (key == KeyEvent.VK_1){if (shiftDown){return "!";} else {return "1";}}
		else if (key == KeyEvent.VK_2){if (shiftDown){return "@";} else {return "2";}}
		else if (key == KeyEvent.VK_3){if (shiftDown){return "#";} else {return "3";}}
		else if (key == KeyEvent.VK_4){if (shiftDown){return "$";} else {return "4";}}
		else if (key == KeyEvent.VK_5){if (shiftDown){return "%";} else {return "5";}}
		else if (key == KeyEvent.VK_6){if (shiftDown){return "^";} else {return "6";}}
		else if (key == KeyEvent.VK_7){if (shiftDown){return "&";} else {return "7";}}
		else if (key == KeyEvent.VK_8){if (shiftDown){return "*";} else {return "8";}}
		else if (key == KeyEvent.VK_9){if (shiftDown){return "(";} else {return "9";}}

		if (!restrictions.equals("numbers")){
			if (key == KeyEvent.VK_A){if (shiftDown){return "A";} else {return "a";}}
			else if (key == KeyEvent.VK_B){if (shiftDown){return "B";} else {return "b";}}
			else if (key == KeyEvent.VK_C){if (shiftDown){return "C";} else {return "c";}}
			else if (key == KeyEvent.VK_D){if (shiftDown){return "D";} else {return "d";}}
			else if (key == KeyEvent.VK_E){if (shiftDown){return "E";} else {return "e";}}
			else if (key == KeyEvent.VK_F){if (shiftDown){return "F";} else {return "f";}}
			else if (key == KeyEvent.VK_G){if (shiftDown){return "G";} else {return "g";}}
			else if (key == KeyEvent.VK_H){if (shiftDown){return "H";} else {return "h";}}
			else if (key == KeyEvent.VK_I){if (shiftDown){return "I";} else {return "i";}}
			else if (key == KeyEvent.VK_J){if (shiftDown){return "J";} else {return "j";}}
			else if (key == KeyEvent.VK_K){if (shiftDown){return "K";} else {return "k";}}
			else if (key == KeyEvent.VK_L){if (shiftDown){return "L";} else {return "l";}}
			else if (key == KeyEvent.VK_M){if (shiftDown){return "M";} else {return "m";}}
			else if (key == KeyEvent.VK_N){if (shiftDown){return "N";} else {return "n";}}
			else if (key == KeyEvent.VK_O){if (shiftDown){return "O";} else {return "o";}}
			else if (key == KeyEvent.VK_P){if (shiftDown){return "P";} else {return "p";}}
			else if (key == KeyEvent.VK_Q){if (shiftDown){return "Q";} else {return "q";}}
			else if (key == KeyEvent.VK_R){if (shiftDown){return "R";} else {return "r";}}
			else if (key == KeyEvent.VK_S){if (shiftDown){return "S";} else {return "s";}}
			else if (key == KeyEvent.VK_T){if (shiftDown){return "T";} else {return "t";}}
			else if (key == KeyEvent.VK_U){if (shiftDown){return "U";} else {return "u";}}
			else if (key == KeyEvent.VK_V){if (shiftDown){return "V";} else {return "v";}}
			else if (key == KeyEvent.VK_W){if (shiftDown){return "W";} else {return "w";}}
			else if (key == KeyEvent.VK_X){if (shiftDown){return "X";} else {return "x";}}
			else if (key == KeyEvent.VK_Y){if (shiftDown){return "Y";} else {return "y";}}
			else if (key == KeyEvent.VK_Z){if (shiftDown){return "Z";} else {return "z";}}

			else if (key == KeyEvent.VK_SPACE){return " ";}
			else if (key == KeyEvent.VK_MINUS){if (shiftDown){return "_";} else {return "-";}}
			else if (key == KeyEvent.VK_PERIOD){if (shiftDown){return ">";} else {return ".";}}
			else if (key == KeyEvent.VK_COMMA){if (shiftDown){return "<";} else {return ",";}}
			else if (key == KeyEvent.VK_SLASH){if (shiftDown){return "?";} else {return "/";}}
			else if (key == KeyEvent.VK_QUOTE){if (shiftDown){return "\"";} else {return "'";}}
			else if (key == KeyEvent.VK_SEMICOLON){if (shiftDown){return ":";} else {return ";";}}
		} else{
			if (key == KeyEvent.VK_PERIOD){if (!shiftDown){return ".";}}
			else if (key == KeyEvent.VK_MINUS){if (!shiftDown){return "-";}}
		}

		if (key == KeyEvent.VK_ESCAPE){return "escape";}
		else if (key == KeyEvent.VK_ENTER){return "enter";}
		else if (key == KeyEvent.VK_BACK_SPACE){if (shiftDown){return "back delete";} else {return "delete";}}
		else if (key == KeyEvent.VK_LEFT){if (shiftDown){return "left";} else {return "left";}}
		else if (key == KeyEvent.VK_RIGHT){if (shiftDown){return "right";} else {return "right";}}
		else if (key == KeyEvent.VK_UP){if (shiftDown){return "up";} else {return "up";}}
		else if (key == KeyEvent.VK_DOWN){if (shiftDown){return "down";} else {return "down";}}
		else if (key == KeyEvent.VK_TAB){if (shiftDown){return "shiftTab";} else {return "tab";}}


		return "";
	}


}
