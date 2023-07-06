package AndreaBarocchi.CapstoneProject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import AndreaBarocchi.CapstoneProject.entities.Category;
import AndreaBarocchi.CapstoneProject.repositories.CategoryRepository;

@Component
public class DataEntryRunner implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        // Genera 10 categorie per giochi da tavola con nomi in inglese
        String[] categoryNames = {"Strategy", "Party", "Cooperative", "Deck-building", "Tile Placement", "Card Game",
                "Dexterity", "Role-playing", "Adventure", "Family"};

        for (String categoryName : categoryNames) {
            if (categoryRepository.findByCategoryName(categoryName) == null) {
                Category category = new Category();
                category.setCategoryName(categoryName);
                categoryRepository.save(category);
                System.out.println("Categoria creata: " + categoryName);
            } else {
                System.out.println("Categoria già presente nel database: " + categoryName);
            }
        }
    }

}
