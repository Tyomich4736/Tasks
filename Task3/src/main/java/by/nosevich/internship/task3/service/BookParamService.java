package by.nosevich.internship.task3.service;

import by.nosevich.internship.task3.dto.Book;
import by.nosevich.internship.task3.dto.BookParam;

import java.util.List;

public interface BookParamService extends DAO<BookParam>{
    List<BookParam> getAllForBook(Book book);
    BookParam getByNameAndBook(String name, Book book);
}
