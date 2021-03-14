package by.nosevich.internship.task3.service.jpa;

import java.util.ArrayList;
import java.util.List;

import by.nosevich.internship.task3.dto.Book;
import by.nosevich.internship.task3.dto.Language;
import by.nosevich.internship.task3.repository.BookRepository;
import by.nosevich.internship.task3.dto.Localization;
import by.nosevich.internship.task3.service.LocalizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.Selection;
import org.springframework.stereotype.Service;

import by.nosevich.internship.task3.service.BookService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

@Service
public class JPABookService implements BookService{

	@Autowired
	private BookRepository repo;
	@Autowired
	private LocalizationService localizationService;
	@PersistenceContext
	private EntityManager em;
	
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
		//localizationService.deleteAllByBook(entity);
		repo.delete(entity);
	}

	@Override
	public Book getById(int id) {
		return repo.getOne(id);
	}

	@Override
	public List<Book> getLocalizedBooks(Language language) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
		Root<Book> root = query.from(Book.class);
		Join<Book, Localization> join = root.join("localizations",JoinType.LEFT);
		join.on(builder.and(
				builder.equal(root.get("id"),join.get("book"))),
				builder.equal(join.get("language").get("id"), builder.parameter(Integer.class,"langId")));
		query.multiselect(
				root.get("id"),
				builder.coalesce(join.get("value"),root.get("name")));

		List<Object[]> results = em.createQuery(query)
				.setParameter("langId", language.getId())
				.getResultList();
		return toBookList(results);
	}

	private List<Book> toBookList(List<Object[]> list){
		List<Book> result = new ArrayList<>();
		for(Object[] arr : list){
			Book book = new Book();
			book.setId(Integer.parseInt(arr[0].toString()));
			book.setName(arr[1].toString());
			result.add(book);
		}

		return result;
	}
}
