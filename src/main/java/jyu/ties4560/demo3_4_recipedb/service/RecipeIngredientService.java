package jyu.ties4560.demo3_4_recipedb.service;

import java.util.List;

import javax.ws.rs.NotFoundException;

import jyu.ties4560.demo3_4_recipedb.domain.Recipe;
import jyu.ties4560.demo3_4_recipedb.domain.RecipeIngredient;

public class RecipeIngredientService {
	
    public RecipeIngredient getOne(Recipe recipe, Long ingredientId) {
		RecipeIngredient recipeIngredient = recipe.getIngredients().stream()
				.filter(ingredient -> ingredient.getIngredient().getId() == ingredientId)
				.findFirst()
				.orElse(null);
		
		if (recipeIngredient == null) {
			throw new NotFoundException("Ingredient not found");
		} else {
			return recipeIngredient;
		}
    }


    public RecipeIngredient update(Recipe recipe, RecipeIngredient newIngredient, Long ingredientId) {
    	List<RecipeIngredient> ingredients = recipe.getIngredients();
    	RecipeIngredient oldIngredient = ingredients.stream()
				.filter(ingredient -> ingredient.getIngredient().getId() == ingredientId)
				.findFirst()
				.orElse(null);
    	if (oldIngredient == null || newIngredient.getIngredient().getId() != ingredientId) {
    		throw new NotFoundException("Ingredient not found");
    	}
    	
		ingredients.remove(oldIngredient);
		ingredients.add(newIngredient);
		return newIngredient;
    }

    public RecipeIngredient delete(Recipe recipe, Long ingredientId) {
    	List<RecipeIngredient> ingredients = recipe.getIngredients();
		ingredients.removeIf(ingredient -> ingredient.getIngredient().getId() == ingredientId);
		return null;
    }
}
