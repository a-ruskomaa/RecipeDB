package jyu.ties4560.demo3_4_recipedb.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.NotFoundException;

import jyu.ties4560.demo3_4_recipedb.domain.Ingredient;
import jyu.ties4560.demo3_4_recipedb.domain.Profile;
import jyu.ties4560.demo3_4_recipedb.domain.Recipe;
import jyu.ties4560.demo3_4_recipedb.domain.Tag;
import jyu.ties4560.demo3_4_recipedb.persistence.Dao;
import jyu.ties4560.demo3_4_recipedb.persistence.GenericDao;
import jyu.ties4560.demo3_4_recipedb.persistence.SimplePersistenceProvider;

/**
 * A service class that connects to the database using data access objects (Dao)
 * and passes fetched resources to resource handler that constructs the response.
 *
 * Allows getting all recipes, applying filters for recipes and all other basic CRUD operations
 * @author aleks
 *
 */
public class RecipeService {

	private Dao<Long, Recipe> recipeDao;
	private Dao<Long, Ingredient> ingredientDao;
	private Dao<Long, Tag> tagDao;
	private Dao<Long, Profile> userDao;


	public RecipeService() {
		System.out.println("Creating RecipeService...");
		SimplePersistenceProvider simplePersistenceProvider = SimplePersistenceProvider.getInstance();
		System.out.println("RecipeService: Got an instance of SimplePersistenceProvider");
		Map<Long, Recipe> recipeDatabase = simplePersistenceProvider.getRecipeDatabase();
		System.out.println("RecipeService: Got recipeDatabase");
		this.recipeDao = new GenericDao<>(recipeDatabase);
		System.out.println("RecipeService: Created recipeDao");

		Map<Long, Ingredient> ingredientDatabase = simplePersistenceProvider.getIngredientDatabase();
		System.out.println("RecipeService: Got ingredientDatabase");
		this.ingredientDao = new GenericDao<>(ingredientDatabase);
		System.out.println("RecipeService: Created ingredientDao");

		Map<Long, Tag> tagDatabase = simplePersistenceProvider.getTagDatabase();
		System.out.println("RecipeService: Got tagDatabase");
		this.tagDao = new GenericDao<>(tagDatabase);
		System.out.println("RecipeService: Created tagDao");
		
		Map<Long, Profile> userDatabase = simplePersistenceProvider.getProfileDatabase();
		System.out.println("RecipeService: Got userDatabase");
		this.userDao = new GenericDao<>(userDatabase);
		System.out.println("RecipeService: Created userDao");
		
		System.out.println("RecipeService created!");
	}

	public List<Recipe> getAllRecipes() {
		return recipeDao.getAll();
	}

	/**
	 * Queries the database for all entries and applies all filters that are passed as arguments
	 * 
	 * @param author Author name
	 * @param ingredients The filtered recipes must contain all of the given ingredient names
	 * @param tags The recipes must contain all of the given tagnames
	 * @param maxprice The recipe must have a pricePerPortion value equal to or less than the maxprice
	 * 
	 * @return return a list that contains all recipes that match the filters
	 */
	public List<Recipe> getFilteredRecipes(String author, String name, List<String> ingredientIds, List<String> tags, Long maxprice) {
		Set<Ingredient> ingredientFilter = getIngredientFilter(ingredientIds);
		Set<Tag> tagFilter = getTagFilter(tags);

		return recipeDao.getAll().stream()
				.filter(recipe -> author == null || recipe.getAuthor().getName().equals(author))
				.filter(recipe -> name == null || recipe.getName().toLowerCase().contains(name.toLowerCase()))
				.filter(recipe -> maxprice == null || recipe.getPricePerPortion() <= maxprice)
				.filter(recipe -> filterByIngredients(recipe, ingredientFilter))
				.filter(recipe -> filterByTags(recipe, tagFilter))
				.collect(Collectors.toList());
	}

	public Recipe getOne(Long key) {
		Recipe recipe = recipeDao.getByKey(key);
		if (recipe == null) {
			throw new NotFoundException("Recipe not found!");
		}
		return recipe;
	}

	public Recipe add(Recipe recipe) {
		Recipe newRecipe =  recipeDao.add(recipe);
		
		Profile profile = userDao.getByKey(newRecipe.getAuthor().getId());
		
		profile.getRecipes().add(newRecipe);
		
		return newRecipe;
	}

	public Recipe update(Recipe recipe) {
		Recipe oldRecipe = recipeDao.getByKey(recipe.getId());
		if (oldRecipe == null) {
			throw new NotFoundException("Recipe not found!");
		}
		Recipe updatedRecipe = recipeDao.update(recipe);
	
		Profile profile = userDao.getByKey(updatedRecipe.getAuthor().getId());
		
		profile.getRecipes().remove(oldRecipe);
		profile.getRecipes().add(updatedRecipe);
		
		return updatedRecipe;
	}

	public Recipe delete(Long key) {
		Recipe recipe = recipeDao.getByKey(key);
		if (recipe == null) {
			throw new NotFoundException("Recipe not found!");
		}
		Profile profile = userDao.getByKey(recipe.getAuthor().getId());
		
		profile.getRecipes().remove(recipe);
		
		return recipeDao.delete(key);
	}

	/**
	 * Checks if the recipe contains all the ingredients that are passed as arguments
	 * 
	 * @param recipe The recipe that is compared
	 * @param ingredients The ingredients that the recipe must contain
	 * @return returns true only if the recipe contains all the given ingredients
	 */
	private boolean filterByIngredients(Recipe recipe, Set<Ingredient> ingredients) {
		return recipe.getIngredients().stream()
				.map(recipeIngredient -> recipeIngredient.getIngredient())
				.collect(Collectors.toSet())
				.containsAll(ingredients);
	}

	/**
	 * Checks if the recipe contains all the tags that are passed as arguments
	 * 
	 * @param recipe The recipe that is compared
	 * @param tags The tags that the recipe must contain
	 * @return returns true only if the recipe contains all the given tags
	 */
	private boolean filterByTags(Recipe recipe, Set<Tag> tags) {
		return recipe.getTags().containsAll(tags);
	}
	
	/**
	 * Constructs a set of Ingredient objects that can be applied as a filter
	 * @param ingredients List of ingredient id's
	 * @return returns a set that contains Ingredient objects that correspond to the given id's
	 */
	private Set<Ingredient> getIngredientFilter(List<String> ingredientIds) {
		if (ingredientIds.isEmpty()) {
			return Collections.emptySet();
		}
		
		return ingredientIds.stream()
				.map(ingredientId -> ingredientDao.getByKey(Long.valueOf(ingredientId)))
				.collect(Collectors.toSet());
	}
	
	/**
	 * Constructs a set of Tag objects that can be applied as a filter
	 * @param tags List of tag names
	 * @return returns a set that contains Tag objects that correspond to the given tag names
	 */
	private Set<Tag> getTagFilter(List<String> tags) {
		if (tags.isEmpty()) {
			return Collections.emptySet();
		}

		return tags.stream()
				.map(tagDao::getByName)
				.collect(Collectors.toSet());
	}

}
