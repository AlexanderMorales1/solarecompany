/**
 * Servicio de banners de la pantalla principal (home).
 * <p>
 * Relación: {@link com.solare.controller.HomeBannerController} y {@link com.solare.controller.admin.AdminHomeBannerController}.
 * </p>
 */
package com.solare.service;

import com.solare.dto.banner.HomeBannerCreateRequest;
import com.solare.dto.banner.HomeBannerDto;
import com.solare.dto.media.ImageUploadResponse;
import com.solare.exception.ResourceNotFoundException;
import com.solare.model.entity.HomeBannerEntity;
import com.solare.repository.HomeBannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;

/** CRUD de banners con almacenamiento de imagen vía {@link ImageStorageService}. */
@Service
@RequiredArgsConstructor
public class HomeBannerService {

    private final HomeBannerRepository homeBannerRepository;
    private final ImageStorageService imageStorageService;

    /** Banners activos ordenados para el frontend público. */
    @Transactional(readOnly = true)
    public List<HomeBannerDto> listPublic() {
        return homeBannerRepository.findByActiveTrueOrderByDisplayOrderAscCreatedAtAsc()
                .stream()
                .map(this::toDto)
                .toList();
    }

    /** Todos los banners (admin), ordenados por displayOrder y fecha de creación. */
    @Transactional(readOnly = true)
    public List<HomeBannerDto> listAll() {
        return homeBannerRepository.findAll().stream()
                .sorted(Comparator.comparingInt(HomeBannerEntity::getDisplayOrder).thenComparing(HomeBannerEntity::getCreatedAt))
                .map(this::toDto)
                .toList();
    }

    /** Crea banner subiendo imagen obligatoria a la carpeta {@code banners}. */
    @Transactional
    public HomeBannerDto create(HomeBannerCreateRequest request, MultipartFile imageFile) {
        ImageUploadResponse saved = imageStorageService.saveImage(imageFile, "banners");
        HomeBannerEntity entity = HomeBannerEntity.builder()
                .imageUrl(saved.getUrl())
                .title(request != null ? request.getTitle() : null)
                .subtitle(request != null ? request.getSubtitle() : null)
                .active(request == null || request.getActive() == null || request.getActive())
                .displayOrder(request != null && request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .build();
        return toDto(homeBannerRepository.save(entity));
    }

    /** Actualización parcial: imagen opcional; solo campos no nulos del request se aplican. */
    @Transactional
    public HomeBannerDto update(Long id, HomeBannerCreateRequest request, MultipartFile imageFile) {
        HomeBannerEntity entity = homeBannerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Banner no encontrado"));
        if (imageFile != null && !imageFile.isEmpty()) {
            ImageUploadResponse saved = imageStorageService.saveImage(imageFile, "banners");
            entity.setImageUrl(saved.getUrl());
        }
        if (request.getTitle() != null) {
            entity.setTitle(request.getTitle());
        }
        if (request.getSubtitle() != null) {
            entity.setSubtitle(request.getSubtitle());
        }
        if (request.getActive() != null) {
            entity.setActive(request.getActive());
        }
        if (request.getDisplayOrder() != null) {
            entity.setDisplayOrder(request.getDisplayOrder());
        }
        return toDto(homeBannerRepository.save(entity));
    }

    /** Elimina banner por id. */
    @Transactional
    public void delete(Long id) {
        if (!homeBannerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Banner no encontrado");
        }
        homeBannerRepository.deleteById(id);
    }

    /** Convierte entidad de banner a DTO para la API. */
    private HomeBannerDto toDto(HomeBannerEntity b) {
        return HomeBannerDto.builder()
                .id(b.getId())
                .imageUrl(b.getImageUrl())
                .title(b.getTitle())
                .subtitle(b.getSubtitle())
                .active(b.isActive())
                .displayOrder(b.getDisplayOrder())
                .build();
    }
}
