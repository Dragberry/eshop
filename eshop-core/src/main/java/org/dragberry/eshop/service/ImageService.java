package org.dragberry.eshop.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.dragberry.eshop.dal.entity.File;

public interface ImageService {

    /**
     * Return main image name for product if exists
     * @param prodiuctArticleId
     * @param productArticle
     * @return
     */
    String findMainImage(Long prodiuctArticleId, String productArticle);
    
    /**
     * Finds all product images except main
     * @param entityKey
     * @param article
     * @return
     */
    List<String> findProductImages(Long entityKey, String article);

    /**
     * Get the product image if exists
     * @param productKey
     * @param productArticle
     * @param imageName 
     * @return
     * @throws IOException 
     */
    InputStream getProductImage(Long productKey, String article, String imageName) throws IOException;

    /**
     * Creates an product image
     * @param prodiuctArticleId
     * @param productArticle
     * @return 
     * @throws IOException
     */
    File createProductImage(Long prodiuctArticleId, String productArticle, String imageName, InputStream imgIs) throws IOException;

    /**
     * Deletes all product images
     * @param prodiuctArticleId
     * @param productArticle
     * @throws IOException
     */
    void deleteProductImages(Long prodiuctArticleId, String productArticle) throws IOException;

    
    /**
     * Create an image
     * @param imageName
     * @param imgIS
     * @return an image link
     * @throws IOException 
     */
    File createImage(String imageName, InputStream imgIS) throws IOException;

    /**
     * Get an image
     * @param folder
     * @param imageName
     * @return
     * @throws IOException 
     */
    InputStream getImage(String path) throws IOException;


	Optional<File> findImage(String path);

}
