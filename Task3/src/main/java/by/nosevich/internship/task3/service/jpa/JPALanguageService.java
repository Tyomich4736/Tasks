package by.nosevich.internship.task3.service.jpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import by.nosevich.internship.task3.dto.Language;
import by.nosevich.internship.task3.repository.LanguageRepository;
import by.nosevich.internship.task3.service.LanguageService;

@Service
public class JPALanguageService implements LanguageService{

	@Autowired
	private LanguageRepository repo;
	
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

}
