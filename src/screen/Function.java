package screen;

public abstract class Function {
	public static boolean within(double mx, double my, double mw, double mh, double x, double y){
		if (x < mx+mw && x > mx && y < my+mh && y > my){
			return true;
		}
		return false;
	}

	public static boolean overlap(double x1, double y1, double w1, double h1, double x2, double y2, double w2, double h2){
		if (x1 > x2+w2) return false;
		if (y1 > y2+h2) return false;
		if (x1+w1 < x2) return false;
		if (y1+h1 < y2) return false;

		return true;
	}

	public static int weightedChoice(double[] _choiceWeights){
		double _total = 0;

		for (int i = 0; i < _choiceWeights.length; i++){
			_total+=_choiceWeights[i];	
		}
		double _rand = Math.random()*_total;
		double _count = 0;
		for (int i = 0; i < _choiceWeights.length; i++){
			_count += _choiceWeights[i];
			if (_rand < _count){
				return i;
			}
		}

		return -1;
	}
	
	public static double weightedChoice(double[] _choices, double[] _choiceWeights){
		double _total = 0;

		for (int i = 0; i < _choiceWeights.length; i++){
			_total+=_choiceWeights[i];	
		}
		double _rand = Math.random()*_total;
		double _count = 0;
		for (int i = 0; i < _choiceWeights.length; i++){
			_count += _choiceWeights[i];
			if (_rand < _count){
				return _choices[i];
			}
		}

		return -1;
	}
	
	
	public static int randIntInclusive(int _min, int _max){
		return (int)(rand(_min, _max+1));
	}
	
	public static double rand(double _min, double _max){
		return (Math.random()*(_max-_min)+_min);
	}
	
	public static double distance(int _x1, int _y1, int _x2, int _y2){
		return Math.sqrt((_x1-_x2)*(_x1-_x2) + (_y1-_y2)*(_y1-_y2));
	}
}
