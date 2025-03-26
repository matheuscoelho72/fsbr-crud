package com.example.fsbr.process.service;

import com.example.fsbr.process.config.StorageConfig;
import com.example.fsbr.process.exception.StorageException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class StorageService {

    private final Path rootLocation;

    @Autowired
    public StorageService(StorageConfig properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    public String storeFile(byte[] bytes, String filename) {
        String extension = FilenameUtils.getExtension(filename);
        String newFilename = UUID.randomUUID().toString() + "." + extension;
        try {
            if (filename.isEmpty()) {
                throw new StorageException("Falha ao armazenar arquivo vazio.");
            }
            Path destinationFile = this.rootLocation.resolve(
                            Paths.get(newFilename))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new StorageException(
                        "Não é possível armazenar o arquivo fora do diretório atual.");
            }
            try (InputStream inputStream = new java.io.ByteArrayInputStream(bytes)) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
            return newFilename;
        }
        catch (IOException e) {
            throw new StorageException("Falha ao armazenar arquivo.", e);
        }
    }

    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Não foi carregar os arquivos", e);
        }

    }

    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageException(
                        "Não foi possível ler o arquivo: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageException("Não foi possível ler o arquivo: " + filename, e);
        }
    }

    public void deleteFile(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new StorageException("Não foi possível deletar o arquivo: " + filename, e);
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Não foi possível iniciar o diretório", e);
        }
    }
}
