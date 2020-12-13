package jyu.ties4560.demo3_4_recipedb.resource.subresource;

import java.net.URI;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.GET;
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
import jyu.ties4560.demo3_4_recipedb.domain.Recipe;
import jyu.ties4560.demo3_4_recipedb.domain.Tag;
import jyu.ties4560.demo3_4_recipedb.security.BasicSecurityContext;
import jyu.ties4560.demo3_4_recipedb.service.RecipeService;
import jyu.ties4560.demo3_4_recipedb.service.RecipeTagService;

/**
 * Resource for fetching
 * 
 * @author aleks
 *
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecipeTagResource {
	RecipeService recipeService = new RecipeService();
	RecipeTagService recipeTagService = new RecipeTagService();

	@GET
	@PermitAll
	public Response getAllRecipeTags(@PathParam("recipeId") Long recipeId, @Context UriInfo uriInfo) {
		List<Tag> tags = recipeService.getOne(recipeId).getTags();
		URI uri = uriInfo.getAbsolutePathBuilder().build();

		return Response.ok(uri).entity(tags).build();
	}

	@GET
	@PermitAll
	@Path("/{tagId}")
	public Response getOneTag(@PathParam("recipeId") Long recipeId, @PathParam("tagId") Long tagId,
			@Context UriInfo uriInfo) {
		Recipe recipe = recipeService.getOne(recipeId);
		Tag tag = recipeTagService.getOne(recipe, tagId);

		URI uri = uriInfo.getAbsolutePathBuilder().build();

		return Response.ok(uri).entity(tag).build();
	}

	@POST
	@RolesAllowed({ "user", "admin" })
	public Response addRecipeTag(@PathParam("recipeId") Long recipeId, Tag recipeTag,
			@Context ContainerRequestContext context) {
		BasicSecurityContext securityContext = (BasicSecurityContext) context.getSecurityContext();

		Recipe recipe = recipeService.getOne(recipeId);

		validateOwner(securityContext, recipe);

		recipe.getTags().add(recipeTag);

		URI uri = context.getUriInfo().getAbsolutePathBuilder().build();

		return Response.ok(uri).entity(recipeTag).build();
	}

	@PUT
	@RolesAllowed({ "user", "admin" })
	@Path("/{tagId}")
	public Response modifyRecipeTag(@PathParam("recipeId") Long recipeId, @PathParam("tagId") Long tagId, Tag recipeTag,
			@Context ContainerRequestContext context) {
		BasicSecurityContext securityContext = (BasicSecurityContext) context.getSecurityContext();

		Recipe recipe = recipeService.getOne(recipeId);

		validateOwner(securityContext, recipe);

		Tag tag = recipeTagService.update(recipe, recipeTag, tagId);

		URI uri = context.getUriInfo().getAbsolutePathBuilder().build();

		return Response.ok(uri).entity(tag).build();
	}

	@DELETE
	@RolesAllowed({ "user", "admin" })
	@Path("/{tagId}")
	public Response deleteRecipeTag(@PathParam("recipeId") Long recipeId, @PathParam("tagId") Long tagId, Tag recipeTag,
			@Context ContainerRequestContext context) {
		BasicSecurityContext securityContext = (BasicSecurityContext) context.getSecurityContext();

		Recipe recipe = recipeService.getOne(recipeId);

		validateOwner(securityContext, recipe);

		recipeTagService.delete(recipe, tagId);

		URI uri = context.getUriInfo().getAbsolutePathBuilder().build();

		return Response.noContent().location(uri).build();
	}
	
	private void validateOwner(BasicSecurityContext securityContext, Recipe recipe) {
		Account author = recipe.getAuthor().getAccount();

		if (!securityContext.isUserInRole("admin") && !securityContext.isUserEqualTo(author)) {
			throw new ForbiddenException("Access denied!");
		}
	}
}
