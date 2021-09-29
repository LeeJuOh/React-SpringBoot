package com.example.gccoffeeclone.product.service;

import static com.example.gccoffeeclone.utils.MapperUtils.mapList;

import com.example.gccoffeeclone.exception.BadRequestException;
import com.example.gccoffeeclone.product.controller.ProductDto;
import com.example.gccoffeeclone.product.model.Category;
import com.example.gccoffeeclone.product.repository.ProductRepository;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
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
                "not found voucherId : " + product.getProductId()));
        return ProductDto.from(retrievedProduct);
    }

}
