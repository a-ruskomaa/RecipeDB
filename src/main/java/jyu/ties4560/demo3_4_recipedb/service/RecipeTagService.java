package jyu.ties4560.demo3_4_recipedb.service;

import java.util.List;

import javax.ws.rs.NotFoundException;

import jyu.ties4560.demo3_4_recipedb.domain.Recipe;
import jyu.ties4560.demo3_4_recipedb.domain.Tag;

public class RecipeTagService {
    
    public Tag getOne(Recipe recipe, Long tagId) {
		Tag tg = recipe.getTags().stream()
				.filter(tag -> tag.getId() == tagId)
				.findFirst()
				.orElse(null);
		if (tg == null) {
			throw new NotFoundException("Tag not found");
		}
		return tg;
    }


    public Tag update(Recipe recipe, Tag newTag, Long tagId) {
    	List<Tag> tags = recipe.getTags();
    	Tag oldTag = tags.stream()
				.filter(tag -> tag.getId() == tagId)
				.findFirst()
				.orElse(null);
    	if (oldTag == null || newTag.getId() != tagId) {
			throw new NotFoundException("Tag not found");
    	}
    	
		tags.remove(oldTag);
		tags.add(newTag);
		return newTag;
    }

    public Tag delete(Recipe recipe, Long tagId) {
    	List<Tag> tags = recipe.getTags();
		tags.removeIf(tag -> tag.getId() == tagId);
		return null;
    }
}
