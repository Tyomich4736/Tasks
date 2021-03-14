package by.nosevich.internship.task3.repository;

import by.nosevich.internship.task3.dto.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer>{
	Language findByAbbreviation(String abbreviation);
}
