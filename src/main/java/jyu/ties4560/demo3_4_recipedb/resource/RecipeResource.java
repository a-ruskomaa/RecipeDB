package jyu.ties4560.demo3_4_recipedb.resource;

import java.net.URI;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import jyu.ties4560.demo3_4_recipedb.domain.Account;
import jyu.ties4560.demo3_4_recipedb.domain.Profile;
import jyu.ties4560.demo3_4_recipedb.domain.Recipe;
import jyu.ties4560.demo3_4_recipedb.resource.subresource.RecipeIngredientResource;
import jyu.ties4560.demo3_4_recipedb.resource.subresource.RecipeTagResource;
import jyu.ties4560.demo3_4_recipedb.security.BasicSecurityContext;
import jyu.ties4560.demo3_4_recipedb.service.RecipeService;

/**
 * Handles CRUD-operations for Recipe resources.
 * 
 * @author aleks
 *
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/recipes")
public class RecipeResource {
	RecipeService recipeService = new RecipeService();

	/**
	 * Returns all recipes if no filters are present or filters the result if any
	 * suitable query param is passed
	 */
	@GET
	@PermitAll
	public Response getAllRecipes(@QueryParam("author") String author, @QueryParam("name") String name,
			@QueryParam("ingredient") List<String> ingredients, @QueryParam("tag") List<String> tags,
			@QueryParam("maxprice") Long maxprice, @Context UriInfo uriInfo) {

		List<Recipe> recipes;
		if (author != null || name != null || !ingredients.isEmpty() || !tags.isEmpty() || maxprice != null) {
			System.out.println("Getting filtered recipes");
			recipes = recipeService.getFilteredRecipes(author, name, ingredients, tags, maxprice);
		} else {
			System.out.println("Getting all recipes");
			recipes = recipeService.getAllRecipes();
		}

		System.out.println("Got " + recipes.size() + " recipes!");

		URI uri = uriInfo.getAbsolutePathBuilder().build();

		return Response.ok(uri).entity(recipes).build();
	}

	
	@GET
	@PermitAll
	@Path("/{recipeId}")
	public Response getOneRecipe(@PathParam("recipeId") Long recipeId, @Context UriInfo uriInfo) {
		System.out.println("Getting a recipe");

		Recipe recipe = recipeService.getOne(recipeId);

		URI uri = getUriForSelf(uriInfo, recipe);

		return Response.ok(uri).entity(recipe).build();
	}

	
	@POST
	@RolesAllowed({ "user" })
	public Response addRecipe(@Valid Recipe recipe, @Context ContainerRequestContext context) {
		BasicSecurityContext securityContext = (BasicSecurityContext) context.getSecurityContext();
		
		Profile current = securityContext.getCurrentProfile();

		System.out.println("User '" + current.getName() + "' is adding a recipe");

		recipe.setAuthor(current);

		Recipe newRecipe = recipeService.add(recipe);

		UriInfo uriInfo = context.getUriInfo();
		URI uri = getUriForSelf(uriInfo, recipe);

		newRecipe = addLinks(uriInfo, newRecipe);

		return Response.created(uri).entity(newRecipe).build();
	}

	
	@PUT
	@RolesAllowed({ "user", "admin" })
	@Path("/{recipeId}")
	public Response updateRecipe(@PathParam("recipeId") Long recipeId, Recipe recipe, @Context ContainerRequestContext context) {
		BasicSecurityContext securityContext = (BasicSecurityContext) context.getSecurityContext();

		validateOwner(securityContext, recipe);

		recipe.setId(recipeId);
		Recipe updatedRecipe = recipeService.update(recipe);
		
		UriInfo uriInfo = context.getUriInfo();
		URI uri = getUriForSelf(uriInfo, recipe);

		return Response.ok(uri).entity(updatedRecipe).build();
	}

	
	@DELETE
	@RolesAllowed({ "user", "admin" })
	@Path("/{recipeId}")
	public Response deleteRecipe(@PathParam("recipeId") Long recipeId, @Context ContainerRequestContext context) {
		BasicSecurityContext securityContext = (BasicSecurityContext) context.getSecurityContext();

		Recipe recipe = recipeService.getOne(recipeId);

		validateOwner(securityContext, recipe);
		
		recipeService.delete(recipeId);

		return Response.noContent().build();
	}


	// sub resource
	@Path("/{recipeId}/ingredients")
	public RecipeIngredientResource getRecipeIngredientResource() {
		return new RecipeIngredientResource();
	}

	// sub resource
	@Path("/{recipeId}/tags")
	public RecipeTagResource getRecipeTagResource() {
		return new RecipeTagResource();
	}
	
	
	/**
	 * Convenience method to check if the current user is authorized to alter the requested resource
	 */
	private void validateOwner(BasicSecurityContext securityContext, Recipe recipe) {
		Account author = recipe.getAuthor().getAccount();

		if (!securityContext.isUserInRole("admin") && !securityContext.isUserEqualTo(author)) {
			throw new ForbiddenException("Access denied!");
		}
	}
	
	
	private Recipe addLinks(UriInfo uriInfo, Recipe recipe) {
		recipe.addLink(getUriForSelf(uriInfo, recipe).toString(), "self");
		recipe.addLink(getUriForAuthor(uriInfo, recipe).toString(), "author");
		recipe.addLink(getUriForIngredients(uriInfo, recipe).toString(), "ingredients");
		recipe.addLink(getUriForTags(uriInfo, recipe).toString(), "tags");
		
		return recipe;
	}

	
	private URI getUriForIngredients(UriInfo uriInfo, Recipe recipe) {
		URI uri = uriInfo.getBaseUriBuilder().path(RecipeResource.class)
				.path(RecipeResource.class, "getRecipeIngredientResource").resolveTemplate("recipeId", recipe.getId())
				.build();
		return uri;
	}

	
	private URI getUriForTags(UriInfo uriInfo, Recipe recipe) {
		URI uri = uriInfo.getBaseUriBuilder().path(RecipeResource.class)
				.path(RecipeResource.class, "getRecipeTagResource").resolveTemplate("recipeId", recipe.getId()).build();
		return uri;
	}

	
	private URI getUriForSelf(UriInfo uriInfo, Recipe recipe) {
		URI uri = uriInfo.getBaseUriBuilder().path(RecipeResource.class).path(Long.toString(recipe.getId())).build();
		return uri;
	}
	
	
	private URI getUriForAuthor(UriInfo uriInfo, Recipe recipe) {
		URI uri = uriInfo.getBaseUriBuilder().path(ProfileResource.class).path(Long.toString(recipe.getAuthor().getId())).build();
		return uri;
	}

}
