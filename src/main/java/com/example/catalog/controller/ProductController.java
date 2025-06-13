package com.example.catalog.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.catalog.model.Product;
import com.example.catalog.repository.ProductRepository;

@Controller
@RequestMapping
public class ProductController {

    private final ProductRepository repo;
    public ProductController(ProductRepository repo) { this.repo = repo; }


    @GetMapping("/products")
    public String list(@RequestParam(required = false) String category, Model model) {
        List<Product> data = (category == null) ? repo.findAll()
                : repo.findByCategoryIgnoreCase(category);
        model.addAttribute("products", data);
        model.addAttribute("category", category);
        return "products";
    }

    @GetMapping("/product/new")
    public String newForm(Model m) { m.addAttribute("product", new Product()); return "product-form"; }

    @GetMapping("/product/{id}")
    public String detail(@PathVariable Long id, Model m) {
        return repo.findById(id).map(p -> { m.addAttribute("product", p); return "product-detail"; })
                .orElse("redirect:/products");
    }

    @GetMapping("/product/{id}/edit")
    public String editForm(@PathVariable Long id, Model m) { return detail(id, m).equals("product-detail") ? "product-form" : "redirect:/products"; }

    @PostMapping("/product")
    public String save(@ModelAttribute Product p) {
        Product saved = repo.save(p);
        return "redirect:/product/" + saved.getId();
    }

    @PostMapping("/product/{id}/delete")
    public String delete(@PathVariable Long id) { repo.deleteById(id); return "redirect:/products"; }

    /* ----- Pure JSON API (same paths you listed) ----- */

    @ResponseBody @GetMapping("/api/products")
    public List<Product> apiList(@RequestParam(required = false) String category) {
        return category == null ? repo.findAll() : repo.findByCategoryIgnoreCase(category);
    }

    @ResponseBody @GetMapping("/api/product/{id}")
    public Product apiGet(@PathVariable Long id) { return repo.findById(id).orElse(null); }

    @ResponseBody @PostMapping("/api/product")
    public Product apiCreate(@RequestBody Product p) { return repo.save(p); }

    @ResponseBody @PutMapping("/api/product/{id}")
    public Product apiUpdate(@PathVariable Long id, @RequestBody Product body) {
        body.setId(id); return repo.save(body);
    }

    @ResponseBody @DeleteMapping("/api/product/{id}")
    public void apiDelete(@PathVariable Long id) { repo.deleteById(id); }
}
