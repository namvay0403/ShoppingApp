package com.nam.ShoppingApp.services.admin.category;

import com.nam.ShoppingApp.dto.CategoryDto;
import com.nam.ShoppingApp.entity.Category;

import java.util.List;

public interface AdminCategoryService {

  Category createCategory(CategoryDto categoryDto);

  List<Category> getAllCategories();
}
