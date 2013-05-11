package tests;
import static org.junit.Assert.*;

import org.junit.Test;

import pvpmagic.Player;
import pvpmagic.Resource;
import pvpmagic.Vector;
import pvpmagic.spells.AbracadabraSpell;
import pvpmagic.spells.BurnSpell;
import pvpmagic.spells.CleanseSpell;
import pvpmagic.spells.CloneSpell;
import pvpmagic.spells.ConfuseSpell;
import pvpmagic.spells.DashSpell;
import pvpmagic.spells.DisarmSpell;
import pvpmagic.spells.FelifySpell;
import pvpmagic.spells.HideSpell;
import pvpmagic.spells.LockSpell;
import pvpmagic.spells.OpenSpell;
import pvpmagic.spells.PushSpell;
import pvpmagic.spells.RejuvenateSpell;
import pvpmagic.spells.RootSpell;
import pvpmagic.spells.ShieldSpell;
import pvpmagic.spells.Spell;
import pvpmagic.spells.StunSpell;
import pvpmagic.spells.SummonSpell;

public class Tests {

	Resource r = new Resource();

	@Test
	public void playerTests(){
		Player p = new Player(null, "andrew", "Bob", new String[]{});
		p.changeHealth(-20, null);
		assertTrue(p._health == p._maxHealth-20);

		p.changeMana(40, null);
		assertTrue(p._mana == p._maxMana);

		Vector force = new Vector(20, 10);
		p.applyForce(force);
		assertTrue(p._force.equals(force));

		p.silence(1000, null);
		assertTrue(p._isSilenced);
	}

	@Test
	public void spellCreationTests(){
		assertTrue(Spell.newSpell(null,"Stun", null, new Vector(1,1)) instanceof StunSpell);
		assertTrue(Spell.newSpell(null,"Disarm", null, new Vector(1,1)) instanceof DisarmSpell);
		assertTrue(Spell.newSpell(null,"Burn", null, new Vector(1,1)) instanceof BurnSpell);
		assertTrue(Spell.newSpell(null,"Root", null, new Vector(1,1)) instanceof RootSpell);
		assertTrue(Spell.newSpell(null,"Push", null, new Vector(1,1)) instanceof PushSpell);
		assertTrue(Spell.newSpell(null,"Abracadabra", null, new Vector(1,1)) instanceof AbracadabraSpell);
		assertTrue(Spell.newSpell(null,"Open", null, new Vector(1,1)) instanceof OpenSpell);
		assertTrue(Spell.newSpell(null,"Lock", null, new Vector(1,1)) instanceof LockSpell);
		assertTrue(Spell.newSpell(null,"Confuse", null, new Vector(1,1)) instanceof ConfuseSpell);
		assertTrue(Spell.newSpell(null,"Rejuvenate", null, new Vector(1,1)) instanceof RejuvenateSpell);
		assertTrue(Spell.newSpell(null,"Cleanse", null, new Vector(1,1)) instanceof CleanseSpell);
		assertTrue(Spell.newSpell(null,"Summon", null, new Vector(1,1)) instanceof SummonSpell);
		assertTrue(Spell.newSpell(null,"Clone", null, new Vector(1,1)) instanceof CloneSpell);
		assertTrue(Spell.newSpell(null,"Hide", null, new Vector(1,1)) instanceof HideSpell);
		assertTrue(Spell.newSpell(null,"Dash", null, new Vector(1,1)) instanceof DashSpell);
		assertTrue(Spell.newSpell(null,"Felify", null, new Vector(1,1)) instanceof FelifySpell);
		assertTrue(Spell.newSpell(null,"Shield", null, new Vector(1,1)) instanceof ShieldSpell);
	}
}
