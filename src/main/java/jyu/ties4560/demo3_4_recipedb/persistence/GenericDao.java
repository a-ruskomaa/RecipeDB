package jyu.ties4560.demo3_4_recipedb.persistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jyu.ties4560.demo3_4_recipedb.domain.DomainObject;

/**
 * A generic DAO implementation that should be enough for our purposes.
 * 
 * Supports Long as a key attribute type and classes that implement DomainObject as values.
 * 
 * Automatically assigns created objects with an id that is larger than the current max value.
 * 
 * It's best to instantiate this class as Dao<Long, ? implements DomainObject>.
 * 
 * @author aleks
 *
 * @param <K>
 * @param <V>
 */
public class GenericDao<L, V extends DomainObject> implements Dao<Long, V> {
	private Map<Long,V> database;
	
	public GenericDao(Map<Long,V> database) {
		this.database = database;
	}
	
	public void setDatabase(Map<Long,V> database) {
		this.database = database;
	}

	@Override
	public List<V> getAll() {
		return new ArrayList<V>(database.values());
	}

	@Override
	public V getByKey(Long key) {
		return database.get(key);
	}

	@Override
	public V getByName(String name) {
		return database.values().stream()
				.filter(v -> v.getName().equals(name))
				.findFirst().orElse(null);
	}
	
	@Override
	public List<V> searchByName(String name) {
		return database.values().stream()
				.filter(v -> v.getName().contains(name))
				.collect(Collectors.toList());
	}

	@Override
	public V add(V object) {
		Long id;
		if (database.isEmpty()) {
			id = 1L;
		} else {			
			id = Collections.max(database.keySet()) + 1;
		}
		object.setId(id);
		database.put(id, object);
		
		System.out.println("Added: " + object);
		
		return object;
	}

	@Override
	public V update(V object) {
		if (object.getId() <= 0) {
			return null;
		}
		database.put(object.getId(), object);
		System.out.println("Updated: " + object);
		return object;
	}

	@Override
	public V delete(Long key) {
		return database.remove(key);
	}


}
