package AndreaBarocchi.CapstoneProject.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AndreaBarocchi.CapstoneProject.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID>{
	  Category findByCategoryName(String categoryName);
}
