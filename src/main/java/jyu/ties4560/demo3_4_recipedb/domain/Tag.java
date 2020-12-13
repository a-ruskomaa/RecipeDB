package jyu.ties4560.demo3_4_recipedb.domain;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Tag implements DomainObject {
	
	private Long id;
	@NotNull
	private String name;
	
	public Tag () {
		
	}
	
	public Tag(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
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
	
	@Override
	public String toString() {
		return "Tag [id=" + id + ", name=" + name + "]";
	}
}
