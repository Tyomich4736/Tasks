package by.nosevich.internship.test.controllertests.unit;

import by.nosevich.internship.task3.controllers.BooksController;
import by.nosevich.internship.task3.dto.Book;
import by.nosevich.internship.task3.dto.Language;
import by.nosevich.internship.task3.service.BookService;
import by.nosevich.internship.task3.service.LanguageService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class BooksControllerTest {
    @Mock
    private BookService bookService;
    @Mock
    private LanguageService languageService;

    @InjectMocks
    private BooksController booksController;

    Book hobbit;

    @Before
    public void init(){
        MockitoAnnotations.openMocks(this);
        hobbit = new Book(1, "Hobbit", null);
    }

    @Test
    public void getBooksForLanguageTest(){
        Language language = new Language(null, "RU", null);
        given(languageService.getByAbbreviation(language.getAbbreviation())).willReturn(language);
        given(bookService.getLocalizedBooks(language)).willReturn(List.of(
                new Book(null, "Властелин Колец", null)
        ));

        ResponseEntity<List<Book>> re = booksController.getBooksForLanguage(language.getAbbreviation(),"");
        assertTrue(hasBookWithName(re.getBody(),"Властелин Колец"),
                "Returned list of books doesn't contain book with required name");
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
        given(languageService.getByAbbreviation(anyString())).willReturn(null);

        ResponseEntity<List<Book>> re = booksController.getBooksForLanguage("-1","");
        assertEquals(HttpStatus.BAD_REQUEST, re.getStatusCode());
    }

    @Test
    public void deleteBookTest() {
        given(bookService.getById(hobbit.getId())).willReturn(hobbit);
        ResponseEntity re = booksController.deleteBook(hobbit.getId());
        assertEquals(HttpStatus.OK, re.getStatusCode());
        verify(bookService).delete(hobbit);
    }

    public void deleteNonExistentBookTest() {
        given(bookService.getById(anyInt())).willReturn(null);
        ResponseEntity re = booksController.deleteBook(0);
        assertEquals(HttpStatus.BAD_REQUEST, re.getStatusCode());
    }

    @Test
    public void addBookTest(){
        ResponseEntity re = booksController.addBook(hobbit);
        verify(bookService).save(hobbit);
        assertEquals(HttpStatus.OK, re.getStatusCode());
    }

    @Test
    public void addBookWithEmptyNameTest(){
        ResponseEntity re = booksController.addBook(new Book(null, "   ", null));
        assertEquals(HttpStatus.BAD_REQUEST, re.getStatusCode());
    }
}
