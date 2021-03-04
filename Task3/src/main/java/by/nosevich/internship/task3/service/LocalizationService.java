package by.nosevich.internship.task3.service;

import by.nosevich.internship.task3.dto.Book;
import by.nosevich.internship.task3.dto.Language;
import by.nosevich.internship.task3.dto.Localization;

public interface LocalizationService extends DAO<Localization>{
	Localization getByBookAndLanguage(Book book, Language language);
}
