package by.nosevich.internship.task3.service;

import by.nosevich.internship.task3.dto.Book;
import by.nosevich.internship.task3.dto.Language;

import java.util.List;

public interface BookService extends DAO<Book> {
    List<Book> getLocalizedBooks(Language language);
    List<Book> getSortedByTargetParam(String paramName);
}
