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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.commons.io.IOUtils;
import org.dragberry.eshop.dal.entity.File;
import org.dragberry.eshop.dal.repo.FileRepository;
import org.dragberry.eshop.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ImageServiceImpl implements ImageService {
	
	private static final Map<String, String> CONTENT_TYPES = new HashMap<>();
	static {
		CONTENT_TYPES.put("gif", "image/gif");
		CONTENT_TYPES.put("jpeg", "image/jpeg");
		CONTENT_TYPES.put("jpg", "image/jpeg");
		CONTENT_TYPES.put("png", "image/png");
	}
    
    private static final String NO_IMAGE = "/static/images/no-image.png";

    private static final String PRODUCT_IMG_TEMPLATE = "{0}_{1}";

    private static final String IMAGES_GLOB_MAIN = "*_main.{jpg,jpeg,png,gif}";
    
    private static final String MAIN_SUFFIX = "_main.";

    private static final String PRODUCTS_DIR = "products";
    
    private static final String OTHERS_DIR = "others";
    
    @Value("${fs.root}")
    private String fsRoot;
    
    @Value("${fs.files}")
    private String fsFiles;
    
    @Value("${fs.images}")
    private String fsImages;
    
    @Autowired
    private FileRepository fileRepo;
    
    //var/eshop  /files/images  /others/34/img.jpg
    //var/eshop  /files/images  /products/DZ09_1000/DZ09_main.jpg
    @Override
    public File createImage(String imageName, InputStream imgIs) throws IOException {
        return createImage(imageName, imgIs, () -> {
            return  Paths.get(fsFiles, fsImages, OTHERS_DIR, String.valueOf(Math.abs(imageName.hashCode() % 64) + 1));
         });
    }
    
    @Override
    public File createProductImage(Long prodiuctArticleId, String productArticle, String imageName, InputStream imgIs) throws IOException {
        return createImage(imageName, imgIs, () -> {
           return Paths.get(fsFiles, fsImages, PRODUCTS_DIR, getImageName(prodiuctArticleId, productArticle));
        });
    }
    
    private File createImage(String imageName, InputStream imgIS, Supplier<Path> folderResolver) throws IOException {
        Path folder = folderResolver.get();
        Path fsFolder = Paths.get(fsRoot).resolve(folder);
        if (!Files.exists(fsFolder)) {
            Files.createDirectories(fsFolder);
        }
        Path imgPath = fsFolder.resolve(imageName);
        if (!Files.exists(imgPath)) {
            try (OutputStream imgOS = Files.newOutputStream(Files.createFile(imgPath))) {
                IOUtils.copy(imgIS, imgOS);
            }
        }
        String path = folder.resolve(imageName).toString().replace('\\', '/');
        return fileRepo.findByPath(path).orElseGet(() -> createImage(imageName, path));
    }

	private File createImage(String imageName, String imgPath) {
		File image = new File();
        image.setPath(imgPath);
        image.setName(imageName);
        image.setContentType(CONTENT_TYPES.get(imageName.substring(imageName.lastIndexOf('.') + 1).trim().toLowerCase()));
        if (image.getContentType() == null) {
        	System.out.println();
        }
        image = fileRepo.save(image);
		return image;
	}
    
	@Override
    public InputStream getImage(String path) throws IOException {
        Path img = Paths.get(fsRoot, path);
        if (Files.exists(img)) {
            return Files.newInputStream(img);
        }
        return new ClassPathResource(NO_IMAGE).getInputStream();
    }
	
	@Override
	public Optional<File> findImage(String path) {
		return fileRepo.findByPath(path);
	}

    @Override
    public String findMainImage(Long prodiuctArticleId, String productArticle) {
        Path imgDir = Paths.get(fsRoot, PRODUCTS_DIR, getImageName(prodiuctArticleId, productArticle));
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
        Path imgDir = Paths.get(fsRoot, PRODUCTS_DIR, getImageName(prodiuctArticleId, productArticle));
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
        Path img = Paths.get(fsRoot, PRODUCTS_DIR, getImageName(prodiuctArticleId, productArticle), imageName);
        if (Files.exists(img)) {
            return Files.newInputStream(img);
        }
        return new ClassPathResource(NO_IMAGE).getInputStream();
    }
    
    @Override
    public void deleteProductImages(Long prodiuctArticleId, String productArticle) throws IOException {
        Path productDir = Paths.get(fsRoot, PRODUCTS_DIR, getImageName(prodiuctArticleId, productArticle));
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

    private static String getImageName(Long prodiuctArticleId, String productArticle) {
        return MessageFormat.format(PRODUCT_IMG_TEMPLATE, productArticle, prodiuctArticleId.toString());
    }
}
