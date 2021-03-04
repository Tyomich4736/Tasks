package by.nosevich.internship.task3.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

@Controller
public class MainController {
	
	@Autowired
	private BookService bookService;
	@Autowired
	private LanguageService languageService;
	@Autowired
	private LocalizationService localizationService;
	
	
	@GetMapping("")
	public String getMainPage(Model model) {
		model.addAttribute("books", bookService.getAll());
		model.addAttribute("languages", languageService.getAll());
		model.addAttribute("localizations", localizationService.getAll());
		return "mainPage";
	}
	
	@GetMapping("/lang/{lang}")
	public String getMainPageByLang(HttpServletRequest request, Model model,
			@PathVariable("lang") String langAbbreviation) {
		Language language = languageService.getByAbbreviation(langAbbreviation);
		if (language!=null) {
			List<Book> books = bookService.getAll();
			model.addAttribute("books", changedBooksForLocal(language, books));
			model.addAttribute("languages", languageService.getAll());
			model.addAttribute("localizations", localizationService.getAll());
			return "mainPage";
		} else {
			return redirectBack(request);
		}
	}
	
	private List<Book> changedBooksForLocal(Language lang, List<Book> books){
		List<Book> localizedBooks = new ArrayList<>();
		for(Book book : books) {
			Localization local = localizationService.getByBookAndLanguage(book, lang);
			if (local!=null) {
				localizedBooks.add(new Book(book.getId(), local.getValue(), null));
			} else {
				localizedBooks.add(book);
			}
		}
		return localizedBooks;
	}
	
	@GetMapping("/deleteBook")
	public String deleteBook(HttpServletRequest request,
			@RequestParam("id") Integer id) {
		Book book = bookService.getById(id);
		bookService.delete(book);
		return redirectBack(request);
	}
	
	@GetMapping("/deleteLanguage")
	public String deleteLanguage(HttpServletRequest request,
			@RequestParam("id") Integer id) {
		Language lang = languageService.getById(id);
		languageService.delete(lang);
		return "redirect:";
	}
	
	@GetMapping("/deleteLocalization")
	public String deleteLocalization(HttpServletRequest request,
			@RequestParam("id") Integer id) {
		Localization local = localizationService.getById(id);
		localizationService.delete(local);
		return redirectBack(request);
	}
	
	@PostMapping("/addBook")
	public String addBook(HttpServletRequest request, @RequestParam("name") String bookName) {
		Book book = new Book();
		book.setName(bookName);
		bookService.save(book);
		return redirectBack(request);
	}
	
	@PostMapping("/addLanguage")
	public String addLanguage(HttpServletRequest request, @RequestParam("abbreviation") String abbreviation) {
		Language lang = new Language();
		lang.setAbbreviation(abbreviation);
		languageService.save(lang);
		return redirectBack(request);
	}
	
	@PostMapping("/addLocalization")
	public String addLocalization(HttpServletRequest request,
			@RequestParam("bookId") Integer bookId,
			@RequestParam("languageId") Integer languageId,
			@RequestParam("value") String value) {
		Localization local = new Localization();
		local.setBook(bookService.getById(bookId));
		local.setLanguage(languageService.getById(languageId));
		local.setValue(value);
		if (!hasSameLocalization(local)) {
			localizationService.save(local);
		}
		return redirectBack(request);
	}
	
	private String redirectBack(HttpServletRequest request) {
		String referer = request.getHeader("Referer");
	    return "redirect:"+ referer;
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
