package by.nosevich.internship.task3.controllers;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import by.nosevich.internship.task3.dto.Book;
import by.nosevich.internship.task3.dto.Language;
import by.nosevich.internship.task3.dto.Localization;
import by.nosevich.internship.task3.service.BookService;
import by.nosevich.internship.task3.service.LanguageService;
import by.nosevich.internship.task3.service.LocalizationService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainRestController {
	
	@Autowired
	private BookService bookService;
	@Autowired
	private LanguageService languageService;
	@Autowired
	private LocalizationService localizationService;

	@GetMapping(value = "/books",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Book>> getBooks(){
		List<Book> books = bookService.getAll();
		return new ResponseEntity<>(books, HttpStatus.OK);
	}
	@GetMapping(value = "/languages",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Language>> getLanguages(){
		List<Language> langs = languageService.getAll();
		return new ResponseEntity<>(langs, HttpStatus.OK);
	}
	@GetMapping(value = "/localizations",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Localization>> getLocalizations(){
		List<Localization> locals = localizationService.getAll();
		return new ResponseEntity<>(locals, HttpStatus.OK);
	}

	
	@GetMapping(value = "/books/{lang}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Book>> getBooksForLanguage(@PathVariable("lang") String langAbbreviation) {
		Language language = languageService.getByAbbreviation(langAbbreviation);
		if (language==null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		List<Book> books = bookService.getLocalizedBooks(language);
		return new ResponseEntity<>(books,HttpStatus.OK);
	}
	
	@DeleteMapping("/books")
	public ResponseEntity deleteBook(@RequestParam("id") Integer id) {
		Book book = bookService.getById(id);
		bookService.delete(book);
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@DeleteMapping("/languages")
	public ResponseEntity deleteLanguage(@RequestParam("id") Integer id) {
		Language lang = languageService.getById(id);
		languageService.delete(lang);
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@DeleteMapping("/localizations")
	public ResponseEntity deleteLocalization(@RequestParam("id") Integer id) {
		Localization local = localizationService.getById(id);
		localizationService.delete(local);
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@PostMapping("/books")
	public ResponseEntity addBook(@RequestParam("name") String bookName) {
		if (bookName!="") {
			Book book = new Book();
			book.setName(bookName);
			bookService.save(book);
			return new ResponseEntity(HttpStatus.OK);
		}
		return new ResponseEntity(HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/languages")
	public ResponseEntity addLanguage(@RequestParam("abbreviation") String abbreviation) {
		if (abbreviation!="") {
			Language lang = new Language();
			lang.setAbbreviation(abbreviation);
			languageService.save(lang);
			return new ResponseEntity(HttpStatus.OK);
		}
		return new ResponseEntity(HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/localizations")
	public ResponseEntity addLocalization(@RequestParam("bookId") Integer bookId,
			@RequestParam("langId") Integer languageId,
			@RequestParam("value") String value) {
		Localization local = new Localization();
		local.setBook(bookService.getById(bookId));
		local.setLanguage(languageService.getById(languageId));
		local.setValue(value);
		if (!hasSameLocalization(local)) {
			localizationService.save(local);
		}
		return new ResponseEntity(HttpStatus.OK);
	}
	
	private boolean hasSameLocalization(Localization local) {
		for(Localization currentLocal : localizationService.getAll()) {
			if (currentLocal.getBook().equals(local.getBook())
					&& currentLocal.getLanguage().equals(local.getLanguage())) {
				return true;
			}
		}
		return false;
	}
}
