package jyu.ties4560.demo3_4_recipedb.security.filter;

import java.io.IOException;
import java.security.Key;
import java.util.Base64;

import javax.annotation.Priority;
import javax.annotation.security.PermitAll;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.commons.codec.digest.DigestUtils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jyu.ties4560.demo3_4_recipedb.domain.Account;
import jyu.ties4560.demo3_4_recipedb.resource.AuthResource;
import jyu.ties4560.demo3_4_recipedb.security.BasicSecurityContext;
import jyu.ties4560.demo3_4_recipedb.security.Keygenerator;
import jyu.ties4560.demo3_4_recipedb.service.AccountService;

/**
 * A custom security filter, that filters the incoming requests based on the
 * requested resource and authentication information present in the request
 * headers.
 * 
 * If the resource is annotated with @PermitAll, all requests will be let
 * through.
 * 
 * If the request contains a basic authentication header, the request is allowed
 * only if the target endpoint is /auth. If the user can be authenticated, this
 * endpoint will generate and sign a JWT token, that will be passed to the
 * client inside the response header.
 * 
 * If the request contains a bearer authentication header, the header string is
 * then tried to parse into a JWT token. If the token is valid and the user can
 * be verified, the request is allowed.
 * 
 * In all cases, except for resources annotated with @PermitAll, the
 * authenticated user is injected into a SecurityContext. The SecurityContext is
 * then used to enforce role based authorization inside the requested resource.
 * 
 * @author aleks
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {
	private static final String AUTHENTICATION_BASIC_PREFIX = "Basic";
	private static final String AUTHENTICATION_BEARER_PREFIX = "Bearer";
	private static final String AUTHENTICATION_ERROR_UNAUTHORIZED = "{ \"error:\" : \"Not authorized!\"}";
	private static final String AUTHENTICATION_ERROR_FORBIDDEN = "{ \"error:\" : \"Access denied!\"}";
	private static final String AUTHENTICATION_ERROR_INVALID_TOKEN = "{ \"error:\" : \"Invalid token!\"}";

	private AccountService accountService = new AccountService();

	@Context
	private ResourceInfo resourceInfo;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		Response unauthStatus = null;

		// allow all requests if accessing a non-secured endpoint
		if (resourceInfo.getResourceMethod().isAnnotationPresent(PermitAll.class)) {
			return;
		}

		// if accessing /auth, use basic authentication
		if (resourceInfo.getResourceClass().isAssignableFrom(AuthResource.class)) {
			if (authHeader == null || !authHeader.startsWith(AUTHENTICATION_BASIC_PREFIX)) {
				unauthStatus = Response.status(Response.Status.UNAUTHORIZED)
						.header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_BASIC_PREFIX).entity("").build();
			} else {
				// try basic authentication
				doBasicAuth(authHeader, requestContext, unauthStatus);
			}
		} else {
			// if accessing any other protected resource than /auth
			if (authHeader == null || !authHeader.startsWith(AUTHENTICATION_BEARER_PREFIX)) {
				unauthStatus = Response.status(Response.Status.UNAUTHORIZED)
						.header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_BEARER_PREFIX)
						.entity(AUTHENTICATION_ERROR_UNAUTHORIZED).build();
			} else {
				// try jwt authentication
				doJwtAuth(authHeader, requestContext, unauthStatus);
			}
		}

		// this will be non-null if there were any errors during authentication
		if (unauthStatus != null) {
			requestContext.abortWith(unauthStatus);
			return;
		}

	}

	/**
	 * Tries to parse the authentication header for a JWT token and verify it using
	 * the previously generated key. Takes a Response object as a parameter, that is
	 * updated to a non-null value in case of an error.
	 * 
	 * @param authHeader     Authentication header
	 * @param requestContext The request context
	 * @param unauthStatus   The response object, that is to be used if the
	 *                       authentication fails
	 */
	private void doJwtAuth(String authHeader, ContainerRequestContext requestContext, Response unauthStatus) {
		String authToken = authHeader.replaceFirst(AUTHENTICATION_BEARER_PREFIX, "").trim();
		String scheme = requestContext.getUriInfo().getBaseUri().getScheme();
		Key key = Keygenerator.getKey();
		try {
			String user = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken).getBody()
					.getSubject();

			try {
				Account account = accountService.getByName(user);
				requestContext.setSecurityContext(new BasicSecurityContext(account, scheme));

			} catch (NotFoundException e) {
				System.err.println(e.getMessage());
				unauthStatus = Response.status(Response.Status.FORBIDDEN)
						.header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_BEARER_PREFIX)
						.entity(AUTHENTICATION_ERROR_FORBIDDEN).type(MediaType.APPLICATION_JSON).build();
			}
		} catch (JwtException e) {
			unauthStatus = Response.status(Response.Status.UNAUTHORIZED)
					.header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_BEARER_PREFIX)
					.entity(AUTHENTICATION_ERROR_INVALID_TOKEN).type(MediaType.APPLICATION_JSON).build();
		} catch (IllegalArgumentException e) {
			unauthStatus = Response.status(Response.Status.UNAUTHORIZED)
					.header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_BEARER_PREFIX)
					.entity(AUTHENTICATION_ERROR_INVALID_TOKEN).type(MediaType.APPLICATION_JSON).build();
		}
	}

	/**
	 * Tries to parse the authentication header for a basic login credentials. Takes
	 * a Response object as a parameter, that is updated to a non-null value in case
	 * of an error.
	 * 
	 * @param authHeader     Authentication header
	 * @param requestContext The request context
	 * @param unauthStatus   The response object, that is to be used if the
	 *                       authentication fails
	 */
	private void doBasicAuth(String authHeader, ContainerRequestContext requestContext, Response unauthStatus) {
		String authToken = authHeader.replaceFirst(AUTHENTICATION_BASIC_PREFIX, "").trim();
		String scheme = requestContext.getUriInfo().getBaseUri().getScheme();
		String decodedString = new String(Base64.getDecoder().decode(authToken));
		String[] credentials = decodedString.split(":");

		try {
			Account account = checkCredentials(credentials);
			requestContext.setSecurityContext(new BasicSecurityContext(account, scheme));

		} catch (ClientErrorException e) {
			System.err.println(e.getMessage());
			unauthStatus = Response.status(Response.Status.FORBIDDEN)
					.header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_BASIC_PREFIX)
					.entity(AUTHENTICATION_ERROR_FORBIDDEN).type(MediaType.APPLICATION_JSON).build();
		}
	}

	/**
	 * Compares the parsed credentials against the database
	 * 
	 * @param credentials Parsed credentials from authentication header, accountname
	 *                    at index 0 and password at index 1
	 * @return returns the associated account or throws an exception
	 */
	private Account checkCredentials(String[] credentials) {
		System.out.println("checking credentials");
		if (credentials == null || credentials.length < 2) {
			throw new ForbiddenException();
		}

		String user = credentials[0];
		System.out.println(credentials[0] + ":" + credentials[1]);
		Account account = accountService.getByName(user);

		String password = DigestUtils.md5Hex(credentials[1]).toUpperCase();

		if (account.getName().equals(user) && account.getPassword().equals(password)) {
			return account;
		} else {
			throw new ForbiddenException();
		}
	}

}
