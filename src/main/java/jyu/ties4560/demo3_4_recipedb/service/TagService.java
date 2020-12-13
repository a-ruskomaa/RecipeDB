package jyu.ties4560.demo3_4_recipedb.service;

import java.util.List;
import java.util.Map;
import javax.ws.rs.NotFoundException;

import jyu.ties4560.demo3_4_recipedb.domain.Tag;
import jyu.ties4560.demo3_4_recipedb.persistence.Dao;
import jyu.ties4560.demo3_4_recipedb.persistence.GenericDao;
import jyu.ties4560.demo3_4_recipedb.persistence.SimplePersistenceProvider;

public class TagService {
	private Dao<Long, Tag> tagDao;

	public TagService() {
//		System.out.println("Creating TagService...");
		SimplePersistenceProvider simplePersistenceProvider = SimplePersistenceProvider.getInstance();
//		System.out.println("TagService: Got an instance of SimplePersistenceProvider");
		Map<Long, Tag> tagDatabase = simplePersistenceProvider.getTagDatabase();
//		System.out.println("TagService: Got tagDatabase");
		this.tagDao = new GenericDao<>(tagDatabase);
//		System.out.println("TagService: Created tagDao");
//		System.out.println("TagService created!");
	}

	public List<Tag> getAllTags() {
		return tagDao.getAll();
	}

	public Tag getOne(Long key) {
		Tag tag = tagDao.getByKey(key);
		if (tag == null) {
			throw new NotFoundException("Tag not found!");
		}
		return tag;
	}

	public Tag add(Tag tag) {
		return tagDao.add(tag);
	}

	public Tag update(Tag tag) {
		Tag tagToUpdate = tagDao.getByKey(tag.getId());
		if (tagToUpdate == null) {
			throw new NotFoundException("Tag not found!");
		}
		return tagDao.update(tag);
	}

	public Tag delete(Long key) {
		Tag tag = tagDao.getByKey(key);
		if (tag == null) {
			throw new NotFoundException("Tag not found!");
		}
		return tagDao.delete(key);
	}
}