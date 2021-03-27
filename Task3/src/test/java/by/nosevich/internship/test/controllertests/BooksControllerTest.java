package by.nosevich.internship.test.controllertests;

import by.nosevich.internship.task3.controllers.BooksController;
import by.nosevich.internship.task3.dto.Book;
import by.nosevich.internship.task3.dto.Language;
import by.nosevich.internship.task3.dto.Localization;
import by.nosevich.internship.task3.service.BookService;
import by.nosevich.internship.task3.service.LanguageService;
import by.nosevich.internship.task3.service.LocalizationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import javax.transaction.Transactional;

@SpringBootTest(classes = {by.nosevich.internship.task3.Task3Application.class})
@Transactional
public class BooksControllerTest {

    @Autowired
    BookService bookService;
    @Autowired
    LanguageService languageService;
    @Autowired
    LocalizationService localizationService;
    @Autowired
    BooksController booksController;

    private static Book lordOfTheRings;
    private static Book hobbit;

    @BeforeEach
    public void init(){
        lordOfTheRings = new Book(null, "Lord of the Rings", null);
        hobbit = new Book(null, "Hobbit", null);
        bookService.save(lordOfTheRings);
        bookService.save(hobbit);
    }

    @Test
    public void getBooksForLanguageTest(){
        Language language = new Language(null, "EN", null);
        languageService.save(language);
        Localization localization = new Localization(null, lordOfTheRings, language, "Властелин Колец");
        localizationService.save(localization);
        ResponseEntity<List<Book>> re = booksController.getBooksForLanguage(language.getAbbreviation(),"");
        assertTrue(hasBookWithName(re.getBody(),"Властелин Колец"));
        languageService.delete(language);
    }
    private boolean hasBookWithName(List<Book> books, String name){
        for(Book book : books){
            if (book.getName().equals(name))
                return true;
        }
        return false;
    }

    @Test
    public void getBooksForIncorrectLanguageTest(){
        ResponseEntity<List<Book>> re = booksController.getBooksForLanguage("-1","");
        assertEquals(HttpStatus.BAD_REQUEST, re.getStatusCode());
    }

    @Test
    public void getBooksForLanguageAndSubstringTest(){
        Language language = new Language(null, "EN", null);
        languageService.save(language);
        ResponseEntity<List<Book>> re = booksController.getBooksForLanguage(language.getAbbreviation(),"Hob");
        assertEquals(hobbit.getName(), re.getBody().get(0).getName());
        languageService.delete(language);
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void deleteBookTest(){
        Book witcher = new Book(null, "The Witcher", null);
        bookService.save(witcher);
        ResponseEntity re = booksController.deleteBook(witcher.getId());
        assertEquals(HttpStatus.OK, re.getStatusCode());

        re = booksController.deleteBook(0);
        assertEquals(HttpStatus.BAD_REQUEST, re.getStatusCode());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void addBookTest(){
        Book witcher = new Book(null, "The Witcher", null);
        ResponseEntity re = booksController.addBook(witcher);
        assertEquals(HttpStatus.OK, re.getStatusCode());
        bookService.delete(witcher);

        re = booksController.addBook(new Book(null, "   ", null));
        assertEquals(HttpStatus.BAD_REQUEST, re.getStatusCode());
    }

    @AfterEach
    public void end(){
        bookService.delete(lordOfTheRings);
        bookService.delete(hobbit);
    }
}
