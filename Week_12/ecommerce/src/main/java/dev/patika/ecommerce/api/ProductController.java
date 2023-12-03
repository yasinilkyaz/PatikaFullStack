package dev.patika.ecommerce.api;

import dev.patika.ecommerce.business.abstracts.ICategoryService;
import dev.patika.ecommerce.business.abstracts.IProductService;
import dev.patika.ecommerce.business.abstracts.ISupplierService;
import dev.patika.ecommerce.core.config.modelMapper.IModelMapperService;
import dev.patika.ecommerce.core.result.ResultData;
import dev.patika.ecommerce.core.utilies.ResultHelper;
import dev.patika.ecommerce.dto.request.product.ProductSaveRequest;
import dev.patika.ecommerce.dto.response.category.CategoryResponse;
import dev.patika.ecommerce.dto.response.product.ProductResponse;
import dev.patika.ecommerce.dto.response.suppliers.SupplierResponse;
import dev.patika.ecommerce.entities.Category;
import dev.patika.ecommerce.entities.Product;
import dev.patika.ecommerce.entities.Supplier;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/products")
public class ProductController {

    private final IProductService productService;
    private final IModelMapperService mapperService;

    private final ICategoryService categoryService;

    private final ISupplierService supplierService;

    public ProductController(IProductService productService, IModelMapperService mapperService, ICategoryService categoryService, ISupplierService supplierService) {
        this.productService = productService;
        this.mapperService = mapperService;
        this.categoryService = categoryService;
        this.supplierService = supplierService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<ProductResponse> save(@Valid @RequestBody ProductSaveRequest productSaveRequest) {
        Product saveProduct=this.mapperService.forRequest().map(productSaveRequest,Product.class);

        Category category=this.categoryService.get(productSaveRequest.getCategoryId());
        saveProduct.setCategory(category);

        Supplier supplier=this.supplierService.get(productSaveRequest.getSupplierId());
        saveProduct.setSupplier(supplier);

        this.productService.save(saveProduct);
        return ResultHelper.created(this.mapperService.forResponse().map(saveProduct,ProductResponse.class));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<ProductResponse> get(@PathVariable("id") int id){
        Product product= this.productService.get(id);
        ProductResponse productResponse = this.mapperService.forResponse().map(product,ProductResponse.class);
        return ResultHelper.success(productResponse);
    }
    @GetMapping("/{id}/supplier")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<SupplierResponse> getSupplier(@PathVariable("id") int id){
        Product product= this.productService.get(id);
        return ResultHelper.success(this.mapperService.forResponse().map(product.getSupplier(),SupplierResponse.class));
    }
}
