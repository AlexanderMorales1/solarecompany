package com.solare.config;

import com.solare.model.entity.BrandEntity;
import com.solare.model.entity.CategoryEntity;
import com.solare.model.entity.DiscountEntity;
import com.solare.model.entity.ProductEntity;
import com.solare.model.entity.RoleEntity;
import com.solare.model.entity.UserEntity;
import com.solare.repository.BrandRepository;
import com.solare.repository.CategoryRepository;
import com.solare.repository.DiscountRepository;
import com.solare.repository.ProductRepository;
import com.solare.repository.RoleRepository;
import com.solare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Profile("!test")
@Order(1)
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (roleRepository.count() > 0) {
            return;
        }
        RoleEntity roleUser = roleRepository.save(RoleEntity.builder().name(RoleEntity.RoleName.ROLE_USER).build());
        RoleEntity roleAdmin = roleRepository.save(RoleEntity.builder().name(RoleEntity.RoleName.ROLE_ADMIN).build());

        Set<RoleEntity> adminRoles = new HashSet<>();
        adminRoles.add(roleAdmin);
        adminRoles.add(roleUser);
        userRepository.save(UserEntity.builder()
                .email("admin@solare.co")
                .password(passwordEncoder.encode("Admin123!"))
                .firstName("Admin")
                .lastName("Solare")
                .provider("LOCAL")
                .enabled(true)
                .roles(adminRoles)
                .build());

        BrandEntity ferrati = brandRepository.save(BrandEntity.builder()
                .code(BrandEntity.BrandCode.FERRATI)
                .displayName("Ferrati")
                .build());
        BrandEntity rayban = brandRepository.save(BrandEntity.builder()
                .code(BrandEntity.BrandCode.RAYBAN)
                .displayName("Ray-Ban")
                .build());

        CategoryEntity catHombre = categoryRepository.save(CategoryEntity.builder().name("Hombre").slug("hombre").build());
        CategoryEntity catMujer = categoryRepository.save(CategoryEntity.builder().name("Mujer").slug("mujer").build());
        CategoryEntity catCasual = categoryRepository.save(CategoryEntity.builder()
                .name("Lentes casuales").slug("lentes-casuales").build());
        CategoryEntity catDeportivo = categoryRepository.save(CategoryEntity.builder()
                .name("Lentes deportivos").slug("lentes-deportivos").build());

        DiscountEntity bogoRayban = discountRepository.save(DiscountEntity.builder()
                .code("RAYBAN_2X1")
                .name("Ray-Ban 2x1")
                .type(DiscountEntity.DiscountType.BOGO_TWO_FOR_ONE)
                .valuePercent(null)
                .active(true)
                .startsAt(null)
                .endsAt(null)
                .build());

        String imgSport = "https://images.unsplash.com/photo-1511499767150-a48a237f0083?w=1200&q=80";
        String imgCasual = "https://images.unsplash.com/photo-1572635196237-14b3f281503f?w=1200&q=80";
        String imgRay = "https://images.unsplash.com/photo-1574258495973-f010dfbb5371?w=1200&q=80";

        List<ProductEntity> products = new ArrayList<>();

        products.add(product(
                "Ferrati Velocity Sport",
                ferrati, ProductEntity.ProductType.DEPORTIVO, ProductEntity.GenderTarget.HOMBRE,
                new BigDecimal("290000.00"),
                "Lentes deportivos Ferrati con protección UV400, diseño aerodinámico usado por atletas de alto rendimiento.",
                40, true, null,
                List.of(imgSport, imgCasual),
                Set.of(catHombre, catDeportivo)));

        products.add(product(
                "Ferrati Urban Casual",
                ferrati, ProductEntity.ProductType.CASUAL, ProductEntity.GenderTarget.HOMBRE,
                new BigDecimal("270000.00"),
                "Estilo casual Ferrati para ciudad, montura ligera y lentes polarizados.",
                55, true, null,
                List.of(imgCasual),
                Set.of(catHombre, catCasual)));

        products.add(product(
                "Ferrati Eclipse Mujer",
                ferrati, ProductEntity.ProductType.CASUAL, ProductEntity.GenderTarget.MUJER,
                new BigDecimal("270000.00"),
                "Diseño elegante Ferrati para mujer, tonos versátiles y máxima comodidad.",
                48, true, null,
                List.of(imgCasual),
                Set.of(catMujer, catCasual)));

        products.add(product(
                "Ferrati Trail Pro",
                ferrati, ProductEntity.ProductType.DEPORTIVO, ProductEntity.GenderTarget.MUJER,
                new BigDecimal("290000.00"),
                "Lentes deportivos Ferrati con agarre seguro para running y outdoor.",
                35, true, null,
                List.of(imgSport),
                Set.of(catMujer, catDeportivo)));

        products.add(product(
                "Ray-Ban Aviator Classic",
                rayban, ProductEntity.ProductType.CASUAL, ProductEntity.GenderTarget.HOMBRE,
                new BigDecimal("760000.00"),
                "Icono atemporal Ray-Ban, cristales G-15 y construcción metálica.",
                25, true, bogoRayban,
                List.of(imgRay),
                Set.of(catHombre, catCasual)));

        products.add(product(
                "Ray-Ban Wayfarer",
                rayban, ProductEntity.ProductType.CASUAL, ProductEntity.GenderTarget.MUJER,
                new BigDecimal("720000.00"),
                "Perfil clásico Wayfarer con estilo contemporáneo.",
                30, true, bogoRayban,
                List.of(imgRay),
                Set.of(catMujer, catCasual)));

        products.add(product(
                "Ray-Ban Clubmaster",
                rayban, ProductEntity.ProductType.CASUAL, ProductEntity.GenderTarget.HOMBRE,
                new BigDecimal("820000.00"),
                "Combinación acetato y metal, look retro premium.",
                18, true, bogoRayban,
                List.of(imgRay),
                Set.of(catHombre, catCasual)));

        products.add(product(
                "Ray-Ban Round Metal",
                rayban, ProductEntity.ProductType.CASUAL, ProductEntity.GenderTarget.MUJER,
                new BigDecimal("780000.00"),
                "Forma redonda vintage con lentes de calidad óptica.",
                22, false, bogoRayban,
                List.of(imgRay),
                Set.of(catMujer, catCasual)));

        productRepository.saveAll(products);
    }

    private ProductEntity product(
            String name,
            BrandEntity brand,
            ProductEntity.ProductType type,
            ProductEntity.GenderTarget gender,
            BigDecimal price,
            String desc,
            int stock,
            boolean featured,
            DiscountEntity discount,
            List<String> images,
            Set<CategoryEntity> categories) {
        ProductEntity p = ProductEntity.builder()
                .name(name)
                .brand(brand)
                .productType(type)
                .gender(gender)
                .priceCop(price)
                .description(desc)
                .stock(stock)
                .featured(featured)
                .discount(discount)
                .imageUrls(new ArrayList<>(images))
                .categories(new HashSet<>(categories))
                .build();
        return p;
    }
}
