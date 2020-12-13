package jyu.ties4560.demo3_4_recipedb.security.filter;

import java.io.IOException;
import java.security.Key;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jyu.ties4560.demo3_4_recipedb.security.Keygenerator;

/**
 * Appends a JWT token to every response if it was present in the original request. This filter could be used
 * to refresh the token if it was near it's expiration
 * 
 * @author aleks
 *
 */
@Provider
public class JwtResponseFilter implements ContainerResponseFilter {
	private static final String AUTHORIZATION_BEARER_PREFIX = "Bearer ";

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		
		if (authHeader != null && authHeader.startsWith(AUTHORIZATION_BEARER_PREFIX)) {
			Key key = Keygenerator.getKey();
			
			String authToken = authHeader.replaceFirst(AUTHORIZATION_BEARER_PREFIX, "");
			
			try {
				Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
				responseContext.getHeaders().add(HttpHeaders.AUTHORIZATION, authHeader);
			} catch (JwtException e) {
			}
		}
		

	}
}