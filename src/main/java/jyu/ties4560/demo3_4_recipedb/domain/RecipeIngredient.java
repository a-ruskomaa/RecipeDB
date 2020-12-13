package jyu.ties4560.demo3_4_recipedb.domain;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@XmlRootElement
@JsonPropertyOrder ({"ingredient","amount","price","links"})
public class RecipeIngredient {
	@NotNull
	private Ingredient ingredient;
	@NotNull
	private Double amount;
	private List<Link> links = new ArrayList<>();
	
	public RecipeIngredient() {
		// TODO Auto-generated constructor stub
	}
	
	public RecipeIngredient(Ingredient ingredient, double amount) {
		this.ingredient = ingredient;
		this.amount = amount;
	}
	

	public RecipeIngredient(Ingredient ingredient, Double amount, List<Link> links) {
		this.ingredient = ingredient;
		this.amount = amount;
		this.links = links;
	}

	public Ingredient getIngredient() {
		return ingredient;
	}
	
	public void setIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
	}
	
	public Double getAmount() {
		return amount;
	}
	
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public Double getPrice() {
		Double price = this.ingredient.getPortionPrice() * this.amount;
		return price;
	}
	

	@JsonProperty public List<Link> getLinks() {
		return links;
	}


	@JsonIgnore public void setLinks(List<Link> links) {
		this.links = links;
	}

	@Override
	public String toString() {
		return "RecipeIngredient [ingredient=" + ingredient + ", amount=" + amount + "]";
	}

	public void addLink(String href, String rel) {
		this.links.add(new Link(href, rel));
		
	}
	
	
	
}
