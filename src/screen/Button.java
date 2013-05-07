package screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import pvpmagic.Resource;



public class Button extends InterfaceElement {

	public Button(Screen _screen, String _id, int _w, int _h, String _name, int _activationKeycode){
		screen = _screen;
		type = "Button";
		id = _id;
		x = 0; y = 0;
		w = _w; h = _h;
		name = _name;
		activationKeycode = _activationKeycode;
	}
	public Button(Screen _screen, String _id, Image _image){
		screen = _screen;
		type = "Button";
		id = _id;
		image = _image;
		x = 0; y = 0;
		recalculateSize();
	}
	public Button(Screen _screen, String _id, Image _image, Image _mousedOverImage, Image _selectedImage){
		screen = _screen;
		type = "Button";
		id = _id;
		image = _image;
		mousedOverImage = _mousedOverImage;
		selectedImage = _selectedImage;
		x = 0; y = 0;
		recalculateSize();
	}

	public Button(Screen _screen, String _id, int _w, int _h){
		screen = _screen;
		type = "Button";
		id = _id;
		w = _w; h = _h;
		x = 0; y = 0;
	}	

	int activationKeycode = -1;

	public Color textColor = Color.black;
	public int defaultFontSize = 18;
	public int fontSize = defaultFontSize;

	int roundness = 20;
	int offsetOnSelection = 1;

	// IMAGE BUTTON ////////////////
	public Image image;
	Image mousedOverImage;
	Image selectedImage;

	Image subjectImage;
	int subjectImageWidth, subjectImageHeight;

	public double namePositionFractionX = 0.5, namePositionFractionY = 0.5;


	public void recalculateSize(){
		if (image != null){
			w = image.getWidth(null);
			h = image.getHeight(null);
		} else if (name != null){
			BufferedImage temp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = temp.createGraphics();

			g.setFont(new Font(Resource._buttonFontName, Font.PLAIN, fontSize));
			w = (int)g.getFontMetrics().getStringBounds(name, g).getWidth();
			h = (int)g.getFontMetrics().getStringBounds(name, g).getHeight();
		}
	}

	public void recalculateText(){
		if (name != null && screen != null){
			Graphics2D g = (Graphics2D) screen._holder.getGraphics();
			if (g == null) return;
			
			int width = 100000000;
			fontSize = defaultFontSize;
			while (width > w-4){
				g.setFont(new Font(Resource._buttonFontName, Font.PLAIN, fontSize));
				width = (int)g.getFontMetrics().getStringBounds(name, g).getWidth();
				if (width > w-4) fontSize--;
			}
		}
	}

	private double lastCheckedWidth = -1;
	@Override
	public void draw(Graphics2D g){

		if (lastCheckedWidth != w){
			recalculateText();
		}
		
		int selectedOffset = 0;
		if (selected) selectedOffset = offsetOnSelection;


		if (image == null){
			if (color != null){
				g.setColor(color);
				g.fillRoundRect((int)x + selectedOffset,(int)y + selectedOffset, (int)w, (int)h, roundness, roundness);
			}
			if (borderColor != null){
				g.setColor(borderColor);
				g.drawRoundRect((int)x + selectedOffset,(int)y + selectedOffset, (int)w, (int)h, roundness, roundness);
			}
		}


		if (selected && selectedImage != null){
			g.drawImage(selectedImage, (int)x + selectedOffset, (int)y + selectedOffset, (int)w, (int)h, null);
		} else if (mousedOver && mousedOverImage != null){
			g.drawImage(mousedOverImage, (int)x + selectedOffset, (int)y + selectedOffset, (int)w, (int)h, null);
		} else if (image != null){
			g.drawImage(image, (int)x + selectedOffset, (int)y + selectedOffset, (int)w, (int)h, null);
		}


		if (subjectImage != null){
			g.drawImage(subjectImage, (int)(x + selectedOffset-subjectImageWidth+w), (int)(y + selectedOffset-subjectImageHeight+h), (int)subjectImageWidth, (int)subjectImageHeight, null);
		}


		if (name != null){
			g.setColor(textColor);
			g.setFont(new Font(Resource._buttonFontName, Font.PLAIN, fontSize));
			double xFrac = namePositionFractionX;
			double yFrac = namePositionFractionY;
			int tempX = (int)x+(int)(w*xFrac)-(int)g.getFontMetrics().getStringBounds(name, g).getWidth()/2;
			int tempY = (int)y+(int)(h*yFrac)+(int)g.getFontMetrics().getStringBounds(name, g).getHeight()/3;
			g.drawString(name, tempX+selectedOffset, tempY+selectedOffset);
		}

		if (selected && selectedImage == null && selectColor != null){
			g.setColor(selectColor);
			g.fillRoundRect((int)x + selectedOffset,(int)y + selectedOffset, (int)w, (int)h, roundness, roundness);
		} else if (mousedOver && mousedOverImage == null && mouseOverColor != null){
			g.setColor(mouseOverColor);
			g.fillRoundRect((int)x + selectedOffset,(int)y + selectedOffset, (int)w, (int)h, roundness, roundness);
		}

	}

	@Override
	public void handleMouseReleased(MouseEvent event){
		if (selected && Function.within(x, y, w, h, event.getX(), event.getY())){
			screen.handleElementReleased(this);
		}
		selected = false;
		mousedOver = false;
	}

	public Button setRoundness(int r){
		roundness = r;
		return this;
	}
	public Button setFontSize(int f){
		fontSize = f;
		return this;
	}

}