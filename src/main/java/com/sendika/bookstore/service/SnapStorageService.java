package com.sendika.bookstore.service;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class SnapStorageService {

    private final Path uploadRoot;

    public SnapStorageService(@Value("${snap.upload-dir}") String uploadDir) throws IOException {
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.uploadRoot);
    }

    public String savePng(MultipartFile pngFile) throws IOException {
        if (pngFile.isEmpty()) throw new IOException("Boş dosya");
        // güvenli dosya adı
        String base = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String name = base + "_" + UUID.randomUUID() + ".png";
        Path target = uploadRoot.resolve(StringUtils.cleanPath(name));
        Files.copy(pngFile.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return name; // sadece dosya adı
    }

    public Path resolve(String filename) {
        return uploadRoot.resolve(filename).normalize();
    }
}
