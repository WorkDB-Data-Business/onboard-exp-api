package br.com.harvest.onboardexperience.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import br.com.harvest.onboardexperience.services.interfaces.IFileStorageService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileStorageService implements IFileStorageService {

	private final Path root = Paths.get("resources");

	@Override
	public void init() {
		try {
			if(!Files.exists(root)) {
				Files.createDirectory(root);
				log.info("The root folder was created successful.");
			}
		} catch (IOException e) {
			log.error("Could not initialize folder for upload!", e);
			throw new RuntimeException("Could not initialize folder for upload!", e.getCause());
		}

	}

	@Override
	public void save(MultipartFile file) {
		try {
			Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
			log.info("File " + file.getOriginalFilename() + " saved successful.");
		} catch (Exception e) {
			log.error("Could not store the file. Error: " + e.getMessage(), e.getCause());
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage(), e.getCause());
		}
	}


	@Override
	public String save(MultipartFile file, String... path) {
		Path filePath = this.root;

		for(String pathParam : path) {
			filePath = filePath.resolve(pathParam);
		}

		File dirPath = filePath.toFile();
		filePath = filePath.resolve(file.getOriginalFilename());

		try {

			if (!dirPath.exists()) {
				dirPath.mkdirs();
			}

			Files.copy(file.getInputStream(), filePath);
			log.info("File " + file.getOriginalFilename() + " saved successful.");
			return filePath.toString();
		} catch (Exception e) {
			throw new RuntimeException("Could not store the file. Error:  " + e.getMessage(), e.getCause());
		}	
	}

	@Override
	public Resource load(String filename) {
		try {
			Path file = Path.of(filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage(), e.getCause());
		}
	}

	@Override
	public Resource load(String filename, String... path) {
		
		Path file = this.root;

		for(String pathParam : path) {
			file = file.resolve(pathParam);
		}
		
		file = file.resolve(filename);
		
		try {
			
			Resource resource = new UrlResource(file.toUri());

			if (resource != null) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage(), e.getCause());
		}
	}

	@Override
	public void deleteAll() {
		try {
			FileSystemUtils.deleteRecursively(root.toFile());
			log.info("All folders was deleted successful.");
		} catch (Exception e ) {
			log.error("Error while deleting the specified folder: " + root.getFileName(), e);
		}
	}

	@Override
	public void deleteAll(String pathDir) {
		File path = root.resolve(pathDir).toFile();  
		try {
			FileSystemUtils.deleteRecursively(path);
		} catch (Exception e) {
			log.error("Error while deleting the specified folder: " + path.getName(), e);
		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
		} catch (IOException e) {
			throw new RuntimeException("Could not load the files!");
		}
	}

}

