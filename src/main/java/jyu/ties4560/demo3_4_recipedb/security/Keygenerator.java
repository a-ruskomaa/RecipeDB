package jyu.ties4560.demo3_4_recipedb.security;

import java.security.Key;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * A stupidly simple "keygenerator". Generates a random key to be used in signing the JWT tokens and verifying the signature.
 * 
 * @author aleks
 *
 */
public class Keygenerator {
	private static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	public static Key getKey() {
		return key;
	}
}
