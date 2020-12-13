package jyu.ties4560.demo3_4_recipedb.resource.subresource;

import java.net.URI;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import jyu.ties4560.demo3_4_recipedb.domain.Account;
import jyu.ties4560.demo3_4_recipedb.domain.Ingredient;
import jyu.ties4560.demo3_4_recipedb.domain.Recipe;
import jyu.ties4560.demo3_4_recipedb.domain.RecipeIngredient;
import jyu.ties4560.demo3_4_recipedb.resource.IngredientResource;
import jyu.ties4560.demo3_4_recipedb.resource.RecipeResource;
import jyu.ties4560.demo3_4_recipedb.security.BasicSecurityContext;
import jyu.ties4560.demo3_4_recipedb.service.RecipeIngredientService;
import jyu.ties4560.demo3_4_recipedb.service.RecipeService;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecipeIngredientResource {
	RecipeService recipeService = new RecipeService();
	RecipeIngredientService recipeIngredientService = new RecipeIngredientService();

	@GET
	@PermitAll
	public Response getAllRecipeIngredients(@PathParam("recipeId") Long recipeId, @Context UriInfo uriInfo) {
		Recipe recipe = recipeService.getOne(recipeId);
		List<RecipeIngredient> ingredients = recipe.getIngredients();
		
		URI uri = uriInfo.getAbsolutePathBuilder().build();
		
		return Response.ok(uri).entity(ingredients).build();
	}

	@GET
	@PermitAll
	@Path("/{ingredientId}")
	public Response getOneIngredient(@PathParam("recipeId") Long recipeId, @PathParam("ingredientId") Long ingredientId,
			@Context UriInfo uriInfo) {
		Recipe recipe = recipeService.getOne(recipeId);
		RecipeIngredient ingredient = recipeIngredientService.getOne(recipe, ingredientId);

		URI uri = uriInfo.getAbsolutePathBuilder().build();

		return Response.ok(uri).entity(ingredient).build();

	}

	@POST
	@RolesAllowed({ "user", "admin" })
	public Response addRecipeIngredient(@PathParam("recipeId") Long recipeId, RecipeIngredient recipeIngredient,
			@Context ContainerRequestContext context) {
		BasicSecurityContext securityContext = (BasicSecurityContext) context.getSecurityContext();
		Recipe recipe = recipeService.getOne(recipeId);

		Account author = recipe.getAuthor().getAccount();

		validateOwner(securityContext, recipe);
		
		recipe.getIngredients().add(recipeIngredient);
		
		UriInfo uriInfo = context.getUriInfo();

		URI uri = uriInfo.getAbsolutePathBuilder().build();
		
		recipeIngredient.addLink(getUriForSelf(uriInfo, recipe, recipeIngredient).toString(), "self");
		recipeIngredient.addLink(getUriForParent(uriInfo, recipe).toString(), "recipe");
		recipeIngredient.addLink(getUriForIngredient(uriInfo, recipeIngredient.getIngredient()).toString(), "ingredient");

		return Response.ok(uri).entity(recipeIngredient).build();
	}

	@PUT
	@RolesAllowed({ "user", "admin" })
	@Path("/{ingredientId}")
	public Response modifyRecipeIngredient(@PathParam("recipeId") Long recipeId,
			@PathParam("ingredientId") Long ingredientId, RecipeIngredient recipeIngredient,
			@Context ContainerRequestContext context) {
		BasicSecurityContext securityContext = (BasicSecurityContext) context.getSecurityContext();
		Recipe recipe = recipeService.getOne(recipeId);

		Account author = recipe.getAuthor().getAccount();

		validateOwner(securityContext, recipe);
		
		RecipeIngredient newIngredient = recipeIngredientService.update(recipe, recipeIngredient, ingredientId);

		UriInfo uriInfo = context.getUriInfo();

		URI uri = uriInfo.getAbsolutePathBuilder().build();

		return Response.ok(uri).entity(newIngredient).build();
	}

	@DELETE
	@RolesAllowed({ "user", "admin" })
	@Path("/{ingredientId}")
	public Response deleteRecipeIngredient(@PathParam("recipeId") Long recipeId,
			@PathParam("ingredientId") Long ingredientId, RecipeIngredient recipeIngredient,
			@Context ContainerRequestContext context) {
		BasicSecurityContext securityContext = (BasicSecurityContext) context.getSecurityContext();

		Recipe recipe = recipeService.getOne(recipeId);

		validateOwner(securityContext, recipe);

		recipeIngredientService.delete(recipe, ingredientId);

		URI uri = context.getUriInfo().getAbsolutePathBuilder().build();

		return Response.noContent().location(uri).build();
	}
	
	
	private void validateOwner(BasicSecurityContext securityContext, Recipe recipe) {
		Account author = recipe.getAuthor().getAccount();

		if (!securityContext.isUserInRole("admin") && !securityContext.isUserEqualTo(author)) {
			throw new ForbiddenException("Access denied!");
		}
	}

	private URI getUriForSelf(UriInfo uriInfo, Recipe recipe, RecipeIngredient recipeIngredient) {
		URI uri = uriInfo.getBaseUriBuilder().path(RecipeResource.class)
				.path(RecipeResource.class, "getRecipeIngredientResource").resolveTemplate("recipeId", recipe.getId())
				.path(Long.toString(recipeIngredient.getIngredient().getId()))
				.build();
		return uri;
	}

	private URI getUriForParent(UriInfo uriInfo, Recipe recipe) {
		URI uri = uriInfo.getBaseUriBuilder().path(RecipeResource.class).path(Long.toString(recipe.getId())).build();
		return uri;
	}
	
	private URI getUriForIngredient(UriInfo uriInfo, Ingredient ingredient) {
		URI uri = uriInfo.getBaseUriBuilder().path(IngredientResource.class)
				.path(Long.toString(ingredient.getId()))
				.build();
		return uri;
	}
	

}
