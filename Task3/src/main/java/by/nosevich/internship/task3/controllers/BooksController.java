package by.nosevich.internship.task3.controllers;

import by.nosevich.internship.task3.dto.Book;
import by.nosevich.internship.task3.dto.Language;
import by.nosevich.internship.task3.service.BookService;
import by.nosevich.internship.task3.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import java.util.stream.Collectors;

/**
 * @author Artyom Nosevich
 * This controller is responsible for manage books
 */
@RestController
@RequestMapping("/books")
public class BooksController {
    @Autowired
    private BookService bookService;
    @Autowired
    private LanguageService languageService;

    /**
     * @return list with all books
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Book>> getBooks(){
        List<Book> books = bookService.getAll();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * This method returns books with it's localized name if this exist or original name if not and with target substring in name
     * @param langAbbreviation abbreviation of language, for which localizations will found
     * @param substring substring, for which localizations will found
     * @return list of found books
     */
    @GetMapping(value = "/{lang}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Book>> getBooksForLanguage(@PathVariable("lang") String langAbbreviation,
                                                          @RequestParam("substring") String substring) {
        Language language = languageService.getByAbbreviation(langAbbreviation);
        if (language==null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Book> books = bookService.getLocalizedBooks(language).
                stream().filter(book -> book.getName().toLowerCase().contains(substring.toLowerCase())).
                collect(Collectors.toList());
        return new ResponseEntity<>(books,HttpStatus.OK);
    }

    /**
     * This method removes target book
     * @param id the id of book to remove
     * @return ResponseEntity with status OK if language has been removed or status BAD_REQUEST if target language doesn't exist
     */
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity deleteBook(@PathVariable("id") Integer id) {
        Book book = bookService.getById(id);
        if (book==null){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        bookService.delete(book);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * This method save new book
     * @param book book, which should be created
     * @return ResponseEntity with status OK if book was saved or status BAD_REQUEST if target book name is empty
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity addBook(@RequestBody Book book) {
        if (!book.getName().trim().equals("")) {
            bookService.save(book);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
