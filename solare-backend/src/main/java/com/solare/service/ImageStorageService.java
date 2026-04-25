package com.solare.service;

import com.solare.config.SolareProperties;
import com.solare.dto.media.ImageUploadResponse;
import com.solare.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageStorageService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp",
            "image/jpg"
    );
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");

    private final SolareProperties solareProperties;

    public ImageUploadResponse saveImage(MultipartFile file, String folder) {
        validateImage(file);
        try {
            String extension = getExtension(file.getOriginalFilename());
            String sanitizedFolder = sanitizeFolder(folder);
            Path baseDir = Paths.get(solareProperties.getStorage().getUploadDir()).toAbsolutePath().normalize();
            Path targetDir = baseDir.resolve(sanitizedFolder).normalize();
            Files.createDirectories(targetDir);

            String filename = UUID.randomUUID() + "." + extension;
            Path targetFile = targetDir.resolve(filename).normalize();
            if (!targetFile.startsWith(baseDir)) {
                throw new BadRequestException("Ruta de almacenamiento inválida");
            }

            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
            String publicUrl = solareProperties.getStorage().getPublicBasePath() + "/" + sanitizedFolder + "/" + filename;
            return ImageUploadResponse.builder()
                    .url(publicUrl)
                    .fileName(filename)
                    .contentType(file.getContentType())
                    .size(file.getSize())
                    .build();
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo guardar la imagen", ex);
        }
    }

    public List<ImageUploadResponse> saveImages(List<MultipartFile> files, String folder) {
        if (files == null || files.isEmpty()) {
            throw new BadRequestException("Debe enviar al menos un archivo");
        }
        return files.stream().map(f -> saveImage(f, folder)).toList();
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("El archivo está vacío");
        }
        String contentType = StringUtils.hasText(file.getContentType()) ? file.getContentType().toLowerCase(Locale.ROOT) : "";
        String extension = getExtension(file.getOriginalFilename());
        if (!ALLOWED_CONTENT_TYPES.contains(contentType) || !ALLOWED_EXTENSIONS.contains(extension)) {
            throw new BadRequestException("Formato de imagen no permitido. Use JPG, JPEG, PNG o WEBP");
        }
    }

    private String getExtension(String filename) {
        if (!StringUtils.hasText(filename) || !filename.contains(".")) {
            throw new BadRequestException("Archivo sin extensión válida");
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
    }

    private String sanitizeFolder(String folder) {
        String value = StringUtils.hasText(folder) ? folder : "misc";
        return value.replaceAll("[^a-zA-Z0-9_-]", "");
    }
}
