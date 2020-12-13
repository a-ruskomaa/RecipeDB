package jyu.ties4560.demo3_4_recipedb.resource;

import java.net.URI;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import jyu.ties4560.demo3_4_recipedb.domain.Ingredient;
import jyu.ties4560.demo3_4_recipedb.service.IngredientService;


@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/ingredients")
public class IngredientResource {
    IngredientService ingredientService = new IngredientService();


    @GET
	@PermitAll
    public Response getAllingredients(@QueryParam("searchParam") String searchParam, @Context UriInfo uriInfo) {
        List<Ingredient> ingredients;
        
        if (searchParam != null) {
            System.out.println("Getting filtered ingredients");
            ingredients = ingredientService.getFilteredIngredients(searchParam);
        } else {
            System.out.println("Getting all ingredients");
            ingredients = ingredientService.getAllIngredients();
        }
        
        URI uri = uriInfo.getAbsolutePathBuilder().build();
        
        return Response.ok(uri)
                .entity(ingredients)
                .build();
    }
    
    @GET
	@PermitAll
    @Path("/{ingredientId}")
    public Response getOneIngredient(@PathParam("ingredientId") Long ingredientId, @Context UriInfo uriInfo) {
        System.out.println("Getting an ingredient");
        
        Ingredient ingredient = ingredientService.getOne(ingredientId);
        
        URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(ingredientId)).build();
        
        return Response.ok(uri)
                .entity(ingredient)
                .build();
    }
    
    @POST
	@RolesAllowed({ "user" })
    public Response addIngredient(@Valid Ingredient ingredient, @Context UriInfo uriInfo) {
        System.out.println("Adding an ingredient");
        
        Ingredient newIngredient = ingredientService.add(ingredient);
        String newId = String.valueOf(newIngredient.getId());
        
        URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
        
        return Response.created(uri)
                .entity(newIngredient)
                .build();
    }
    
    @PUT
	@RolesAllowed({ "admin" })
    @Path("/{ingredientId}")
    public Response updateIngredient(@PathParam("ingredientId") Long ingredientId, Ingredient ingredient, @Context UriInfo uriInfo) {
        System.out.println("Updating an ingredient");
        ingredient.setId(ingredientId);
        Ingredient updatedIngredient = ingredientService.update(ingredient);
        
        URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(ingredientId)).build();
        
        return Response.ok(uri)
                .entity(updatedIngredient)
                .build();
    }
    
    @DELETE
	@RolesAllowed({ "admin" })
    @Path("/{ingredientId}")
    public Response deleteRecipe(@PathParam("ingredientId") Long ingredientId) {
        System.out.println("Deleting an ingredient");
        ingredientService.delete(ingredientId);
        
        return Response.noContent().build();
    }  
}