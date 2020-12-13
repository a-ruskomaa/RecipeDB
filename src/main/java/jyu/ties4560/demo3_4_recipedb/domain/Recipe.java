package jyu.ties4560.demo3_4_recipedb.domain;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;


@JsonRootName("recipe")
@JsonPropertyOrder ({"id","name","author","portions","pricePerPortion","ingredients","tags","links"})
public class Recipe implements DomainObject{
	
	private Long id;
	@NotNull private String name;
	private Profile author;
	@NotNull private Integer portions;
	private List<RecipeIngredient> ingredients = new ArrayList<>();
	private List<Tag> tags = new ArrayList<>();
	private List<Link> links = new ArrayList<>();
	
	public Recipe() {
		// TODO Auto-generated constructor stub
	}
	
	
	public Recipe(String name, Profile author, int portions) {
		this.name = name;
		this.author = author;
		this.portions = portions;
		this.ingredients = new ArrayList<RecipeIngredient>();
		this.tags = new ArrayList<Tag>();
	}
	
	public Recipe(String name, Profile author, int portions, List<RecipeIngredient> ingredients, List<Tag> tags) {
		this.name = name;
		this.author = author;
		this.portions = portions;
		this.ingredients = ingredients;
		this.tags = tags;
	}
	
	public Recipe(Long id, String name, Profile author, int portions, List<RecipeIngredient> ingredients, List<Tag> tags, List<Link> links) {
		this.id = id;
		this.name = name;
		this.author = author;
		this.portions = portions;
		this.ingredients = ingredients;
		this.tags = tags;
		this.links = links;
	}

	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty public Profile getAuthor() {
		if (this.author == null) {
			return new Profile(-1L, "Unknown author", null);
		}
		return author;
	}

	@JsonIgnore public void setAuthor(Profile author) {
		this.author = author;
	}

	public Integer getPortions() {
		return portions;
	}

	public void setPortions(Integer portions) {
		if (portions == null || portions <= 0) {
			this.portions = 1;
		} else {
			this.portions = portions;
		}
	}

	public List<RecipeIngredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<RecipeIngredient> ingredients) {
		this.ingredients = ingredients;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	
	public Double getPricePerPortion() {
		double totalPrice = this.ingredients.stream().reduce(0.0, (acc, el) -> acc + el.getPrice(), Double::sum);
		
		return totalPrice / this.portions;
	}
	

	@JsonProperty public List<Link> getLinks() {
		return links;
	}


	@JsonIgnore public void setLinks(List<Link> links) {
		this.links = links;
	}
	
	public void addLink(String uri, String rel) {
		this.links.add(new Link(uri, rel));
	}


	@Override
	public String toString() {
		return "Recipe [id=" + id + ", name=" + name + ", author=" + author + ", portions=" + portions
				+ ", ingredients=" + ingredients + ", tags=" + tags + "]";
	}
	

}
