/**
 * Desc: Base class to define a common interface for all game
 *       entities
 *
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.common.core;

import static com.lk.engine.common.misc.NumUtils.maxOf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.lk.engine.common.d2.UVector2D;
import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.debug.Debug;
import com.lk.engine.common.debug.Debuggable;
import com.lk.engine.common.misc.Active;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsType;

public abstract class BaseGameEntity implements Updatable, Debuggable {
	public static final int DEFAULT_ENTITY_TYPE = -1;
	// each entity has a unique ID
	private int ID;
	// every entity has a type associated with it (health, troll, ammo etc)
	private int type = DEFAULT_ENTITY_TYPE;
	// this is a generic flag.
	private boolean tag;
	// this is the next valid ID. Each time a BaseGameEntity is instantiated
	// this value is updated
	private static int nextValidID = 2000; //reserve for fixed ids
	//id = 0 to 6 for teams
	//id = 10 to 600 for players

	// its location in the environment
	protected final Vector2D position = new Vector2D();

	protected Vector2D scale = new Vector2D(1.0, 1.0);
	// the magnitude of this object's bounding radius
	protected double boundingRadius;

	private List<Slot> slots = Collections.emptyList();

	protected BaseGameEntity() {
		this(getNextValidID());
	}

	protected BaseGameEntity(final int ID) {
		setID(ID);
	}

	@Override
  public void debug(Debug debug) {
		debug.put("id", ID);
		debug.put("position", position);
  }
	/**
	 * this must be called within each constructor to make sure the ID is set
	 * correctly. It verifies that the value passed to the method is greater or
	 * equal to the next valid ID, before setting the ID and incrementing the next
	 * valid ID
	 */
	private void setID(final int val) {
		ID = val;
	}

	public void mount(int slot, BaseGameEntity entity) {
		if (slots.isEmpty()) {
			if (slots.equals(Collections.emptyList()))
				slots = new ArrayList<BaseGameEntity.Slot>();

			slots.add(new Slot());
		}
		slots.get(slot).setEntity(entity);
	}

	public void unmount(int slot) {
		slots.get(slot).setEntity(null);
	}

	@Override
	public Active update(long time, int delta) {
		return Active.No;
	}

	// entities should be able to read/write their data to a stream
	// virtual void Write(std::ostream& os)const{}
	// virtual void Read (std::ifstream& is){}
	// use this to grab the next valid ID
	public static int getNextValidID() {
		return nextValidID++;
	}

	// this can be used to reset the next ID
	public static void resetNextValidID() {
		nextValidID = 0;
	}

	@JsMethod
	public UVector2D pos() {
		return position;
	}

	public void setPos(final UVector2D new_pos) {
		position.set(new_pos);
		for (Slot slot : slots) {
	    slot.updatePos();
    }
	}

	public double bRadius() {
		return boundingRadius;
	}

	public void setBRadius(final double r) {
		boundingRadius = r;
	}

	public boolean isTagged() {
		return tag;
	}

	public void tag() {
		tag = true;
	}

	public void unTag() {
		tag = false;
	}

	public UVector2D scale() {
		return scale;
	}

	public void setScale(final UVector2D val) {
		boundingRadius *= maxOf(val.x(), val.y()) / maxOf(scale.x, scale.y);
		scale.set(val);
	}

	public void setScale(final double val) {
		boundingRadius *= (val / maxOf(scale.x, scale.y));
		scale.x = val;
		scale.y = val;
	}

	public int entityType() {
		return type;
	}

	public void setEntityType(final int type) {
		this.type = type;
	}

	public int Id() {
		return ID;
	}

	private class Slot {
		private Vector2D relative = new Vector2D(5, 5);
		private BaseGameEntity entity = null;

		private void setEntity(BaseGameEntity entity) {
			this.entity = entity;
		}

		public void updatePos() {
			if (entity != null) {
				entity.setPos(Vector2D.add(position, relative));
			}
		}
	}
}
