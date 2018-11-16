package org.dragberry.eshop.files.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.dragberry.eshop.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ImageServiceImpl implements ImageService {
    
    private static final String NO_IMAGE = "/static/images/no-image.png";

    private static final String PRODUCT_IMG_TEMPLATE = "{0}_{1}";

    private static final String IMAGES_GLOB_MAIN = "*_main.{jpg,jpeg,png,gif}";
    
    private static final String MAIN_SUFFIX = "_main.";

    private static final String PRODUCTS_DIR = "products";
    
    private static final String OTHERS_DIR = "others";
    
    @Value("${db.images}")
    private String dbImages;
    
    @Value("${url.images}")
    private String urlImages;

    @Override
    public String findMainImage(Long prodiuctArticleId, String productArticle) {
        Path imgDir = Paths.get(dbImages, PRODUCTS_DIR, getImageName(prodiuctArticleId, productArticle));
        if (Files.exists(imgDir) && Files.isDirectory(imgDir)) {
            try (DirectoryStream<Path> imgStream = Files.newDirectoryStream(imgDir, IMAGES_GLOB_MAIN)) {
                Iterator<Path> iter;
                if ((iter = imgStream.iterator()).hasNext()) {
                    return iter.next().getFileName().toString();
                }
            } catch (IOException exc) {
                log.error(MessageFormat.format("An error has occurred whle reading images for product article: {0}", prodiuctArticleId), exc);
            }
        }
        return null;
    }
    
    @Override
    public List<String> findProductImages(Long prodiuctArticleId, String productArticle) {
        Path imgDir = Paths.get(dbImages, PRODUCTS_DIR, getImageName(prodiuctArticleId, productArticle));
        if (Files.exists(imgDir) && Files.isDirectory(imgDir)) {
            try (DirectoryStream<Path> imgStream = Files.newDirectoryStream(imgDir, path -> !path.getFileName().toString().contains(MAIN_SUFFIX))) {
                List<String> imgs = new ArrayList<>();
                Iterator<Path> iter = imgStream.iterator();
                while (iter.hasNext()) {
                    imgs.add(iter.next().getFileName().toString());
                }
                return imgs;
            } catch (IOException exc) {
                log.error(MessageFormat.format("An error has occurred whle reading images for product article: {0}", prodiuctArticleId), exc);
            }
        }
        return Collections.emptyList();
    }
    
    @Override
    public InputStream getProductImage(Long prodiuctArticleId, String productArticle, String imageName) throws IOException {
        Path img = Paths.get(dbImages, PRODUCTS_DIR, getImageName(prodiuctArticleId, productArticle), imageName);
        if (Files.exists(img)) {
            return Files.newInputStream(img);
        }
        return new ClassPathResource(NO_IMAGE).getInputStream();
    }
    @Override
    public void createProductImage(Long prodiuctArticleId, String productArticle, String imageName, InputStream imgIs) throws IOException {
        Path productDir = Paths.get(dbImages, PRODUCTS_DIR, getImageName(prodiuctArticleId, productArticle));
        if (!Files.exists(productDir)) {
            Files.createDirectories(productDir);
        }
        Path imgPath = productDir.resolve(imageName);
        if (!Files.exists(imgPath)) {
            try (OutputStream imgOS = Files.newOutputStream(Files.createFile(imgPath))) {
                IOUtils.copy(imgIs, imgOS);
            }
        }
    }
    
    @Override
    public void deleteProductImages(Long prodiuctArticleId, String productArticle) throws IOException {
        Path productDir = Paths.get(dbImages, PRODUCTS_DIR, getImageName(prodiuctArticleId, productArticle));
        if (Files.exists(productDir)) {
            Files.walk(productDir)
                .sorted(Comparator.reverseOrder())
                .forEach(file -> {
                    try {
                        Files.delete(file);
                    } catch (IOException exc) {
                        log.error(MessageFormat.format("An error occured during deleting image for {0}:{1}", prodiuctArticleId, productArticle), exc);
                        throw new RuntimeException(exc);
                    }
                });
        }
    }

    @Override
    public String createImage(String imageName, InputStream imgIS) throws IOException {
        Path folder = Paths.get(dbImages, OTHERS_DIR, String.valueOf(Math.abs(imageName.hashCode() % 64) + 1));
        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
        }
        Path imgPath = folder.resolve(imageName);
        if (!Files.exists(imgPath)) {
            try (OutputStream imgOS = Files.newOutputStream(Files.createFile(imgPath))) {
                IOUtils.copy(imgIS, imgOS);
            }
        }
        return Stream.of(
                urlImages,
                folder.getFileName().toString(),
                imageName).collect(Collectors.joining("/"));
    }
    
@Override
    public InputStream getImage(String folder, String imageName) throws IOException {
        Path img = Paths.get(dbImages, OTHERS_DIR, folder, imageName);
        if (Files.exists(img)) {
            return Files.newInputStream(img);
        }
        return new ClassPathResource(NO_IMAGE).getInputStream();
    }
    
    private static String getImageName(Long prodiuctArticleId, String productArticle) {
        return MessageFormat.format(PRODUCT_IMG_TEMPLATE, productArticle, prodiuctArticleId.toString());
    }
}
