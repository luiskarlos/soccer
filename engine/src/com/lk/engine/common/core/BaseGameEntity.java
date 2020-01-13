/**
 * Desc: Base class to define a common interface for all game
 *       entities
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.common.core;

import static com.lk.engine.common.misc.NumUtils.maxOf;

import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.misc.Active;

public abstract class BaseGameEntity implements Updatable {
	public static final int DEFAULT_ENTITY_TYPE = -1;
	// each entity has a unique ID
	private int ID;
	// every entity has a type associated with it (health, troll, ammo etc)
	private int type = DEFAULT_ENTITY_TYPE;
	// this is a generic flag.
	private boolean tag;
	// this is the next valid ID. Each time a BaseGameEntity is instantiated
	// this value is updated
	private static int nextValidID = 1;

	// its location in the environment
	protected Vector2D position = new Vector2D();
	protected Vector2D scale = new Vector2D(1.0, 1.0);
	// the magnitude of this object's bounding radius
	protected double boundingRadius;

	public BaseGameEntity() {
		this(getNextValidID());
	}

	protected BaseGameEntity(final int ID) {
		setID(ID);
	}

	/**
	 * this must be called within each constructor to make sure the ID is set
	 * correctly. It verifies that the value passed to the method is greater or
	 * equal to the next valid ID, before setting the ID and incrementing the next
	 * valid ID
	 */
	private void setID(final int val) {
		// make sure the val is equal to or greater than the next available ID
		// assert (val >= nextValidID) : "<BaseGameEntity::SetID>: invalid ID";

		ID = val;

	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	@Override
	public Active update() {
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

	public Vector2D pos() {
		return new Vector2D(position);
	}

	public void setPos(final Vector2D new_pos) {
		position = new Vector2D(new_pos);
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

	public Vector2D scale() {
		return new Vector2D(scale);
	}

	public void setScale(final Vector2D val) {
		boundingRadius *= maxOf(val.x, val.y) / maxOf(scale.x, scale.y);
		scale = new Vector2D(val);
	}

	public void setScale(final double val) {
		boundingRadius *= (val / maxOf(scale.x, scale.y));
		scale = new Vector2D(val, val);
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
}
