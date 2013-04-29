package pvpmagic.spells;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

import pvpmagic.*;


public class BurnSpell extends Spell {

	public BurnSpell(GameData data, Player caster, Vector dir) {
		super(data, caster, dir);
		_size = new Vector(10, 10);
		setVelocity(4);
	}
	

	public void collide(Collision c){
		//if (u.getClass() == Player.class) {
		//final Player target = (Player) u;
		System.out.println("HIT BURN SPELL");
		final int manaDecreasePerSecond = 5;
		final Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {
			private int count = 0;
			private int duration = 10;
			@Override
			public void run() {
				if (count >= duration) {
					System.out.println("REACHED END");
					this.cancel();
					t.cancel();
					t.purge();
				}
				System.out.println(count);
			//	target.decreaseMana(manaDecreasePerSecond);
				count++;
			}
		}, 0, 3000);
		//}
		//u.burn(10);
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.red);
		v.fillRect(_pos, _size);
	}
}
