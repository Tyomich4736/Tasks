package by.nosevich.internship.task3.repository;

import by.nosevich.internship.task3.dto.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import by.nosevich.internship.task3.dto.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>{

    @Query(value = "select b.id, l.value as \"name\" from books b " + //select books with localization
            "inner join localizations l on b.id = l.book_id " +
            "where l.language_id = :lang and l.value is not null " +
            "union " +
            "select b.id, b.name from books b where " + // select books without localization
            "(select l from localizations l " +
            "where l.language_id = :lang and l.book_id = b.id) " +
            "is null",
            nativeQuery = true)
    List<Book> findLocalizedBooks(@Param("lang") int langId);
}
