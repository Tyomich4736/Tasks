package by.nosevich.internship.test.controllertests.integration;

import by.nosevich.internship.task3.controllers.LanguagesController;
import by.nosevich.internship.task3.dto.Language;
import by.nosevich.internship.task3.service.LanguageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {by.nosevich.internship.task3.Task3Application.class})
@Transactional
public class LanguagesControllerTest {

    @Autowired
    LanguageService languageService;
    @Autowired
    LanguagesController languagesController;

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void addLanguageTest(){
        Language language = new Language(null, "EN", null);
        ResponseEntity re = languagesController.addLanguage(language);
        assertEquals(HttpStatus.OK, re.getStatusCode());
        languageService.delete(language);

        re = languagesController.addLanguage(new Language(null, "   ", null));
        assertEquals(HttpStatus.BAD_REQUEST, re.getStatusCode());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void deleteLanguageTest(){
        Language language = new Language(null, "EN", null);
        languageService.save(language);
        ResponseEntity re = languagesController.deleteLanguage(language.getId());
        assertEquals(HttpStatus.OK, re.getStatusCode());

        re = languagesController.deleteLanguage(-1);
        assertEquals(HttpStatus.BAD_REQUEST, re.getStatusCode());
    }
}
