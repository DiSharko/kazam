package pvpmagic;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import screen.Screen;


public abstract class EngineLogic {

	public int gameState = 0; // -1 = lose, 0 = in progress, 1 = won
	public boolean paused = false;
	public boolean reset = false;

	public Screen screen;

	public String levelname = "";

	public ArrayList<Unit> entities;


	public abstract void setup();

	public abstract void handleMousePressed(Vector point, MouseEvent event);
	public abstract void handleMouseDragged(Vector point, MouseEvent event);
	public abstract void handleMouseReleased(Vector point, MouseEvent event);

	public abstract void handleKeyPressed(KeyEvent e);
	public abstract void handleKeyReleased(KeyEvent e);

	public abstract void update(long time);


	public void collideEntities(){
		for (int i = 0; i < entities.size(); i++){
			Unit e1 = entities.get(i);
			if (!e1._collidable) continue;
			for (int j = i+1; j < entities.size(); j++){
				Unit e2 = entities.get(j);
				if (!e2._collidable || e1 == e2) continue;
				Collision c = Shape.collide(e1._shape, e2._shape);

				if (c != null){
					double ve1 = e1._vel.dot(c.mtv(e1).normalize());
					double ve2 = e2._vel.dot(c.mtv(e2).normalize());

					double cor = Math.sqrt(e1._restitution*e2._restitution);

					if (e1._movable && e2._movable){
						e1._pos = e1._pos.plus(c.mtv(e1).div(2));
						e2._pos = e2._pos.plus(c.mtv(e2).div(2));

						e1.applyImpulse(c.mtv(e1).normalize().mult((c.mtv(e1).normalize().dot(e2._vel.minus(e1._vel)))*e1._mass*e2._mass*(1+cor)/(e1._mass+e2._mass)));
						e2.applyImpulse(c.mtv(e2).normalize().mult((c.mtv(e2).normalize().dot(e1._vel.minus(e2._vel)))*e1._mass*e2._mass*(1+cor)/(e1._mass+e2._mass)));
					} else if (e1._movable){
						e1._pos = e1._pos.plus(c.mtv(e1));
						e1.applyImpulse(c.mtv(e1).normalize().mult((ve2-ve1)*e1._mass*(1+cor)));

					} else if (e2._movable){
						e2._pos = e2._pos.plus(c.mtv(e2));
						e2.applyImpulse(c.mtv(e2).normalize().mult((ve1-ve2)*e2._mass*(1+cor)));
					}
				}

				e1.collide(c);
				e2.collide(c);
				e1._shape.colliding = true;
				e2._shape.colliding = true;
			}
		}
	}


	public void applyMovement(long time){
		for (int i = 0; i < entities.size(); i++){
			Unit e = entities.get(i);
			if (!e._movable) continue;

			e._vel = e._vel.plus(e._force.div(e._mass).mult(time/10000000)).plus(e._impulse.div(e._mass));
			e._vel = new Vector(Math.min(Math.max(e._vel.x, -30), 30),Math.min(Math.max(e._vel.y, -50), 50));

			e._pos = e._pos.plus(e._vel);
			e._impulse = new Vector(0,0);
			e._force = new Vector(0,0);
		}
	}


	
//	public RaycastCollision raycast(Vector src, Vector dir){
//		return raycast(null, src, dir);
//	}
//
//	public RaycastCollision raycast(Entity sourceEntity, Vector src, Vector dir){
//		dir = dir.normalize();
//		Vector point = null;
//		Entity entity = null;
//		for (int i = 0; i < entities.size(); i++){
//			if (entities.get(i) == sourceEntity) continue;
//			if (entities.get(i).collisionShape instanceof Circle){
//				Circle c = (Circle) entities.get(i).collisionShape;
//				Vector proj = c.getCenter().projectOntoLine(src, src.plus(dir));
//				float x = proj.dist(c.getCenter());
//				if ((proj.minus(dir).dist(src) < proj.dist(src) || src.dist(c.getCenter()) < c.radius) && x < c.radius){
//					float L = proj.dist(src);
//					Vector temp = src.plus(dir.smult(L - (float)Math.sqrt(c.radius*c.radius - x*x)));
//					if (point == null || temp.dist(src) < point.dist(src)){
//						point = temp;
//						entity = entities.get(i);
//					}
//				}
//			} else if (entities.get(i).collisionShape instanceof Polygon){
//				Polygon p = (Polygon) entities.get(i).collisionShape;
//				for (int j = 0; j < p.points.size(); j++){
//					Vector p1 = p.points.get(j).plus(p.getPosition());
//					Vector p2 = null;
//					if (j >= p.points.size() - 1) p2 = p.points.get(0).plus(p.getPosition());
//					else p2 = p.points.get(j+1).plus(p.getPosition());
//					if ((p1.minus(src).cross(dir))*(p2.minus(src).cross(dir)) < 0){
//						Vector n = new Vector(-p2.minus(p1).y, p2.minus(p1).x).normalize();
//						float t = ((p2.minus(src)).dot(n))/(dir.dot(n));
//						if (t >= 0){
//							Vector temp = src.plus(dir.smult(t));
//							if (point == null || temp.dist(src) < point.dist(src)){
//								point = temp;
//								entity = entities.get(i);
//							}
//						}
//					}
//				}
//			}
//		}
//		return new RaycastCollision(point, entity);
//	}



//	public Entity getEntity(String _id){
//		for (int i = 0; i < entities.size(); i++){
//			if (_id.equals(entities.get(i).id)){
//				return entities.get(i);
//			}
//		}
//		return null;
//	}



	//////////////////////////////////////////
	// Djikstra's Algorithm for pathfinding //
	//////////////////////////////////////////

//	public abstract ArrayList<Pathable> getPathableObjects();
//	public ArrayList<Pathable> createPath(ArrayList<Pathable> nodesList, Pathable a, Pathable b){
//		for (int i = 0; i < nodesList.size(); i++){
//			nodesList.get(i).setVisited(false);
//			nodesList.get(i).setHeuristicValue(1000000000);
//		}
//		PriorityQueue<Pathable> queue = new PriorityQueue<Pathable>();
//		a.setHeuristicValue(0);
//		queue.add(a);
//		while (!queue.isEmpty()){
//			Pathable p = queue.remove();
//			if (p.isVisited()) continue;
//			p.setVisited(true);
//			Pathable[] n = p.getNeighbors();
//			for (int i = 0; i < n.length; i++){
//				if (n[i] == null || n[i].isVisited() || !n[i].isHeuristicallyPassable()) continue;
//				double d = (p.getHeuristicValue() + p.getDistance(n[i]) + n[i].getDistance(b));
//				if (!p.isActuallyPassable()) d += 10000;
//				if (d < n[i].getHeuristicValue()){
//					n[i].setHeuristicValue(d);
//					n[i].setLastNode(p);
//				}
//				queue.add(n[i]);
//			}
//			if (b.getHeuristicValue() != 1000000000) break;
//		}
//		if (b.getHeuristicValue() != 1000000000){
//			ArrayList<Pathable> path = new ArrayList<Pathable>();
//			Pathable c = b;
//			while (c != a && c != null){
//				path.add(0, c);
//				c = c.getLastNode();
//			}
//			return path;
//		}
//		return null;
//	}
}
