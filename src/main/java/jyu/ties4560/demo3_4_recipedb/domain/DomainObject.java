package jyu.ties4560.demo3_4_recipedb.domain;

/**
 * Interface for domain objects, like Recipe, Ingredient and Tag. Will allow using the GeneridDao as a persistence provider.
 * @author aleks
 *
 */
public interface DomainObject {

	public Long getId();
	
	public void setId(Long id);
	
	public String getName();
	
	public void setName(String name);
}
