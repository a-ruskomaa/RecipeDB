package jyu.ties4560.demo3_4_recipedb.resource;

import java.net.URI;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import jyu.ties4560.demo3_4_recipedb.domain.Tag;
import jyu.ties4560.demo3_4_recipedb.service.TagService;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/tags")

public class TagResource {
	TagService tagService = new TagService();

	@GET
	@PermitAll
	public Response getAlltags(@Context UriInfo uriInfo) {
		List<Tag> tags;
		tags = tagService.getAllTags();

		URI uri = uriInfo.getAbsolutePathBuilder().build();

		return Response.ok(uri).entity(tags).build();
	}

	@GET
	@PermitAll
	@Path("/{tagid}")
	public Response getOneTag(@PathParam("tagid") Long tagId, @Context UriInfo uriInfo) {
		System.out.println("Getting a tag");

		Tag tag = tagService.getOne(tagId);

		URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(tagId)).build();

		return Response.ok(uri).entity(tag).build();
	}

	@POST
	@RolesAllowed({ "user" })
	public Response addTag(@Valid Tag tag, @Context UriInfo uriInfo) {
		System.out.println("Adding a tag");

		Tag newTag = tagService.add(tag);
		String newId = String.valueOf(newTag.getId());

		URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();

		return Response.created(uri).entity(newTag).build();
	}

	@PUT
	@RolesAllowed({ "admin" })
	@Path("/{tagId}")
	public Response updateTag(@PathParam("tagId") Long tagId, Tag tag, @Context UriInfo uriInfo) {
		System.out.println("Updating a tag");
		tag.setId(tagId);
		Tag updatedTag = tagService.update(tag);

		URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(tagId)).build();

		return Response.ok(uri).entity(updatedTag).build();
	}

	@DELETE
	@RolesAllowed({ "admin" })
	@Path("/{tagId}")
	public Response deleteTag(@PathParam("tagId") Long tagId) {
		System.out.println("Deleting tag");
		tagService.delete(tagId);

		return Response.noContent().build();
	}
}
