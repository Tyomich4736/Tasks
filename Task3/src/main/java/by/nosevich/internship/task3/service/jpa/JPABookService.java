package by.nosevich.internship.task3.service.jpa;

import java.util.List;

import by.nosevich.internship.task3.dto.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import by.nosevich.internship.task3.dto.Book;
import by.nosevich.internship.task3.repository.BookRepository;
import by.nosevich.internship.task3.service.BookService;

@Service
public class JPABookService implements BookService{

	@Autowired
	private BookRepository repo;
	
	@Override
	public List<Book> getAll() {
		return repo.findAll();
	}

	@Override
	public void save(Book entity) {
		repo.save(entity);
	}

	@Override
	public void delete(Book entity) {
		repo.delete(entity);
	}

	@Override
	public Book getById(int id) {
		return repo.getOne(id);
	}

	@Override
	public List<Book> getLocalizedBooks(Language language) {
		return repo.findLocalizedBooks(language.getId());
	}
}
