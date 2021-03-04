package by.nosevich.internship.task3.service;

import java.util.List;

import by.nosevich.internship.task3.dto.Language;
import by.nosevich.internship.task3.service.DAO;

public interface LanguageService extends DAO<Language> {
	Language getByAbbreviation(String abbreviation);
}
