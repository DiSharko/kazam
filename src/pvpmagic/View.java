package pvpmagic;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;

public class View {

	private Graphics2D g;
	GameData _data;
	
	Vector _pos;
	Vector _size;
	Vector _camera;
	double _scale;
	
	public View(GameData data){
		_data = data;
		_pos = new Vector(0,0);
		_camera = new Vector(0,0);
		_size = new Vector(800, 800);
		_scale = 1;
	}
	
	/**
	 * Get the backing Graphics2D object for the view
	 * @return the g
	 */
	public Graphics2D getGraphics() {
		return g;
	}
	
	public void setGraphics(Graphics2D tempG){
		g = tempG;
	}
	
	public void drawRect(Vector pos, Vector size){
		pos = gameToScreenPoint(pos);
		size = size.mult(_scale);
		g.drawRect((int)(pos.x), (int)(pos.y), (int)size.x, (int)size.y);
	}
	
	public void fillRect(Vector pos, Vector size){
		pos = gameToScreenPoint(pos);
		size = size.mult(_scale);
		g.fillRect((int)(pos.x), (int)(pos.y), (int)size.x, (int)size.y);
	}
	
	public void drawCircle(Vector pos, double r){
		pos = gameToScreenPoint(pos);
		r = r*_scale;
		g.drawOval((int)(pos.x), (int)(pos.y), (int)r, (int)r);
	}
	
	public void fillCircle(Vector pos, double r){
		pos = gameToScreenPoint(pos);
		r = r*_scale;
		g.fillOval((int)(pos.x), (int)(pos.y), (int)r, (int)r);
	}
	
	public void drawPolygon(Polygon p){
		paintPolygon(p, false);
	}
	public void fillPolygon(Polygon p){
		paintPolygon(p, true);
	}
	protected void paintPolygon(Polygon p, boolean fill){
		int npoints = p.points.size();
		int[] xpoints = new int[npoints];
		int[] ypoints = new int[npoints];
		for (int i = 0; i < p.points.size(); i++){
			Vector absPoint = gameToScreenPoint(p.points.get(i).plus(p.getPosition()));
			xpoints[i] = (int)absPoint.x;
			ypoints[i] = (int)absPoint.y;
		}
		Shape s = new java.awt.Polygon(xpoints, ypoints, npoints);
		if (fill && (g != null)) g.fill(s);
		else if (g != null)  g.draw(s);
	}
	
	
	public void drawImage(Image i, Vector pos){
		pos = gameToScreenPoint(pos);
		g.drawImage(i, (int)(pos.x), (int)(pos.y), null);
	}
	
	public void drawImage(Image i, Vector pos, Vector size){
		pos = gameToScreenPoint(pos);
		size = size.mult(_scale);
		g.drawImage(i, (int)(pos.x), (int)(pos.y), (int)(size.x), (int)(size.y), null);
	}
	
	public Vector gameToScreenPoint(Vector gamePoint){
		return gamePoint.minus(_camera.minus(_size.div(_scale*2))).mult(_scale).plus(_pos);
	}
	public Vector screenToGamePoint(Vector screenPoint){
		return screenPoint.minus(_pos).div(_scale).plus(_camera.minus(_size.div(_scale*2)));
	}
}
