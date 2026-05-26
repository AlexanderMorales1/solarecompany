/**
 * Servicio de promociones y descuentos (CRUD admin y listado público activo).
 * <p>
 * Relación: {@link com.solare.controller.admin.AdminDiscountController} y {@link com.solare.controller.DiscountPublicController}.
 * </p>
 */
package com.solare.service;

import com.solare.dto.discount.DiscountDto;
import com.solare.dto.discount.DiscountUpsertDto;
import com.solare.exception.ResourceNotFoundException;
import com.solare.model.entity.DiscountEntity;
import com.solare.repository.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/** Persistencia y mapeo de entidades {@link com.solare.model.entity.DiscountEntity}. */
@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepository discountRepository;

    /** Promociones con bandera activa (vista tienda). */
    @Transactional(readOnly = true)
    public List<DiscountDto> listActive() {
        return discountRepository.findByActiveTrue().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /** Todas las promociones (panel admin). */
    @Transactional(readOnly = true)
    public List<DiscountDto> listAll() {
        return discountRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /** Alta de promoción. */
    @Transactional
    public DiscountDto create(DiscountUpsertDto dto) {
        DiscountEntity e = map(dto);
        return toDto(discountRepository.save(e));
    }

    /** Actualización de promoción existente. */
    @Transactional
    public DiscountDto update(Long id, DiscountUpsertDto dto) {
        DiscountEntity e = discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promoción no encontrada"));
        apply(dto, e);
        return toDto(discountRepository.save(e));
    }

    /** Borrado físico de promoción. */
    @Transactional
    public void delete(Long id) {
        if (!discountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Promoción no encontrada");
        }
        discountRepository.deleteById(id);
    }

    /** Crea entidad nueva a partir del DTO de alta/actualización. */
    private DiscountEntity map(DiscountUpsertDto dto) {
        DiscountEntity e = new DiscountEntity();
        apply(dto, e);
        return e;
    }

    /** Copia campos editables del DTO a la entidad. */
    private void apply(DiscountUpsertDto dto, DiscountEntity e) {
        e.setCode(dto.getCode());
        e.setName(dto.getName());
        e.setType(dto.getType());
        e.setValuePercent(dto.getValuePercent());
        e.setActive(dto.isActive());
        e.setStartsAt(dto.getStartsAt());
        e.setEndsAt(dto.getEndsAt());
    }

    /** Mapeo entidad → DTO de respuesta. */
    private DiscountDto toDto(DiscountEntity e) {
        return DiscountDto.builder()
                .id(e.getId())
                .code(e.getCode())
                .name(e.getName())
                .type(e.getType())
                .valuePercent(e.getValuePercent())
                .active(e.isActive())
                .startsAt(e.getStartsAt())
                .endsAt(e.getEndsAt())
                .build();
    }
}
