package jyu.ties4560.demo3_4_recipedb.resource;

import java.net.URI;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;


import io.jsonwebtoken.Jwts;
import jyu.ties4560.demo3_4_recipedb.security.Keygenerator;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/auth")
public class AuthResource {
	
	@POST
	public Response doAuth(@Context ContainerRequestContext context) {
		SecurityContext securityContext = context.getSecurityContext();
		UriInfo uriInfo = context.getUriInfo();
		
		String accountname = securityContext.getUserPrincipal().getName();
		
		Key key = Keygenerator.getKey();

		URI uri = uriInfo.getAbsolutePathBuilder().build();
		
		String jwtoken = Jwts.builder()
				.setSubject(accountname)
				.setIssuer(uri.toString())
				.setIssuedAt(new Date())
				.setExpiration(Date.from(Instant.now().plus(15, ChronoUnit.MINUTES)))
                .signWith(key)
                .compact();
		
		return Response.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtoken).build();
	}

}
