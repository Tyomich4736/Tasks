package by.nosevich.internship.task3.service.jpa;

import by.nosevich.internship.task3.dto.Book;
import by.nosevich.internship.task3.dto.BookParam;
import by.nosevich.internship.task3.repository.BookParamRepository;
import by.nosevich.internship.task3.service.BookParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.Selection;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * This service is responsible for manage books parameters in database
 */
@Service
public class JPABookParamService implements BookParamService{

    @Autowired
    private BookParamRepository bookParamRepository;
    @PersistenceContext
    private EntityManager em;

    /**
     * @return all books parameters from database
     */
    @Override
    public List<BookParam> getAll() {
        return bookParamRepository.findAll();
    }

    /**
     * This method saves book parameter in database
     * @param bookParam the parameter, which should be saved
     */
    @Override
    public void save(BookParam bookParam) {
        bookParamRepository.save(bookParam);
    }

    /**
     * This method removes book parameter from database
     * @param bookParam the parameter, which should be removed
     */
    @Override
    public void delete(BookParam bookParam) {
        bookParamRepository.delete(bookParam);
    }

    /**
     * This method returns book parameter by id from database
     * @param id target id for search
     * @return parameter if it found or null if not
     */
    @Override
    public BookParam getById(int id) {
        return bookParamRepository.findById(id).orElse(null);
    }

    /**
     * This method returns book parameters for target book
     * @param book target book for search
     * @return list of parameters for book
     */
    @Override
    public List<BookParam> getAllForBook(Book book) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<BookParam> query = builder.createQuery(BookParam.class);
        Root<BookParam> bookParam = query.from(BookParam.class);
        query.select(bookParam).
                where(builder.equal(bookParam.get("book"), builder.parameter(Book.class,"book")));

        List<BookParam> result = em.createQuery(query)
                .setParameter("book", book)
                .getResultList();
        return result;
    }

    @Override
    public BookParam getByNameAndBook(String name, Book book) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<BookParam> query = builder.createQuery(BookParam.class);
        Root<BookParam> bookParam = query.from(BookParam.class);
        query.select(bookParam).
                where(builder.and(
                        builder.equal(bookParam.get("name"), builder.parameter(String.class, "name")),
                        builder.equal(bookParam.get("book"), builder.parameter(Book.class, "book"))
                ));

        List<BookParam> list = em.createQuery(query)
                .setParameter("book", book)
                .setParameter("name", name)
                .getResultList();
        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
