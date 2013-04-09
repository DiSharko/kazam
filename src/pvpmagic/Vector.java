package pvpmagic;

public class Vector {

	public final double x;
	public final double y;
	
	public Vector(double tx, double ty) {
		x = tx;
		y = ty;
	}
	
	public final Vector mult(double s) {
		return new Vector(x*s, y*s);
	}
	public final Vector mult(Vector v) {
		return new Vector(x*v.x, y*v.y);
	}
	public final Vector mult(double xs, double ys) {
		return new Vector(x*xs, y*ys);
	}
	
	public final Vector plus(double s) {
		return new Vector(x+s, y+s);
	}
	public final Vector plus(Vector v) {
		return new Vector(x+v.x, y+v.y);
	}
	public final Vector plus(double xs, double ys) {
		return new Vector(x+xs, y+ys);
	}
	
	public final Vector minus(double s) {
		return new Vector(x-s, y-s);
	}
	public final Vector minus(Vector v) {
		return new Vector(x-v.x, y-v.y);
	}
	public final Vector minus(double xs, double ys) {
		return new Vector(x-xs, y-ys);
	}
	
	public final Vector div(double s) {
		return new Vector(x/s, y/s);
	}
	public final Vector div(Vector v) {
		return new Vector(x/v.x, y/v.y);
	}
	public final Vector div(double xs, double ys) {
		return new Vector(x/xs, y/ys);
	}
	
	
	public final double mag(){
		return Math.sqrt(x*x+y*y);
	}
	public final Vector normalize(){
		double mag = mag();
		return new Vector(x/mag, y/mag);
	}
	public final double dist(Vector v){
		return Math.sqrt(Math.pow(x-v.x, 2)+ Math.pow(y-v.y, 2));
	}
	
	@Override
	public String toString(){
		return "("+x+","+y+")";
	}
	@Override
	public boolean equals(Object o){
		if (o instanceof Vector){
			Vector v = (Vector) o;
			return (x == v.x && y == v.y);
		}
		return false;
	}
}
