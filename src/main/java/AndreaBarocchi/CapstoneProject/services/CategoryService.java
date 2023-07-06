package AndreaBarocchi.CapstoneProject.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import AndreaBarocchi.CapstoneProject.entities.Category;
import AndreaBarocchi.CapstoneProject.exceptions.NotFoundException;
import AndreaBarocchi.CapstoneProject.payloads.CategoryPayload;
import AndreaBarocchi.CapstoneProject.repositories.CategoryRepository;


@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepo;

	public Page<Category> findAllCategories(int page, int size, String sortBy) {
		if (size < 0)
			size = 10;
		if (size > 100)
			size = 20;
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
		return categoryRepo.findAll(pageable);
	}

	public Category createCategory(CategoryPayload categoryPayload) {
		Category newCategory = new Category();
		newCategory.setCategoryName(categoryPayload.getCategoryName());
		return categoryRepo.save(newCategory);
	}

	public Category findCategoryById(UUID categoryId) throws NotFoundException {
		return categoryRepo.findById(categoryId)
				.orElseThrow(() -> new NotFoundException("Category not found for ID: " + categoryId));
	}

	public Category updateCategory(UUID categoryId, CategoryPayload categoryPayload) throws NotFoundException {
		Category category = findCategoryById(categoryId);
		category.setCategoryName(categoryPayload.getCategoryName());
		return categoryRepo.save(category);
	}

	public void deleteCategory(UUID categoryId) throws NotFoundException {
		Category category = findCategoryById(categoryId);
		categoryRepo.delete(category);
	}

	public List<Category> findAllCategories() {
		return categoryRepo.findAll();
	}
}
