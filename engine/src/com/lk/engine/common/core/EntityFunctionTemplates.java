/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.common.core;

import static com.lk.engine.common.d2.Geometry.twoCirclesOverlapped;
import static com.lk.engine.common.d2.Vector2D.add;
import static com.lk.engine.common.d2.Vector2D.div;
import static com.lk.engine.common.d2.Vector2D.mul;
import static com.lk.engine.common.d2.Vector2D.sub;

import java.util.Collection;
import java.util.List;

import com.lk.engine.common.d2.Vector2D;

public class EntityFunctionTemplates {
	// ////////////////////////////////////////////////////////////////////////
	//
	// Some useful template functions
	//
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * tests to see if an entity is overlapping any of a number of entities stored
	 * in a std container
	 */
	public static boolean overlapped(final BaseGameEntity entity, final List<? extends BaseGameEntity> entities) {
		return overlapped(entity, entities, 40.0);
	}

	public static boolean overlapped(final BaseGameEntity entity, final List<? extends BaseGameEntity> entities,
	    final double MinDistBetweenObstacles) {
		for (final BaseGameEntity e : entities) {
			if (twoCirclesOverlapped(entity.pos(), entity.bRadius() + MinDistBetweenObstacles, e.pos(), e.bRadius())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * tags any entities contained in a std container that are within the radius
	 * of the single entity parameter
	 */
	public static void tagNeighbors(final BaseGameEntity entity, final List<? extends BaseGameEntity> entities,
	    final double radius) {
		for (final BaseGameEntity e : entities) {
			// first clear any current tag
			e.unTag();

			// work in distance squared to avoid sqrts
			final Vector2D to = sub(e.pos(), entity.pos());

			// the bounding radius of the other is taken into account by adding it
			// to the range
			final double range = radius + e.bRadius();

			// if entity within range, tag for further consideration
			if ((e != entity) && (to.lengthSq() < range * range)) {
				e.tag();
			}

		}// next entity
	}

	/**
	 * Given a pointer to an entity and a std container of pointers to nearby
	 * entities, this function checks to see if there is an overlap between
	 * entities. If there is, then the entities are moved away from each other
	 */
	public static void enforceNonPenetrationContraint(final BaseGameEntity entity,
	    final Collection<? extends BaseGameEntity> entities) {
		for (final BaseGameEntity e : entities) {
			// make sure we don't check against this entity
			if (e == entity) {
				continue;
			}
			// calculate the distance between the positions of the entities
			final Vector2D toEntity = sub(entity.pos(), e.pos());
			final double distFromEachOther = toEntity.length();

			// if this distance is smaller than the sum of their radii then this
			// entity must be moved away in the direction parallel to the
			// ToEntity vector
			final double amountOfOverLap = (e.bRadius() + entity.bRadius() - distFromEachOther) * 1.5;

			if (amountOfOverLap >= 0) {
				// move the entity a distance away equivalent to the amount of overlap.
				entity.setPos(add(entity.pos(), mul(div(toEntity, distFromEachOther), amountOfOverLap)));
			}
		}
	}
}