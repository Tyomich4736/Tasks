package by.nosevich.internship.task3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import by.nosevich.internship.task3.dto.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer>{
	Language findByAbbreviation(String abbreviation);
}
