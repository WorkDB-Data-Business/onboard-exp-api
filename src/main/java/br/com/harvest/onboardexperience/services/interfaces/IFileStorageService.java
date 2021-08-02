package br.com.harvest.onboardexperience.services.interfaces;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IFileStorageService {
	
	public void init();
	
	public void save(MultipartFile file);
	
	public String save(MultipartFile file, String... path);
	
	public Resource load(String filename);
	
	public Resource load(String filename, String... path);
	
	public void deleteAll();
	
	public void deleteAll(String pathDir);
	
	public Stream<Path> loadAll();

}
