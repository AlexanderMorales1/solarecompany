package com.solare.config;

import com.solare.model.entity.BrandEntity;
import com.solare.model.entity.CategoryEntity;
import com.solare.model.entity.ProductEntity;
import com.solare.repository.BrandRepository;
import com.solare.repository.CategoryRepository;
import com.solare.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/** Garantiza al menos 30 productos por categoría principal (catálogo demo). Solo inserta lo que falta. */
@Component
@Profile("!test")
@Order(10)
@RequiredArgsConstructor
@Slf4j
public class CategoryCatalogExpansionRunner implements CommandLineRunner {

    static final int TARGET_PER_CATEGORY = 30;

    private static final String[] CATEGORY_SLUGS = {
            "hombre", "mujer", "lentes-casuales", "lentes-deportivos"
    };

    private static final String[] IMAGE_POOL = {
            "https://images.unsplash.com/photo-1511499767150-a48a237f0083?w=800&q=80&auto=format&fit=crop",
            "https://images.unsplash.com/photo-1572635196237-14b3f281503f?w=800&q=80&auto=format&fit=crop",
            "https://images.unsplash.com/photo-1574258495973-f010dfbb5371?w=800&q=80&auto=format&fit=crop",
            "https://images.unsplash.com/photo-1473496169904-658ba7c44d8a?w=800&q=80&auto=format&fit=crop",
            "https://images.unsplash.com/photo-1508296694405-3e7b57befdb8?w=800&q=80&auto=format&fit=crop",
    };

    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void run(String... args) {
        BrandEntity brand = brandRepository.findByCode(BrandEntity.BrandCode.FERRATI).orElse(null);
        if (brand == null) {
            log.warn("CategoryCatalogExpansionRunner: marca Ferrati no encontrada, se omite la expansión.");
            return;
        }
        for (String slug : CATEGORY_SLUGS) {
            CategoryEntity cat = categoryRepository.findBySlug(slug).orElse(null);
            if (cat == null) {
                log.warn("CategoryCatalogExpansionRunner: categoría '{}' no existe.", slug);
                continue;
            }
            long current = productRepository.countByCategorySlug(slug);
            int need = TARGET_PER_CATEGORY - (int) Math.min(current, TARGET_PER_CATEGORY);
            if (need <= 0) {
                continue;
            }
            List<ProductEntity> batch = new ArrayList<>(need);
            for (int i = 0; i < need; i++) {
                int edition = (int) current + i + 1;
                batch.add(buildProduct(slug, cat, brand, edition, i));
            }
            productRepository.saveAll(batch);
            log.info("CategoryCatalogExpansionRunner: +{} productos en categoría '{}' (total objetivo {}).",
                    need, slug, TARGET_PER_CATEGORY);
        }
    }

    private ProductEntity buildProduct(String slug, CategoryEntity cat, BrandEntity brand, int edition, int salt) {
        ProductEntity.ProductType type = ProductEntity.ProductType.CASUAL;
        ProductEntity.GenderTarget gender = ProductEntity.GenderTarget.HOMBRE;
        switch (slug) {
            case "hombre" -> {
                gender = ProductEntity.GenderTarget.HOMBRE;
                type = edition % 2 == 0 ? ProductEntity.ProductType.CASUAL : ProductEntity.ProductType.DEPORTIVO;
            }
            case "mujer" -> {
                gender = ProductEntity.GenderTarget.MUJER;
                type = edition % 2 == 0 ? ProductEntity.ProductType.CASUAL : ProductEntity.ProductType.DEPORTIVO;
            }
            case "lentes-casuales" -> {
                type = ProductEntity.ProductType.CASUAL;
                gender = edition % 2 == 0 ? ProductEntity.GenderTarget.HOMBRE : ProductEntity.GenderTarget.MUJER;
            }
            case "lentes-deportivos" -> {
                type = ProductEntity.ProductType.DEPORTIVO;
                gender = edition % 2 == 0 ? ProductEntity.GenderTarget.HOMBRE : ProductEntity.GenderTarget.MUJER;
            }
            default -> {
            }
        }
        String name = "Solare " + humanLabel(slug) + " · edición " + edition;
        BigDecimal price = BigDecimal.valueOf(180_000L + (edition % 50) * 2_500L);
        String img = IMAGE_POOL[(edition + salt) % IMAGE_POOL.length];
        String desc = "Lente " + humanLabel(slug).toLowerCase()
                + " con protección UV400, materiales ligeros y diseño alineado con el catálogo Solare.";
        return ProductEntity.builder()
                .name(name)
                .brand(brand)
                .productType(type)
                .gender(gender)
                .priceCop(price)
                .description(desc)
                .stock(30 + (edition % 40))
                .featured(edition % 7 == 0)
                .discount(null)
                .imageUrls(new ArrayList<>(List.of(img)))
                .categories(Set.of(cat))
                .build();
    }

    private static String humanLabel(String slug) {
        return switch (slug) {
            case "hombre" -> "Hombre";
            case "mujer" -> "Mujer";
            case "lentes-casuales" -> "Casual";
            case "lentes-deportivos" -> "Deportivo";
            default -> slug;
        };
    }
}
