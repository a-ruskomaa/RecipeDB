package jyu.ties4560.demo3_4_recipedb.domain;

import java.security.Principal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.apache.commons.codec.digest.DigestUtils;


@JsonRootName("account")
@JsonPropertyOrder ({"id","name","author","password","roles","profile"})
public class Account implements Principal, DomainObject {
	private Long id;
	@NotNull
	private String accountname;
	@NotNull
	private String password;
	private List<String> roles;
	private Profile profile;
	
	public Account() {
	}
	

	public Account(Long id, String username, String password, List<String> roles) {
		super();
		this.id = id;
		this.accountname = username;
		this.password = password;
		this.roles = roles;
	}
	
	public Account(Long id, String username, String password, List<String> roles, Profile profile) {
		super();
		this.id = id;
		this.accountname = username;
		this.password = password;
		this.roles = roles;
		this.profile = profile;
	}


	@Override
	public String getName() {
		return this.accountname;
	}
	
	@Override
	public void setName(String name) {
		this.accountname = name;
	}
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	//FOR TESTING PURPOSES ONLY!
	@JsonProperty public String getPassword() {
		return password;
	}

	@JsonProperty public void setPassword(String password) {
		this.password = DigestUtils.md5Hex(password).toUpperCase();
	}

	@JsonProperty public List<String> getRoles() {
		return roles;
	}

	@JsonIgnore public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	@JsonProperty public Profile getProfile() {
		return profile;
	}

	@JsonIgnore public void setProfile(Profile profile) {
		this.profile = profile;
	}
	

	@Override
	public String toString() {
		return "Account [id=" + id + ", accountname=" + accountname + ", password=" + password + ", roles=" + roles
				+ ", profile=" + profile + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountname == null) ? 0 : accountname.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (accountname == null) {
			if (other.accountname != null)
				return false;
		} else if (!accountname.equals(other.accountname))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
}
