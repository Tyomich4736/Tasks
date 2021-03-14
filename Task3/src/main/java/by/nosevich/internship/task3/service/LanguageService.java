package by.nosevich.internship.task3.service;

import by.nosevich.internship.task3.dto.Language;

public interface LanguageService extends DAO<Language> {
	Language getByAbbreviation(String abbreviation);
}
