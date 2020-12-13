package jyu.ties4560.demo3_4_recipedb.persistence;

import java.util.List;

/**
 * DAO interface to hide implementation details. It's advisable to instantiate DAO objects
 * inside Service classes as objects of this interface.
 * @author aleks
 *
 * @param <K> Type of the key attribute
 * @param <V> Type of the value attribute
 */
public interface Dao<K,V> {
	
	public List<V> getAll();
	
	public V getByKey(K key);
	
	public V getByName(String name);
	
	public List<V> searchByName(String name);
	
	public V add(V object);
	
	public V update(V object);
	
	public V delete(K key);

}
