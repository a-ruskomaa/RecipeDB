package jyu.ties4560.demo3_4_recipedb;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

@ApplicationPath("webapi")
public class RecipeDbAppConfig extends ResourceConfig {
	public RecipeDbAppConfig() {
		System.out.println("Application started!");
		register(RolesAllowedDynamicFeature.class);
	}
}
