/**
 * Servicio de catálogo de productos: búsqueda filtrada, CRUD y acceso a entidad JPA.
 * <p>
 * Relación: usa {@link ProductSpecification}, {@link ProductMapper} y repositorios de marca/categoría/descuento;
 * expuesto por {@link com.solare.controller.ProductController} y {@link com.solare.controller.admin.AdminProductController}.
 * </p>
 */
package com.solare.service;

import com.solare.dto.product.ProductCreateUpdateDto;
import com.solare.dto.product.ProductDto;
import com.solare.exception.BadRequestException;
import com.solare.exception.ResourceNotFoundException;
import com.solare.model.entity.BrandEntity;
import com.solare.model.entity.CategoryEntity;
import com.solare.model.entity.DiscountEntity;
import com.solare.model.entity.ProductEntity;
import com.solare.repository.BrandRepository;
import com.solare.repository.CategoryRepository;
import com.solare.repository.DiscountRepository;
import com.solare.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Orquesta operaciones de producto: listados públicos paginados y mantenimiento administrativo.
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final DiscountRepository discountRepository;
    private final ProductMapper productMapper;

    /**
     * Busca productos aplicando filtros opcionales combinados con especificaciones JPA.
     *
     * @param brand        código de marca (opcional)
     * @param gender       público objetivo (opcional)
     * @param type         tipo de prenda/accesorio (opcional)
     * @param categorySlug slug de categoría (opcional)
     * @param query        texto libre en nombre, descripción, marca o categoría (opcional)
     * @param featured     si solo destacados (opcional)
     * @param pageable     paginación y orden
     * @return página de DTOs de producto
     */
    @Transactional(readOnly = true)
    public Page<ProductDto> search(
            BrandEntity.BrandCode brand,
            ProductEntity.GenderTarget gender,
            ProductEntity.ProductType type,
            String categorySlug,
            String query,
            Boolean featured,
            Pageable pageable) {
        Specification<ProductEntity> spec = Specification.where(ProductSpecification.brandCode(brand))
                .and(ProductSpecification.gender(gender))
                .and(ProductSpecification.productType(type))
                .and(ProductSpecification.categorySlug(categorySlug))
                .and(ProductSpecification.searchText(query))
                .and(ProductSpecification.featured(featured));
        return productRepository.findAll(spec, pageable).map(productMapper::toDto);
    }

    /**
     * Obtiene el detalle público de un producto por identificador.
     *
     * @param id clave primaria del producto
     * @return DTO del producto
     * @throws com.solare.exception.ResourceNotFoundException si no existe
     */
    @Transactional(readOnly = true)
    public ProductDto getById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
    }

    /**
     * Lista productos marcados como destacados para una marca concreta.
     *
     * @param code código de marca del catálogo
     * @return lista de productos destacados
     */
    @Transactional(readOnly = true)
    public List<ProductDto> featuredByBrand(BrandEntity.BrandCode code) {
        BrandEntity brand = brandRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Marca no encontrada"));
        return productRepository.findByBrandAndFeaturedTrue(brand).stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Crea un producto nuevo a partir del DTO de administración.
     *
     * @param dto datos de alta
     * @return producto persistido como DTO
     */
    @Transactional
    public ProductDto create(ProductCreateUpdateDto dto) {
        ProductEntity e = mapNew(dto);
        return productMapper.toDto(productRepository.save(e));
    }

    /**
     * Actualiza un producto existente.
     *
     * @param id  identificador del producto
     * @param dto campos a sobrescribir
     * @return producto actualizado como DTO
     */
    @Transactional
    public ProductDto update(Long id, ProductCreateUpdateDto dto) {
        ProductEntity e = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
        apply(dto, e);
        return productMapper.toDto(productRepository.save(e));
    }

    /**
     * Elimina un producto por identificador.
     *
     * @param id clave primaria
     */
    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado");
        }
        productRepository.deleteById(id);
    }

    /**
     * Carga la entidad JPA (uso interno de otros servicios, p. ej. carrito o pedidos).
     *
     * @param id identificador del producto
     * @return entidad persistida
     */
    @Transactional
    public ProductEntity getEntity(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
    }

    /** Instancia una entidad nueva y aplica el DTO de creación/actualización. */
    private ProductEntity mapNew(ProductCreateUpdateDto dto) {
        ProductEntity e = new ProductEntity();
        apply(dto, e);
        return e;
    }

    /**
     * Mapea campos del DTO a la entidad, resolviendo marca, descuento y categorías por slug.
     */
    private void apply(ProductCreateUpdateDto dto, ProductEntity e) {
        BrandEntity brand = brandRepository.findByCode(dto.getBrandCode())
                .orElseThrow(() -> new BadRequestException("Marca inválida"));
        e.setName(dto.getName());
        e.setBrand(brand);
        e.setProductType(dto.getProductType());
        e.setGender(dto.getGender());
        e.setPriceCop(dto.getPriceCop());
        e.setDescription(dto.getDescription());
        e.setStock(dto.getStock());
        e.setFeatured(dto.isFeatured());
        if (dto.getDiscountId() != null) {
            DiscountEntity d = discountRepository.findById(dto.getDiscountId())
                    .orElseThrow(() -> new BadRequestException("Descuento no encontrado"));
            e.setDiscount(d);
        } else {
            e.setDiscount(null);
        }
        if (dto.getImageUrls() != null) {
            e.setImageUrls(new java.util.ArrayList<>(dto.getImageUrls()));
        } else {
            e.setImageUrls(new java.util.ArrayList<>());
        }
        if (dto.getCategorySlugs() != null && !dto.getCategorySlugs().isEmpty()) {
            Set<CategoryEntity> cats = new HashSet<>();
            for (String slug : dto.getCategorySlugs()) {
                CategoryEntity c = categoryRepository.findBySlug(slug)
                        .orElseThrow(() -> new BadRequestException("Categoría no encontrada: " + slug));
                cats.add(c);
            }
            e.setCategories(cats);
        } else {
            e.setCategories(new HashSet<>());
        }
    }
}
