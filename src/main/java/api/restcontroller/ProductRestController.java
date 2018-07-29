package api.restcontroller;

import java.util.List;

import api.entitiy.Product;
import api.dto.ProductDto;
import api.entitiy.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import api.service.ProductService;

@RequiredArgsConstructor
@RestController
public class ProductRestController {

    private final ProductService productService;

    @GetMapping("/products")
    public List<Product> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @PostMapping("/products")
    public void createProduct(@RequestBody ProductDto dto) {
        productService.create(dto);
    }

    @PatchMapping("/products/{id}")
    public void updateNumberOfStock(@PathVariable Long id, @RequestBody Integer num) {
        productService.updateNumberStock(id, num);
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}