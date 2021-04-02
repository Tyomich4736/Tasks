package by.nosevich.internship.task3.controllers;

import by.nosevich.internship.task3.dto.Book;
import by.nosevich.internship.task3.dto.BookParam;
import by.nosevich.internship.task3.service.BookParamService;
import by.nosevich.internship.task3.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/params")
public class BooksParamsController {

    private BookService bookService;
    private BookParamService bookParamService;

    @Autowired
    public BooksParamsController(BookService bookService, BookParamService bookParamService) {
        this.bookService = bookService;
        this.bookParamService = bookParamService;
    }

    /**
     * @return Response Entity with list of all parameters of target book if book exist or with bad request status if not
     * @param id the id of target book
     */
    @GetMapping("/book/{id}")
    public ResponseEntity<List<BookParam>> getAllForBook(@PathVariable("id") Integer id){
        Book book = bookService.getById(id);
        if (book==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<BookParam> result = bookParamService.getAllForBook(book);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * This method add new book parameter
     * @param bookId the id of book, which should have parameter
     * @param name the name of new parameter
     * @param value the value of new parameter
     * @return ResponseEntity with status OK if parameter was created or status CONFLICT if the same localization exist
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity addBookParam(@RequestParam("name") String name,
                                       @RequestParam("value") String value,
                                       @RequestParam("bookId") Integer bookId){
        Book book = bookService.getById(bookId);
        if (!name.trim().equals("") && book!=null){
            BookParam param = new BookParam(null, name, value, book);
            if (!hasSameParam(param)) {
                bookParamService.save(param);
                return new ResponseEntity(HttpStatus.OK);
            }
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
    private boolean hasSameParam(BookParam param){
        return (bookParamService.getByNameAndBook(param.getName(), param.getBook())!=null);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity deleteBookParam(@PathVariable("id") Integer id){
        BookParam param = bookParamService.getById(id);
        if (param==null){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        bookParamService.delete(param);
        return new ResponseEntity(HttpStatus.OK);
    }

}
