package jyu.ties4560.demo3_4_recipedb.resource;

import java.net.URI;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
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
import jyu.ties4560.demo3_4_recipedb.domain.Profile;
import jyu.ties4560.demo3_4_recipedb.domain.Recipe;
import jyu.ties4560.demo3_4_recipedb.security.BasicSecurityContext;
import jyu.ties4560.demo3_4_recipedb.service.ProfileService;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/users")
public class ProfileResource {
	ProfileService profileService = new ProfileService();

	@GET
	@RolesAllowed({ "admin" })
	public Response getAllusers(@Context UriInfo uriInfo) {
		List<Profile> profiles;

		System.out.println("Getting all users");
		profiles = profileService.getAllUsers();

		URI uri = uriInfo.getAbsolutePathBuilder().build();

		return Response.ok(uri).entity(profiles).build();
	}

	@GET
	@RolesAllowed({ "user" })
	@Path("/{userId}")
	public Response getOneUser(@PathParam("userId") Long userId, @Context UriInfo uriInfo) {
		System.out.println("Getting a user");

		Profile profile = profileService.getOne(userId);

		URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(userId)).build();

		return Response.ok(uri).entity(profile).build();
	}

	@POST
	@RolesAllowed({ "user" })
	public Response addUser(Profile profile, @Context ContainerRequestContext context) {
		BasicSecurityContext securityContext = (BasicSecurityContext) context.getSecurityContext();

		Account current = (Account) securityContext.getUserPrincipal();

		if (current.getProfile() != null) {
			throw new BadRequestException();
		}

		try {
			profile.setAccount(current);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new ForbiddenException();
		}

		System.out.println("Adding a user");

		Profile newUser = profileService.add(profile);

		current.setProfile(newUser);

		String newId = String.valueOf(newUser.getId());

		UriInfo uriInfo = context.getUriInfo();

		newUser.addLink(getUriForUser(uriInfo, profile).toString(), "self");
		newUser.addLink(getUriForRecipes(uriInfo, profile).toString(), "recipes");

		URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();

		return Response.created(uri).entity(newUser).build();
	}

	@PUT
	@RolesAllowed({ "user" })
	@Path("/{userId}")
	public Response updateUser(@PathParam("userId") Long userId, Profile profile, @Context ContainerRequestContext context) {
		BasicSecurityContext securityContext = (BasicSecurityContext) context.getSecurityContext();

		Account account = profileService.getOne(userId).getAccount();

		System.out.println("Updating a user");
		System.out.println(account);
		System.out.println(securityContext.getUserPrincipal().getName());

		if (!securityContext.isUserEqualTo(account)) {
			throw new ForbiddenException();
		}

		try {
			profile.setAccount(account);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new ForbiddenException();
		}

		System.out.println("Updating a user");
		profile.setId(userId);
		Profile updatedUser = profileService.update(profile);

		account.setProfile(updatedUser);

		UriInfo uriInfo = context.getUriInfo();

		URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(userId)).build();

		return Response.ok(uri).entity(updatedUser).build();
	}

	@DELETE
	@RolesAllowed({ "admin" })
	@Path("/{userId}")
	public Response deleteUser(@PathParam("userId") Long userId, @Context ContainerRequestContext context) {
		BasicSecurityContext securityContext = (BasicSecurityContext) context.getSecurityContext();

		Account account = profileService.getOne(userId).getAccount();

		if (!securityContext.isUserInRole("admin") && !securityContext.isUserEqualTo(account)) {
			throw new ForbiddenException();
		}
		
		System.out.println("Deleting a user");
		profileService.delete(userId);

		return Response.noContent().build();
	}

	@GET
	@RolesAllowed({ "user" })
	@Path("/{userId}/recipes")
	public Response getUsersRecipes(@PathParam("userId") Long userId, @Context UriInfo uriInfo) {
		System.out.println("Getting all recipes from user " + userId);

		Profile profile = profileService.getOne(userId);

		System.out.println(profile.getName());

		List<Recipe> recipes = profile.getRecipes();

		System.out.println(profile.getRecipes());

		URI uri = uriInfo.getAbsolutePath();

		return Response.ok(uri).entity(recipes).build();
	}

	private URI getUriForUser(UriInfo uriInfo, Profile profile) {
		URI uri = uriInfo.getBaseUriBuilder().path(ProfileResource.class).path(Long.toString(profile.getId())).build();
		return uri;
	}

	private URI getUriForRecipes(UriInfo uriInfo, Profile profile) {
		URI uri = uriInfo.getBaseUriBuilder().path(ProfileResource.class).path(ProfileResource.class, "getUsersRecipes")
				.resolveTemplate("userId", profile.getId()).build();
		return uri;
	}

}
