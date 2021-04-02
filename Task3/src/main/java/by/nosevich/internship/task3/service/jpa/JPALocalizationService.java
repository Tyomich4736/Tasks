package by.nosevich.internship.task3.service.jpa;

import java.util.List;

import by.nosevich.internship.task3.dto.Book;
import by.nosevich.internship.task3.dto.Language;
import by.nosevich.internship.task3.repository.LocalizationRepository;
import by.nosevich.internship.task3.service.BookService;
import by.nosevich.internship.task3.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.nosevich.internship.task3.dto.Localization;
import by.nosevich.internship.task3.service.LocalizationService;

@Service
@Transactional
public class JPALocalizationService implements LocalizationService{
	@Autowired
	private LocalizationRepository repo;

	/**
	 * @return all localizations from database
	 */
	@Override
	public List<Localization> getAll() {
		return repo.findAll();
	}

	/**
	 * This method saves localization in database
	 * @param localization the language, which should be saved
	 */
	@Override
	public void save(Localization localization) {
		repo.save(localization);
	}

	/**
	 * This method removes localization from database
	 * @param localization the localization, which should be removed
	 */
	@Override
	public void delete(Localization localization) {
		repo.delete(localization);
	}

	/**
	 * This method returns localization by id from database
	 * @param id target id for search
	 * @return localization if it found or null if not
	 */
	@Override
	public Localization getById(int id) {
		return repo.findById(id).orElse(null);
	}

	/**
	 * This method returns localization by book and language from database
	 * @param book target book for search
	 * @param language target language for search
	 * @return localization if it found or null if not
	 */
	@Override
	public Localization getByBookAndLanguage(Book book, Language language) {
		return repo.findByBookAndLanguage(book.getId(), language.getId());
	}

}
