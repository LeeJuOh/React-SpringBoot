package com.example.gccoffeeclone.product.service;

import static com.example.gccoffeeclone.utils.MapperUtils.mapList;

import com.example.gccoffeeclone.exception.BadRequestException;
import com.example.gccoffeeclone.product.controller.ProductDto;
import com.example.gccoffeeclone.product.model.Category;
import com.example.gccoffeeclone.product.repository.ProductRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class DefaultProductService implements ProductService {

    private final ProductRepository productRepository;

    public DefaultProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDto> getProductsByCategory(Category category) {
        return mapList(
            productRepository.findByCategory(category),
            ProductDto.class
        );
    }

    @Override
    public ProductDto getProduct(UUID productId) {
        var product = productRepository.findById(productId)
            .orElseThrow(() -> new BadRequestException(
                "Can not found product(%s)".formatted(productId)));
        return ProductDto.from(product);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return mapList(
            productRepository.findAll(),
            ProductDto.class
        );
    }

    @Override
    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        var product = productDto.toEntity();
        productRepository.insert(product);
        var retrievedProduct = productRepository.findById(product.getProductId())
            .orElseThrow(() -> new BadRequestException(
                "Can not found product(%s)".formatted(product.getProductId())));
        return ProductDto.from(retrievedProduct);
    }

    @Override
    @Transactional
    public void updateProduct(UUID productId, ProductDto productDto) {
        var product = productRepository
            .findById(productId)
            .orElseThrow(
                () -> new BadRequestException("Can not found product(%s)".formatted(productId)));
        product.setProductName(productDto.getProductName());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        productRepository.update(product);
    }

    @Override
    @Transactional
    public void deleteById(UUID productId) {
        productRepository.deleteById(productId);
    }

}
