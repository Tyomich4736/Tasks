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
	private BookService bookService;
	@Autowired
	private LanguageService languageService;
	
	private int idCount=1;
	
	@Autowired
	private LocalizationRepository repo;
	
	@Override
	public List<Localization> getAll() {
		return repo.findAll();
	}

	@Override
	public void save(Localization entity) {
		repo.insert(idCount++, entity.getValue(), entity.getBook().getId(), entity.getLanguage().getId());
	}

	@Override
	public void delete(Localization entity) {
		repo.delete(entity);
	}

	@Override
	public Localization getById(int id) {
		return repo.getOne(id);
	}

	@Override
	public Localization getByBookAndLanguage(Book book, Language language) {
		return repo.findByBookAndLanguage(book.getId(), language.getId());
	}

	@Override
	public void deleteAllByBook(Book book) {
		repo.deleteAllByBook(book);
	}

	@Override
	public void deleteAllByLanguage(Language language) {
		repo.deleteAllByLanguage(language);
	}

}
