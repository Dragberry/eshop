package org.dragberry.eshop.service;

public interface TransliteService {
	
	/**
	 * Translite from russian to latin
	 * @return
	 */
	public String translite(String source);
	
	/**
	 * Replace non-alphanumeric characters with dash, make lower case, translite
	 * @return
	 */
	public String transformToId(String source);
	
}
