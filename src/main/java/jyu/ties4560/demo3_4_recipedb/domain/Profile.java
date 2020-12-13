package jyu.ties4560.demo3_4_recipedb.domain;

import java.util.ArrayList;
import java.util.List;

//import javax.json.bind.annotation.JsonbProperty;
//import javax.json.bind.annotation.JsonbTransient;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@XmlRootElement
@JsonPropertyOrder ({"id","username","links"})
public class Profile implements DomainObject {
	private Long id;
	private String username;
	@JsonIgnore private Account account;
	
	@JsonIgnore private List<Recipe> recipes = new ArrayList<>();
	private List<Link> links = new ArrayList<>();
	
	public Profile() {
		// TODO Auto-generated constructor stub
	}
	
	
	public Profile(Long id, String name, Account account) {
		super();
		this.id = id;
		this.username = name;
		this.account = account;
	}

	@JsonProperty("username") 
	@Override
	public String getName() {
		return username;
	}

	@JsonProperty("username") 
	@Override
	public void setName(String name) {
		this.username = name;
	}

	
	public Account getAccount() {
		return account;
	}
	
	public void setAccount(Account account) throws IllegalAccessException {
		if (this.account != null) {
			throw new IllegalAccessException();
		} else {
			this.account = account;
		}
	}


	public List<Recipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(List<Recipe> recipes) {
		this.recipes = recipes;
	}


	@Override
	public Long getId() {
		return id;
	}


	@Override
	public void setId(Long id) {
		this.id = id;	
	}

	@JsonProperty public List<Link> getLinks() {
		return links;
	}


	@JsonIgnore public void setLinks(List<Link> links) {
		this.links = links;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", id=" + id + "]";
	}


	public void addLink(String uri, String rel) {
		this.links.add(new Link(uri, rel));
		
	}
	
	
}
