package by.nosevich.internship.test.controllertests.unit;

import by.nosevich.internship.task3.controllers.LanguagesController;
import by.nosevich.internship.task3.dto.Language;
import by.nosevich.internship.task3.dto.Localization;
import by.nosevich.internship.task3.service.LanguageService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class LanguagesControllerTest {
    @Mock
    private LanguageService languageService;

    @InjectMocks
    private LanguagesController languagesController;

    private Language english;

    @Before
    public void init(){
        MockitoAnnotations.openMocks(this);
        english = new Language(1, "EN", null);
    }

    @Test
    public void addLanguageTest(){
        ResponseEntity re = languagesController.addLanguage(english);
        assertEquals(HttpStatus.OK, re.getStatusCode());
        verify(languageService).save(english);
    }

    @Test
    public void addLanguageWithEmptyAbbreviationTest(){
        ResponseEntity re = languagesController.addLanguage(
                new Language(null, "   ", null));
        assertEquals(HttpStatus.BAD_REQUEST, re.getStatusCode());
    }

    @Test
    public void deleteLanguageTest(){
        given(languageService.getById(english.getId())).willReturn(english);
        ResponseEntity re = languagesController.deleteLanguage(english.getId());
        assertEquals(HttpStatus.OK, re.getStatusCode());
        verify(languageService).delete(english);
    }

    @Test
    public void deleteNonExistentLanguageTest(){
        given(languageService.getById(english.getId())).willReturn(null);
        ResponseEntity re = languagesController.deleteLanguage(english.getId());
        assertEquals(HttpStatus.BAD_REQUEST, re.getStatusCode());
    }

    @Test
    public void getLocalizationCountTest(){
        english.setLocalizations(List.of(
                new Localization(),
                new Localization()
        ));

        given(languageService.getAll()).willReturn(List.of(english));
        ResponseEntity<Map<String, Integer>> re = languagesController.getLocalizationCount();

        assertEquals(HttpStatus.OK, re.getStatusCode());
        assertEquals(2, re.getBody().get(english.getAbbreviation()),
                "Returned number doesn't match the number of localizations");
    }
}