package jyu.ties4560.demo3_4_recipedb.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import jyu.ties4560.demo3_4_recipedb.domain.Account;
import jyu.ties4560.demo3_4_recipedb.service.AccountService;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/accounts")
public class AccountResource {
	AccountService accountService = new AccountService();

	@GET
	@RolesAllowed({ "admin" })
	public Response getAllAccounts(@Context UriInfo uriInfo) {
		System.out.println("Getting all users");
		
		List<Account> accounts = accountService.getAllAccounts();

		URI uri = uriInfo.getAbsolutePathBuilder().build();

		return Response.ok(uri).entity(accounts).build();
	}

	@GET
	@RolesAllowed({ "admin" })
	@Path("/{accountId}")
	public Response getOneAccount(@PathParam("accountId") Long accountId, @Context UriInfo uriInfo) {
		System.out.println("Getting a user");

		Account account = accountService.getOne(accountId);

		URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(accountId)).build();

		return Response.ok(uri).entity(account).build();
	}
	
	@POST
	@PermitAll
	public Response createAccount(@Valid Account account, @Context UriInfo uriInfo) {
		account.setRoles(new ArrayList<String>(Arrays.asList("user")));
		
		Account newAccount = accountService.add(account);
		
		String newId = String.valueOf(newAccount.getId());

		URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();

		return Response.created(uri).entity(newAccount).build();
	}
	
}
