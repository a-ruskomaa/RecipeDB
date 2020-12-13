package jyu.ties4560.demo3_4_recipedb.security;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import jyu.ties4560.demo3_4_recipedb.domain.Account;
import jyu.ties4560.demo3_4_recipedb.domain.Profile;

public class BasicSecurityContext implements SecurityContext {
	private Account account;
	private String scheme;
	
	
	public BasicSecurityContext(Account account, String scheme) {
		super();
		this.account = account;
		this.scheme = scheme;
	}

	@Override
	public Principal getUserPrincipal() {
		return this.account;
	}

	@Override
	public boolean isUserInRole(String role) {
		return this.account.getRoles().contains(role);
	}
	
	public boolean isUserEqualTo(Account other) {
		return other.equals(this.account);
	}
	
	public Profile getCurrentProfile() {
		return this.account.getProfile();
	}

	@Override
	public boolean isSecure() {
		return "https".equals(scheme);
	}

	@Override
	public String getAuthenticationScheme() {
		return this.scheme;
	}

}
