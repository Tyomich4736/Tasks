package by.nosevich.internship.test.controllertests.unit;

import by.nosevich.internship.task3.controllers.BooksParamsController;
import by.nosevich.internship.task3.dto.Book;
import by.nosevich.internship.task3.dto.BookParam;
import by.nosevich.internship.task3.service.BookParamService;
import by.nosevich.internship.task3.service.BookService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class BooksParamsControllerTest {
    @Mock
    private BookService bookService;
    @Mock
    private BookParamService bookParamService;

    @InjectMocks
    private BooksParamsController booksParamsController;

    Book hobbit;
    BookParam numberOfPages;

    @Before
    public void init(){
        MockitoAnnotations.openMocks(this);
        hobbit = new Book(1, "Hobbit", null);
        numberOfPages = new BookParam(1, "Number of pages", "324", hobbit);
    }

    @Test
    public void getAllForBookTest(){
        given(bookService.getById(hobbit.getId())).willReturn(hobbit);
        given(bookParamService.getAllForBook(hobbit)).willReturn(List.of(numberOfPages));
        ResponseEntity<List<BookParam>> re = booksParamsController.getAllForBook(hobbit.getId());
        assertEquals(HttpStatus.OK, re.getStatusCode());
        assertEquals("result doesn't contain the book parameter ",numberOfPages, re.getBody().get(0));
    }

    @Test
    public void getAllForNonExistentBookTest(){
        given(bookService.getById(any(Integer.class))).willReturn(null);
        ResponseEntity<List<BookParam>> re = booksParamsController.getAllForBook(hobbit.getId());
        assertEquals(HttpStatus.BAD_REQUEST, re.getStatusCode());
    }

    @Test
    public void addBookParamTest(){
        given(bookService.getById(hobbit.getId())).willReturn(hobbit);
        ResponseEntity re = booksParamsController.addBookParam("Cover", "Solid", hobbit.getId());
        assertEquals(HttpStatus.OK, re.getStatusCode());
        verify(bookParamService).save(any(BookParam.class));
    }

    @Test
    public void addBookParamForNonExistentBookTest(){
        given(bookService.getById(any(Integer.class))).willReturn(null);
        ResponseEntity re = booksParamsController.addBookParam("Cover", "Solid", hobbit.getId());
        assertEquals(HttpStatus.BAD_REQUEST, re.getStatusCode());
    }

    @Test
    public void addBookParamWithEmptyNameTest(){
        given(bookService.getById(hobbit.getId())).willReturn(hobbit);
        ResponseEntity re = booksParamsController.addBookParam("   ", "Solid", hobbit.getId());
        assertEquals(HttpStatus.BAD_REQUEST, re.getStatusCode());
    }

    @Test
    public void addTheSameBookParamTest(){
        given(bookService.getById(any(Integer.class))).willReturn(hobbit);
        given(bookParamService.getByNameAndBook(numberOfPages.getName(), hobbit)).willReturn(numberOfPages);
        ResponseEntity re = booksParamsController.addBookParam(numberOfPages.getName(), "456", hobbit.getId());
        assertEquals(HttpStatus.CONFLICT, re.getStatusCode());
    }

    @Test
    public void deleteBookParamTest(){
        given(bookParamService.getById(numberOfPages.getId())).willReturn(numberOfPages);
        ResponseEntity re = booksParamsController.deleteBookParam(numberOfPages.getId());
        assertEquals(HttpStatus.OK, re.getStatusCode());
        verify(bookParamService).delete(numberOfPages);
    }

    @Test
    public void deleteNonExistentBookParamTest(){
        given(bookParamService.getById(numberOfPages.getId())).willReturn(null);
        ResponseEntity re = booksParamsController.deleteBookParam(numberOfPages.getId());
        assertEquals(HttpStatus.BAD_REQUEST, re.getStatusCode());
    }
}
