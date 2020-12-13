package jyu.ties4560.demo3_4_recipedb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ws.rs.NotFoundException;

import jyu.ties4560.demo3_4_recipedb.domain.Ingredient;
import jyu.ties4560.demo3_4_recipedb.persistence.Dao;
import jyu.ties4560.demo3_4_recipedb.persistence.GenericDao;
import jyu.ties4560.demo3_4_recipedb.persistence.SimplePersistenceProvider;

public class IngredientService {
	private Dao<Long, Ingredient> ingredientDao;

	public IngredientService() {
		System.out.println("Creating IngredientService...");
		SimplePersistenceProvider simplePersistenceProvider = SimplePersistenceProvider.getInstance();
		System.out.println("IngredientService: Got an instance of SimplePersistenceProvider");
		Map<Long, Ingredient> ingredientDatabase = simplePersistenceProvider.getIngredientDatabase();
		System.out.println("IngredientService: Got ingredientDatabase");
		this.ingredientDao = new GenericDao<>(ingredientDatabase);
		System.out.println("IngredientService: Created ingredientDao");
		System.out.println("IngredientService created!");
	}

	public List<Ingredient> getAllIngredients() {
		return ingredientDao.getAll();
	}

	public List<Ingredient> getFilteredIngredients(String searchParam) {
		List<Ingredient> ingredients = ingredientDao.getAll();
		List<Ingredient> ingredientsToReturn = new ArrayList<Ingredient>();

		for (Ingredient ingredient : ingredients) {
			if (ingredient.getName().indexOf(searchParam) != -1) {
				ingredientsToReturn.add(ingredient);
			}
		}
		return ingredientsToReturn;
	}

	public Ingredient getOne(Long key) {
		Ingredient ingredient = ingredientDao.getByKey(key);
		if (ingredient == null) {
			throw new NotFoundException("Ingredient not found!");
		}
		return ingredient;
	}

	public Ingredient add(Ingredient ingredient) {
		return ingredientDao.add(ingredient);
	}

	public Ingredient update(Ingredient ingredient) {
		Ingredient ingredientToUpdate = ingredientDao.getByKey(ingredient.getId());
		if (ingredientToUpdate == null) {
			throw new NotFoundException("Ingredient not found!");
		}
		return ingredientDao.update(ingredient);
	}

	public Ingredient delete(Long key) {
		Ingredient ingredient = ingredientDao.getByKey(key);
		if (ingredient == null) {
			throw new NotFoundException("Ingredient not found!");
		}
		return ingredientDao.delete(key);
	}
}