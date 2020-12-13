package jyu.ties4560.demo3_4_recipedb.service;

import java.util.List;
import java.util.Map;

import javax.ws.rs.NotFoundException;

import jyu.ties4560.demo3_4_recipedb.domain.*;
import jyu.ties4560.demo3_4_recipedb.persistence.Dao;
import jyu.ties4560.demo3_4_recipedb.persistence.GenericDao;
import jyu.ties4560.demo3_4_recipedb.persistence.SimplePersistenceProvider;

/*
 * A service class to get all users, create, delete & modify users etc..
 */
public class ProfileService {
	
	private Dao<Long, Profile> profileDao;
	
	public ProfileService() {
		System.out.println("Creating ProfileService...");
		SimplePersistenceProvider simplePersistenceProvider = SimplePersistenceProvider.getInstance();
		System.out.println("ProfileService: Got an instance of SimplePersistenceProvider");
		Map<Long, Profile> profileDatabase = simplePersistenceProvider.getProfileDatabase();
		System.out.println("ProfileService: Got profileDatabase");
		this.profileDao = new GenericDao<>(profileDatabase);
		System.out.println("ProfileService: Created profileDao");
		
		System.out.println("ProfileService created!");
	}
	
	// Get all users.
	public List<Profile> getAllUsers() {
		return profileDao.getAll();
	}
	
	
	public Profile getOne(Long key) {
		Profile profile = profileDao.getByKey(key);
		if (profile == null) {
			throw new NotFoundException("User not found!");
		}
		return profile;
	}
	
	// Add new user
	public Profile add(Profile profile) {
		return profileDao.add(profile);
	}
	
	
	public Profile update(Profile profile) {
		Profile userToUpdate = profileDao.getByKey(profile.getId());
		if (userToUpdate == null) {
			throw new NotFoundException("User not found!");
		}
		return profileDao.update(profile);
	}
	
	
	public Profile delete(Long key) {
		Profile profile = profileDao.getByKey(key);
		if (profile == null) {
			throw new NotFoundException("User not found!");
		}
		return profileDao.delete(key);
	}
}
