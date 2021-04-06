package by.nosevich.internship.task3.service.jpa;

import java.util.ArrayList;
import java.util.List;

import by.nosevich.internship.task3.dto.Book;
import by.nosevich.internship.task3.dto.BookParam;
import by.nosevich.internship.task3.dto.Language;
import by.nosevich.internship.task3.repository.BookRepository;
import by.nosevich.internship.task3.dto.Localization;
import by.nosevich.internship.task3.service.LocalizationService;
import org.hibernate.criterion.Order;
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

/**
 * This service is responsible for manage books in database
 */
@Service
public class JPABookService implements BookService{

	@Autowired
	private BookRepository repo;
	@Autowired
	private LocalizationService localizationService;
	@PersistenceContext
	private EntityManager em;

	/**
	 * @return all books from database
	 */
	@Override
	public List<Book> getAll() {
		return repo.findAll();
	}

	/**
	 * This method saves book in database
	 * @param book the book, which should be saved
	 */
	@Override
	public void save(Book book) {
		repo.save(book);
	}

	/**
	 * This method removes book from database
	 * @param book the book, which should be removed
	 */
	@Override
	public void delete(Book book) {
		repo.delete(book);
	}

	/**
	 * This method returns book by id from database
	 * @param id target id for search
	 * @return book if it found or null if not
	 */
	@Override
	public Book getById(int id) {
		return repo.findById(id).orElse(null);
	}

	/**
	 * This method returns books with it's localized name on target language if this name exist or with original name if not
	 * @param language the target language
	 * @return list with localized books
	 */
	@Override
	public List<Book> getLocalizedBooks(Language language) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
		Root<Book> book = query.from(Book.class);
		Join<Book, Localization> local = book.join("localizations",JoinType.LEFT);
		local.on(builder.and(
				builder.equal(book.get("id"),local.get("book"))),
				builder.equal(local.get("language").get("id"), builder.parameter(Integer.class,"langId")));
		query.multiselect(
				book.get("id"),
				builder.coalesce(local.get("value"),book.get("name")));

		List<Object[]> results = em.createQuery(query)
				.setParameter("langId", language.getId())
				.getResultList();
		return toBookList(results);
	}

	@Override
	public List<Book> getSortedByTargetParam(String paramName) {
		/*
		select b
		from books b
		left join book_params bp
		on b.id = bp.book_id and bp."name" = :paramName
		order by bp.value
		*/

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Book> query = builder.createQuery(Book.class);
		Root<Book> book = query.from(Book.class);
		Join<Book, BookParam> param = book.join("bookParams", JoinType.LEFT);
		param.on(
				builder.equal(book, param.get("book")),
				builder.equal(param.get("name"), builder.parameter(String.class, "paramName"))
		);
		query.select(book).orderBy(builder.asc(param.get("value")));

		List<Book> list = em.createQuery(query)
				.setParameter("paramName", paramName)
				.getResultList();
		return list;
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
