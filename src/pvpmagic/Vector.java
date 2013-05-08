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
		if (mag == 0) return this;
		return new Vector(x/mag, y/mag);
	}
	public final double dist(Vector v){
		return Math.sqrt(Math.pow(x-v.x, 2)+ Math.pow(y-v.y, 2));
	}
	
	
	public final double dot(Vector v) {
		return x*v.x + y*v.y;
	}
	
	public final double dot(double tx, double ty) {
		return tx*x + ty*y;
	}

	public static final Vector NaN = new Vector(Float.NaN, Float.NaN);
	public static final Vector ZERO = new Vector(0,0);
	
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
	
	public String toNet() {
		return x + " " + y;
	}
	
	public static Vector fromNet(String vectorString) throws IllegalArgumentException {
		String[] vec = vectorString.split(" ");
		if (vec.length != 2) {
			throw new IllegalArgumentException("ERROR: More than 2 values. Invalid vector string.");
		} else {
			double x = Double.parseDouble(vec[0]);
			double y = Double.parseDouble(vec[1]);
			return new Vector(x, y);
		}
	}
	
	public final Vector proj(Vector other) {
		if (other.equals(ZERO)) return this;
		return other.mult(this.dot(other)/Math.pow(other.mag(),2));
	}
	
	public final Vector bound(Vector min, Vector max){
		if (min.x > max.x || min.y > max.y) return this;
		return new Vector(Math.min(Math.max(x, min.x), max.x), Math.min(Math.max(y, min.y), max.y));
	}
}
