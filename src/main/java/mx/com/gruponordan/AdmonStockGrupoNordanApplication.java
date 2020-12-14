package mx.com.gruponordan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import mx.com.gruponordan.file.service.FilesStorageServiceImpl;

@SpringBootApplication
public class AdmonStockGrupoNordanApplication implements CommandLineRunner{

	@Value("${file.upload-dir}")
    private String uploadPath;
	
	@Autowired
	FilesStorageServiceImpl storageService;
	
	public static void main(String[] args) {
		SpringApplication.run(AdmonStockGrupoNordanApplication.class, args);
	}
	
	public void run(String... arg) {
		storageService.init(uploadPath);
	}

}
