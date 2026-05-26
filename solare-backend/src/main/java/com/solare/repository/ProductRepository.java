/**
 * Repositorios Spring Data JPA para el catálogo de productos.
 * <p>
 * Combina CRUD estándar con {@link org.springframework.data.jpa.repository.JpaSpecificationExecutor}
 * para filtros dinámicos en listados públicos y de administración.
 */
package com.solare.repository;

import com.solare.model.entity.BrandEntity;
import com.solare.model.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Acceso a datos y consultas especializadas de {@link ProductEntity}.
 */
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {

    /**
     * Productos destacados de una marca concreta.
     *
     * @param brand entidad de marca
     * @return productos con {@link ProductEntity#featured} en {@code true}
     */
    List<ProductEntity> findByBrandAndFeaturedTrue(BrandEntity brand);

    /**
     * Todos los productos marcados como destacados.
     *
     * @return listado de productos featured
     */
    List<ProductEntity> findByFeaturedTrue();

    /**
     * Cuenta productos distintos asociados a una categoría por slug.
     * <p>
     * Usa JOIN sobre la colección {@link ProductEntity#categories} para evitar duplicados
     * cuando un producto tiene varias categorías.
     *
     * @param slug slug de la categoría
     * @return número de productos en esa categoría
     */
    @Query("SELECT COUNT(DISTINCT p.id) FROM ProductEntity p JOIN p.categories c WHERE c.slug = :slug")
    long countByCategorySlug(@Param("slug") String slug);
}
