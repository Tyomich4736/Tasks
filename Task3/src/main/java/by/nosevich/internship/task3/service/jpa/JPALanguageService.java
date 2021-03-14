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
	
	@Override
	public List<Language> getAll() {
		return repo.findAll();
	}

	@Override
	public void save(Language entity) {
		repo.save(entity);
	}

	@Override
	public void delete(Language entity) {
		//localizationService.deleteAllByLanguage(entity);
		repo.delete(entity);
	}

	@Override
	public Language getById(int id) {
		return repo.getOne(id);
	}

	@Override
	public Language getByAbbreviation(String abbreviation) {
		return repo.findByAbbreviation(abbreviation);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (getAll().size()==0){
			save(new Language(null, "PL", null));
			save(new Language(null, "RU", null));
			save(new Language(null, "BY", null));
		}
	}
}
