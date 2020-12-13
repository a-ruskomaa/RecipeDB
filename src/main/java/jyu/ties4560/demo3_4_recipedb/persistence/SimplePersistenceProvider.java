package jyu.ties4560.demo3_4_recipedb.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import jyu.ties4560.demo3_4_recipedb.domain.Account;
import jyu.ties4560.demo3_4_recipedb.domain.Ingredient;
import jyu.ties4560.demo3_4_recipedb.domain.Link;
import jyu.ties4560.demo3_4_recipedb.domain.Profile;
import jyu.ties4560.demo3_4_recipedb.domain.Recipe;
import jyu.ties4560.demo3_4_recipedb.domain.RecipeIngredient;
import jyu.ties4560.demo3_4_recipedb.domain.Tag;

/**
 * A mock database. Contains static HashMaps to store Recipes, Ingredients and Tags.
 * 
 * The static block can be used to insert test data. It will be run when this class is created
 * for the first time.
 * 
 * @author aleks
 *
 */
public class SimplePersistenceProvider {
	private static SimplePersistenceProvider simplePersistenceProvider;
	private Map<Long, Recipe> recipeDatabase;
	private Map<Long, Ingredient> ingredientDatabase;
	private Map<Long, Tag> tagDatabase;
	private Map<Long, Profile> profileDatabase;
	private Map<Long, Account> accountDatabase;
	
	public static synchronized SimplePersistenceProvider getInstance() {
		System.out.println("SimplePersistenceProvider: Getting an instance of SimplePersistenceProvider");
		if (simplePersistenceProvider == null) {
			System.out.println("SimplePersistenceProvider: Creating a singleton instance of SimplePersistenceProvider");
			
			Map<Long, Account> accountDb = new HashMap<Long, Account>();
			Map<Long, Profile> profileDb = new HashMap<Long, Profile>();
			Map<Long, Recipe> recipeDb = new HashMap<Long, Recipe>();
			Map<Long, Ingredient> ingredientDb = new HashMap<Long, Ingredient>();
			Map<Long, Tag> tagDb = new HashMap<Long, Tag>();
			
			simplePersistenceProvider = new SimplePersistenceProvider(accountDb, profileDb, recipeDb, ingredientDb, tagDb);
			
			populateTestData(accountDb, profileDb, recipeDb, ingredientDb, tagDb);
			
			return simplePersistenceProvider;
		}
		System.out.println("SimplePersistenceProvider: Returning an instance of SimplePersistenceProvider");
		return simplePersistenceProvider;
	}
	
	private SimplePersistenceProvider() {
	}

	
	private SimplePersistenceProvider(Map<Long, Account> accountDatabase, Map<Long, Profile> profileDatabase, Map<Long, Recipe> recipeDatabase, Map<Long, Ingredient> ingredientDatabase, Map<Long, Tag> tagDatabase) {
		this.accountDatabase = accountDatabase;
		this.profileDatabase = profileDatabase;
		this.recipeDatabase = recipeDatabase;
		this.ingredientDatabase = ingredientDatabase;
		this.tagDatabase = tagDatabase;
	}

	
	public Map<Long, Recipe> getRecipeDatabase() {
		return this.recipeDatabase;
	}
	
	public Map<Long, Ingredient> getIngredientDatabase() {
		return this.ingredientDatabase;
	}
	
	public Map<Long, Tag> getTagDatabase() {
		return this.tagDatabase;
	}
	
	public Map<Long, Profile> getProfileDatabase() {
		return this.profileDatabase;
	}

	public Map<Long, Account> getAccountDatabase() {
		return this.accountDatabase;
	}

	private static synchronized void populateTestData(Map<Long, Account> accountDb, Map<Long, Profile> profileDb, Map<Long, Recipe> recipeDb, Map<Long, Ingredient> ingredientDb, Map<Long, Tag> tagDb) {
		String root = "http://localhost:8080/demo3-recipedb/webapi/";
		System.out.println("SimplePersistenceProvider: Generating test data...");

		//these can be used to test authentication only
		Account admin = new Account(0L, "admin", DigestUtils.md5Hex("admin").toUpperCase(), new ArrayList<String>(Arrays.asList("user", "admin")), null);
		Profile adminProfile = new Profile(0L, "admin", admin);
		admin.setProfile(adminProfile);
		adminProfile.addLink(root + "users/0", "self");
		adminProfile.addLink(root + "users/0/recipes", "recipes");
		
		Account user = new Account(1L, "user", DigestUtils.md5Hex("password").toUpperCase(), new ArrayList<String>(Arrays.asList("user")), null);
		Profile userProfile = new Profile(1L, "user", user);
		user.setProfile(userProfile);
		userProfile.addLink(root + "users/1", "self");
		userProfile.addLink(root + "users/1/recipes", "recipes");
		
		//these can be used to test other features
		Account testAccount1 = new Account(2L, "johnlennon123", DigestUtils.md5Hex("123").toUpperCase(), new ArrayList<String>(Arrays.asList("user")));
		Profile testProfile1 = new Profile(2L, "John L", testAccount1);
		testAccount1.setProfile(testProfile1);
		testProfile1.addLink(root + "users/2", "self");
		testProfile1.addLink(root + "users/2/recipes", "recipes");
		
		Account testAccount2 = new Account(3L, "nancy_jordan", DigestUtils.md5Hex("drowssap").toUpperCase(), new ArrayList<String>(Arrays.asList("user")));
		Profile testProfile2 = new Profile(3L, "Mincy Nancy", testAccount2);
		testAccount2.setProfile(testProfile2);
		testProfile2.addLink(root + "users/3", "self");
		testProfile2.addLink(root + "users/3/recipes", "recipes");
		
		accountDb.put(0L, admin);
		accountDb.put(1L, user);
		accountDb.put(2L, testAccount1);
		accountDb.put(3L, testAccount2);
		System.out.println("SimplePersistenceProvider: Test accounts added!");
		
		profileDb.put(0L, adminProfile);
		profileDb.put(1L, userProfile);
		profileDb.put(2L, testProfile1);
		profileDb.put(3L, testProfile2);
		System.out.println("SimplePersistenceProvider: Test profiles added!");
		
		//test ingredients
		Ingredient ing1 = new Ingredient(1L, "milk", "liters", 1.0, 1.5);
		ingredientDb.put(1L,ing1);
		Ingredient ing2 = new Ingredient(2L, "wheat flour", "kg", 1.0, 1.5);
		ingredientDb.put(2L,ing2);
		Ingredient ing3 = new Ingredient(3L, "chili peppers", "pieces", 1.0, 0.2);
		ingredientDb.put(3L,ing3);
		Ingredient ing4 = new Ingredient(4L, "minced beef", "grams", 400.0, 3.5);
		ingredientDb.put(4L,ing4);
		Ingredient ing5 = new Ingredient(5L, "canned beans", "grams", 400.0, 0.8);
		ingredientDb.put(5L,ing5);
		Ingredient ing6 = new Ingredient(6L, "rice", "kg", 1.0, 1.2);
		ingredientDb.put(6L,ing6);
		Ingredient ing7 = new Ingredient(7L, "sugar", "kg", 1.0, 1.0);
		ingredientDb.put(7L,ing7);
		Ingredient ing8 = new Ingredient(8L, "cheese", "grams", 500.0, 4.5);
		ingredientDb.put(8L,ing8);
		Ingredient ing9 = new Ingredient(9L, "spaghetti", "grams", 200.0, 1.5);
		ingredientDb.put(9L,ing9);
		Ingredient ing10 = new Ingredient(10L, "canned tomatoes", "grams", 500.0, 0.8);
		ingredientDb.put(10L,ing10);
		System.out.println("SimplePersistenceProvider: Test ingredients added!");
	
		Tag tag1 = new Tag (1L, "#finnish");
		tagDb.put(1L, tag1);
		Tag tag2 = new Tag (2L, "#german");
		tagDb.put(2L, tag2);
		Tag tag3 = new Tag (3L, "#mexican");
		tagDb.put(3L, tag3);
		Tag tag4 = new Tag (4L, "#italian");
		tagDb.put(4L, tag4);
		Tag tag5 = new Tag (5L, "#asian");
		tagDb.put(5L, tag5);
		Tag tag6 = new Tag (6L, "#dessert");
		tagDb.put(6L, tag6);
		Tag tag7 = new Tag (7L, "#main course");
		tagDb.put(7L, tag7);
		System.out.println("SimplePersistenceProvider: Test tags added!");
		
		Recipe recipe1 = new Recipe(1L,
				"Cheesecake",
				testProfile2,
				4,
				new ArrayList<RecipeIngredient>(Arrays.asList(
						new RecipeIngredient(ing1, 1.0, Arrays.asList(
								new Link(root + "recipes/1/ingredients/1", "self"),
								new Link(root + "recipes/1/", "recipe"),
								new Link(root + "ingredients/1/", "ingredient"))),
						new RecipeIngredient(ing2, 0.3, Arrays.asList(
								new Link(root + "recipes/1/ingredients/2", "self"),
								new Link(root + "recipes/1/", "recipe"),
								new Link(root + "ingredients/2/", "ingredient"))),
						new RecipeIngredient(ing7, 0.3, Arrays.asList(
								new Link(root + "recipes/1/ingredients/7", "self"),
								new Link(root + "recipes/1/", "recipe"),
								new Link(root + "ingredients/7/", "ingredient"))),
						new RecipeIngredient(ing8, 0.4, Arrays.asList(
								new Link(root + "recipes/1/ingredients/8", "self"),
								new Link(root + "recipes/1/", "recipe"),
								new Link(root + "ingredients/8/", "ingredient"))))),
				new ArrayList<Tag>(Arrays.asList(tag1, tag6)),
						Arrays.asList(
								new Link(root + "recipes/1/", "self"),
								new Link(root + "users/3/", "author"),
								new Link(root + "recipes/1/ingredients/", "ingredients"),
								new Link(root + "recipes/1/tags/", "tags")));
		
		testProfile2.getRecipes().add(recipe1);
		recipeDb.put(1L, recipe1);
		
		Recipe recipe2 = new Recipe(2L, "Tortillas", testProfile1, 6,
				new ArrayList<RecipeIngredient>(Arrays.asList(
						new RecipeIngredient(ing2, 0.3, Arrays.asList(
								new Link(root + "recipes/2/ingredients/2", "self"),
								new Link(root + "recipes/2/", "recipe"),
								new Link(root + "ingredients/2/", "ingredient"))),
						new RecipeIngredient(ing3, 5.0, Arrays.asList(
								new Link(root + "recipes/2/ingredients/3", "self"),
								new Link(root + "recipes/2/", "recipe"),
								new Link(root + "ingredients/3/", "ingredient"))),
						new RecipeIngredient(ing5, 1.0, Arrays.asList(
								new Link(root + "recipes/2/ingredients/5", "self"),
								new Link(root + "recipes/2/", "recipe"),
								new Link(root + "ingredients/5/", "ingredient"))),
						new RecipeIngredient(ing6, 0.4, Arrays.asList(
								new Link(root + "recipes/2/ingredients/6", "self"),
								new Link(root + "recipes/2/", "recipe"),
								new Link(root + "ingredients/6/", "ingredient"))),
						new RecipeIngredient(ing8, 0.4, Arrays.asList(
								new Link(root + "recipes/2/ingredients/8", "self"),
								new Link(root + "recipes/2/", "recipe"),
								new Link(root + "ingredients/8/", "ingredient"))))),
				new ArrayList<Tag>(Arrays.asList(tag3, tag7)),
				Arrays.asList(
						new Link(root + "recipes/2/", "self"),
						new Link(root + "users/2/", "author"),
						new Link(root + "recipes/2/ingredients/", "ingredients"),
						new Link(root + "recipes/2/tags/", "tags")));
		
		testProfile1.getRecipes().add(recipe2);
		recipeDb.put(2L, recipe2);
		
		Recipe recipe3 = new Recipe(3L, "Hot Pot", testProfile1, 3,
				new ArrayList<RecipeIngredient>(Arrays.asList(
						new RecipeIngredient(ing3, 5.0, Arrays.asList(
								new Link(root + "recipes/3/ingredients/3", "self"),
								new Link(root + "recipes/3/", "recipe"),
								new Link(root + "ingredients/3/", "ingredient"))),
						new RecipeIngredient(ing4, 1.0, Arrays.asList(
								new Link(root + "recipes/3/ingredients/4", "self"),
								new Link(root + "recipes/3/", "recipe"),
								new Link(root + "ingredients/4/", "ingredient"))),
						new RecipeIngredient(ing6, 0.3, Arrays.asList(
								new Link(root + "recipes/3/ingredients/6", "self"),
								new Link(root + "recipes/3/", "recipe"),
								new Link(root + "ingredients/6/", "ingredient"))))),
				new ArrayList<Tag>(Arrays.asList(tag5, tag7)),
				Arrays.asList(
						new Link(root + "recipes/3/", "self"),
						new Link(root + "users/2/", "author"),
						new Link(root + "recipes/3/ingredients/", "ingredients"),
						new Link(root + "recipes/3/tags/", "tags")));
		
		testProfile1.getRecipes().add(recipe3);
		recipeDb.put(3L, recipe3);
		
		Recipe recipe4 = new Recipe(4L, "Pasta Bolognese", testProfile2, 3,
				new ArrayList<RecipeIngredient>(Arrays.asList(
						new RecipeIngredient(ing9, 0.5, Arrays.asList(
								new Link(root + "recipes/4/ingredients/9", "self"),
								new Link(root + "recipes/4/", "recipe"),
								new Link(root + "ingredients/9/", "ingredient"))),
						new RecipeIngredient(ing10, 1.0, Arrays.asList(
								new Link(root + "recipes/4/ingredients/10", "self"),
								new Link(root + "recipes/4/", "recipe"),
								new Link(root + "ingredients/10/", "ingredient"))),
						new RecipeIngredient(ing8, 0.4, Arrays.asList(
								new Link(root + "recipes/4/ingredients/8", "self"),
								new Link(root + "recipes/4/", "recipe"),
								new Link(root + "ingredients/8/", "ingredient"))),
						new RecipeIngredient(ing4, 1.0, Arrays.asList(
								new Link(root + "recipes/4/ingredients/4", "self"),
								new Link(root + "recipes/4/", "recipe"),
								new Link(root + "ingredients/4/", "ingredient"))),
						new RecipeIngredient(ing3, 0.5, Arrays.asList(
								new Link(root + "recipes/4/ingredients/3", "self"),
								new Link(root + "recipes/4/", "recipe"),
								new Link(root + "ingredients/3/", "ingredient"))))),
				new ArrayList<Tag>(Arrays.asList(tag4, tag7)),
				Arrays.asList(
						new Link(root + "recipes/4/", "self"),
						new Link(root + "users/3/", "author"),
						new Link(root + "recipes/4/ingredients/", "ingredients"),
						new Link(root + "recipes/4/tags/", "tags")));
		
		testProfile2.getRecipes().add(recipe4);
		recipeDb.put(4L, recipe4);
		

		System.out.println("SimplePersistenceProvider: Test recipes added!");
	}
	
}
