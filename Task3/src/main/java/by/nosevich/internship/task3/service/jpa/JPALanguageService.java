package by.nosevich.internship.task3.service.jpa;

import java.util.List;

import by.nosevich.internship.task3.dto.Language;
import by.nosevich.internship.task3.repository.LanguageRepository;
import by.nosevich.internship.task3.service.LocalizationService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import by.nosevich.internship.task3.service.LanguageService;

@Service
public class JPALanguageService implements LanguageService, InitializingBean {

	@Autowired
	private LanguageRepository repo;
	@Autowired
	private LocalizationService localizationService;

	/**
	 * @return all languages from database
	 */
	@Override
	public List<Language> getAll() {
		return repo.findAll();
	}

	/**
	 * This method saves language in database
	 * @param language the language, which should be saved
	 */
	@Override
	public void save(Language language) {
		repo.save(language);
	}

	/**
	 * This method removes language from database
	 * @param language the language, which should be removed
	 */
	@Override
	public void delete(Language language) {
		//localizationService.deleteAllByLanguage(entity);
		repo.delete(language);
	}

	/**
	 * This method returns language by id from database
	 * @param id target id for search
	 * @return language if it found or null if not
	 */
	@Override
	public Language getById(int id) {
		return repo.findById(id).orElse(null);
	}

	/**
	 * This method returns language by abbreviation from database
	 * @param abbreviation target abbreviation for search
	 * @return language if it found or null if not
	 */
	@Override
	public Language getByAbbreviation(String abbreviation) {
		return repo.findByAbbreviation(abbreviation);
	}

	/**
	 * This method add default languages in database if it's empty
	 */
	@Override
	public void afterPropertiesSet(){
		if (getAll().isEmpty()){
			save(new Language(null, "PL", null));
			save(new Language(null, "RU", null));
			save(new Language(null, "BY", null));
		}
	}
}
