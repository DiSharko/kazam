package screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class TextBlock extends InterfaceElement {
	String text = "";
	int fontSize;
	
	public Color bgColor;
	public Color color;
	
	char alignment = 'c';
	
	ArrayList<String> textLines;
	int lineHeight = 0;
	
	public TextBlock(String _id, int _w, int _h, String _text, int _fontSize){
		type = "TextBlock";
		id = _id;
		text = _text;
		
		w = _w;
		h = _h;
		
		fontSize = _fontSize;
		setup();
	}
	
	public void setup(){
		textLines = new ArrayList<String>();
		BufferedImage temp = new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
		Graphics2D g = temp.createGraphics();
		g.setFont(new Font("Times New Roman", Font.PLAIN, fontSize));
		FontMetrics fm = g.getFontMetrics();
		lineHeight = fm.getHeight()+1;
		
		if (text == null) return;
		String[] words = text.split(" ");
		String line = "";
		for (int i = 0; i < words.length; i++){
			if (fm.getStringBounds((line + " "+ words[i]),g).getWidth() > w){
				textLines.add(line);
				line = "";
			}
			line += words[i] + " ";
		}
		textLines.add(line);
	}
	
	@Override
	public void draw(Graphics2D g){
		if (bgColor != null){
			g.setColor(bgColor);
			g.fillRect((int)x, (int)y, (int)w, (int)h);
		}
		if (color != null) g.setColor(color);
		else g.setColor(Color.white);
		g.setFont(new Font("Times New Roman", Font.PLAIN, fontSize));
		for (int i = 0; i < textLines.size(); i++){
			double _x = x;
			String line = textLines.get(i);
			if (alignment == 'c') _x = x + w/2-(int) g.getFontMetrics().getStringBounds(line, g).getWidth()/2;
			
			g.drawString(line, (int)_x, (int)y+(i+1)*lineHeight);
		}
		
	
	}
}
