package dev.patika.ecommerce.business.abstracts;

import dev.patika.ecommerce.entities.Category;
import org.springframework.data.domain.Page;

public interface ICategoryService {
    Category save(Category category);
    Category get(Long id);
    Page<Category> cursor(int page,int pageSize);

}
