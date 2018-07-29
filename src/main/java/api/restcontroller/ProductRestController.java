package api.restcontroller;

import java.util.List;

import api.entitiy.Product;
import api.dto.ProductDto;
import api.entitiy.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import api.service.ProductService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductRestController {

    private final ProductService productService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Product getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createProduct(@RequestBody ProductDto dto) {
        productService.create(dto);
        return "{}";
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String updateNumberOfStock(@PathVariable Long id, @RequestBody Integer num) {
        productService.updateNumberStock(id, num);
        return "{}";
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}