package by.nosevich.internship.test.controllertests.unit;

import by.nosevich.internship.task3.controllers.LocalizationsController;
import by.nosevich.internship.task3.dto.Book;
import by.nosevich.internship.task3.dto.Language;
import by.nosevich.internship.task3.dto.Localization;
import by.nosevich.internship.task3.service.BookService;
import by.nosevich.internship.task3.service.LanguageService;
import by.nosevich.internship.task3.service.LocalizationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class LocalizationsControllerTest {
    @Mock
    private BookService bookService;
    @Mock
    private LanguageService languageService;
    @Mock
    private LocalizationService localizationService;

    private LocalizationsController localizationsController;

    Book hobbit;
    Language language;
    Localization localization;

    public LocalizationsControllerTest(){
        MockitoAnnotations.openMocks(this);
        localizationsController = new LocalizationsController(
                bookService,
                languageService,
                localizationService);
        hobbit = new Book(1,"Хоббит",null);
        language = new Language(1,"EN",null);
        localization = new Localization(1, hobbit, language, "Hobbit");
    }

    @Test
    public void addLocalizationTest(){
        given(localizationService.getByBookAndLanguage(hobbit, language)).willReturn(null);
        given(bookService.getById(hobbit.getId())).willReturn(hobbit);
        given(languageService.getById(language.getId())).willReturn(language);
        ResponseEntity re = localizationsController.addLocalization(hobbit.getId(), language.getId(), "Hobbit");
        assertEquals(HttpStatus.OK, re.getStatusCode());
        verify(localizationService).save(any(Localization.class));

        given(localizationService.getByBookAndLanguage(hobbit, language)).willReturn(localization);
        re = localizationsController.addLocalization(hobbit.getId(), language.getId(), "Same Localization");
        assertEquals(HttpStatus.CONFLICT, re.getStatusCode());

        given(bookService.getById(hobbit.getId())).willReturn(null);
        re = localizationsController.addLocalization(hobbit.getId(), language.getId(), "Localization");
        assertEquals(HttpStatus.BAD_REQUEST, re.getStatusCode());
    }

    @Test
    public void deleteLocalizationTest(){
        given(localizationService.getById(localization.getId())).willReturn(localization);
        ResponseEntity re = localizationsController.deleteLocalization(localization.getId());
        assertEquals(HttpStatus.OK, re.getStatusCode());
        verify(localizationService).delete(localization);

        given(localizationService.getById(localization.getId())).willReturn(null);
        re = localizationsController.deleteLocalization(localization.getId());
        assertEquals(HttpStatus.BAD_REQUEST, re.getStatusCode());
    }
}
