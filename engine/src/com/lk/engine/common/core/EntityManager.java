/**
 * Desc:   Singleton class to handle the  management of Entities.
 *
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.common.core;

import com.google.gwt.core.client.GWT;
import com.google.gwt.logging.client.LogConfiguration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class EntityManager {
  private static Logger logger = Logger.getLogger(EntityManager.class.getName());

  // to facilitate quick lookup the entities are stored in a std::map, in which
  // pointers to entities are cross referenced by their identifying number
  private final Map<Integer, BaseGameEntity> entityMap = new HashMap<>(50);
  private final Map<String, BaseGameEntity> named = new HashMap<>(50);

  /**
   * this method stores a pointer to the entity in the std::vector m_Entities at
   * the index position indicated by the entity's ID (makes for faster access)
   */
  public void registerEntity(final BaseGameEntity newEntity) {
    entityMap.put(newEntity.Id(), newEntity);
    if (newEntity instanceof Named) {
      final String name = ((Named) newEntity).getName();
      if (LogConfiguration.loggingIsEnabled()) {
        if (named.containsKey(name)) {
          logger.warning("Entity was replaced - "+ name);
        } else {
          logger.warning("Entity added - "+ name);
        }
      }
      named.put(name, newEntity);
    }
  }

  /**
   * @return a pointer to the entity with the ID given as a parameter
   */
  public BaseGameEntity getEntityFromID(final int id) {
    // find the entity
    final BaseGameEntity ent = entityMap.get(id);
    // assert that the entity is a member of the map
    assert (ent != null) : "<EntityManager::GetEntityFromID>: invalid ID";
    return ent;
  }

  /**
   * @return a pointer to the entity with the ID given as a parameter
   */
  public BaseGameEntity getEntityByName(final String id) {
    // find the entity
    final BaseGameEntity ent = named.get(id);
    // assert that the entity is a member of the map
    assert (ent != null) : "<EntityManager::GetEntityFromID>: invalid ID: " + id;
    return ent;
  }


  /**
   * this method removes the entity from the list
   */
  public void removeEntity(final BaseGameEntity pEntity) {
    entityMap.remove(pEntity.Id());
  }

  /**
   * clears all entities from the entity map
   */
  public void reset() {
    entityMap.clear();
  }

  public Collection<BaseGameEntity> getAllEntities() {
    return entityMap.values();
  }

}
