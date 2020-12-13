package jyu.ties4560.demo3_4_recipedb.domain;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@XmlRootElement
@JsonPropertyOrder ({"id","name","portionSize","unit","portionPrice"})
public class Ingredient implements DomainObject {
	private Long id;
	@NotNull
    private String name;
	@NotNull
    private String unit;
	@NotNull
    private Double portionSize;
	@NotNull
    private Double portionPrice;
	private List<Link> links = new ArrayList<>();
    
    public Ingredient() {
	}
    
    
	public Ingredient(Long id, String name, String unit, Double portionSize, Double portionPrice) {
		super();
		this.id = id;
		this.name = name;
		this.unit = unit;
		this.portionSize = portionSize;
		this.portionPrice = portionPrice;
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


	public String getUnit() {
		return unit;
	}


	public void setUnit(String unit) {
		this.unit = unit;
	}


	public Double getPortionSize() {
		return portionSize;
	}


	public void setPortionSize(Double portionSize) {
		this.portionSize = portionSize;
	}


	public Double getPortionPrice() {
		return portionPrice;
	}


	public void setPortionPrice(Double portionPrice) {
		this.portionPrice = portionPrice;
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
		return "Ingredient [id=" + id + ", name=" + name + ", unit=" + unit + ", portionSize=" + portionSize
				+ ", portionPrice=" + portionPrice + "]";
	}


	
	
}

	
	
	
	



