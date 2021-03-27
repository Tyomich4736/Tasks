package by.nosevich.internship.test.controllertests;

import by.nosevich.internship.task3.controllers.LocalizationsController;
import by.nosevich.internship.task3.dto.Book;
import by.nosevich.internship.task3.dto.Language;
import by.nosevich.internship.task3.dto.Localization;
import by.nosevich.internship.task3.service.BookService;
import by.nosevich.internship.task3.service.LanguageService;
import by.nosevich.internship.task3.service.LocalizationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {by.nosevich.internship.task3.Task3Application.class})
@Transactional
public class LocalizationsControllerTest {

    Book book;
    Language language;

    @Autowired
    LocalizationService localizationService;
    @Autowired
    BookService bookService;
    @Autowired
    LanguageService languageService;
    @Autowired
    LocalizationsController localizationsController;

    @BeforeEach
    public void init(){
        book = new Book(null, "Хоббит", null);
        bookService.save(book);
        language = new Language(null, "EN", null);
        languageService.save(language);
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void addLocalizationTest(){
        ResponseEntity re = localizationsController.addLocalization(book.getId(), language.getId(), "Hobbit");
        assertEquals(HttpStatus.OK, re.getStatusCode());

        re = localizationsController.addLocalization(book.getId(), language.getId(), "Same Localization");
        assertEquals(HttpStatus.CONFLICT, re.getStatusCode());

        re = localizationsController.addLocalization(0, language.getId(), "Localization");
        assertEquals(HttpStatus.BAD_REQUEST, re.getStatusCode());
        re = localizationsController.addLocalization(book.getId(), 0, "Localization");
        assertEquals(HttpStatus.BAD_REQUEST, re.getStatusCode());

        localizationService.delete(localizationService.getByBookAndLanguage(book,language));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void deleteLocalizationTest(){
        Localization localization = new Localization(null, book, language, "Hobbit");
        localizationService.save(localization);
        ResponseEntity re = localizationsController.deleteLocalization(localization.getId());
        assertEquals(HttpStatus.OK, re.getStatusCode());

        re = localizationsController.deleteLocalization(0);
        assertEquals(HttpStatus.BAD_REQUEST, re.getStatusCode());
    }

    @AfterEach
    public void end(){
        bookService.delete(book);
        languageService.delete(language);
    }
}
