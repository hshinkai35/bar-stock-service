package api.service;

import api.entitiy.Product;
import api.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import api.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class ProductService {

    private final ProductRepository repository;

    public List<Product> getProducts() {
        return repository.findAll();
    }

    public Product getProduct(Long id) {
        Optional<Product> optionalProduct = repository.findById(id);
        return optionalProduct.orElse(null);
    }

    public void create(ProductDto dto) {
        repository.create(Product.create(dto.getName(), dto.getNumberOfStock()));
    }

    public void updateNumberStock(long id, int num) {
        repository.updateNumberOfStock(id, num);
    }

    public void deleteProduct(long id) {
        repository.delete(id);
    }


}
