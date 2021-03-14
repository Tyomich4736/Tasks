package by.nosevich.internship.task3.repository;

import by.nosevich.internship.task3.dto.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import by.nosevich.internship.task3.dto.Book;
import by.nosevich.internship.task3.dto.Localization;

@Repository
public interface LocalizationRepository extends JpaRepository<Localization, Integer>{
	
	@Query(value = "select * from localizations"
    		+ " where book_id=:bookId and language_id=:langId", nativeQuery = true)
	Localization findByBookAndLanguage(@Param("bookId") int bookId,@Param("langId") int langId);

	@Modifying
    @Query(value = "insert into localizations (id,value, book_id, language_id)"
    		+ " VALUES (:id,:value,:bookId,:languageId)", nativeQuery = true)
	void insert(@Param("id") int id, @Param("value") String value,
			@Param("bookId") int bookId, @Param("languageId") int languageId);

	void deleteAllByBook(Book book);
	void deleteAllByLanguage(Language language);
}
