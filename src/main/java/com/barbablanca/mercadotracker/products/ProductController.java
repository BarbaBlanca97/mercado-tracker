package com.barbablanca.mercadotracker.products;

import com.barbablanca.mercadotracker.exceptions.CustomException;
import com.barbablanca.mercadotracker.security.PrincipalCredentials;
import com.barbablanca.mercadotracker.users.UserEntity;
import com.barbablanca.mercadotracker.users.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Objects;
import java.util.Set;

@RestController()
public class ProductController {
    private final ProductRepository productsRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    ProductController(ProductRepository productsRepository, UserRepository userRepository, RestTemplate restTemplate) {
        this.productsRepository = productsRepository;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    @PostMapping("api/users/{userId}/products")
    public ProductEntity createProduct(@Valid @RequestBody PostProduct productInfo) throws Exception {
        Integer userId = ( (PrincipalCredentials) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException());

        String mlItemUrl = "https://api.mercadolibre.com/items?ids="+ productInfo.getId() +
                "&attributes=id,title,thumbnail,price,currency_id,permalink";

        MLProductResponse[] response = Objects.requireNonNull(restTemplate.getForObject(
            mlItemUrl,
            MLProductResponse[].class
        ));

        if (response.length == 0 || response[0].getCode().equals(404)) {
            throw new CustomException(404, "No se puede recuperar el producto "+ productInfo.getId() +" de MercadoLibre");
        }

        ProductEntity product = response[0].getBody().asProductEntity();

        product = productsRepository.save(product);
        user.addProduct(product);
        userRepository.save(user);

        return product;
    }

    @GetMapping("users/{userId}/products")
    public Set<ProductEntity> getAllProductsForUser(@PathVariable Integer userId) {
        return userRepository.findById(userId).orElseThrow(EntityNotFoundException::new).getProducts();
    }

    @DeleteMapping("api/users/{userId}/products/{productId}")
    public Boolean getAllProductsForUser(@PathVariable String productId) {
        Integer userId = ( (PrincipalCredentials) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        ProductEntity product = productsRepository.findById(productId)
                .orElseThrow(EntityNotFoundException::new);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        user.deleteProduct(product);

        if (product.getSubscribers().isEmpty()) {
            productsRepository.delete(product);
        }
        else {
            productsRepository.save(product);
        }

        userRepository.save(user);

        return true;
    }
}
