package by.nosevich.internship.task3.controllers;

import by.nosevich.internship.task3.dto.Language;
import by.nosevich.internship.task3.service.LanguageService;
import by.nosevich.internship.task3.service.LocalizationService;
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

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Artyom Nosevich
 * This controller is responsible for manage languages
 */
@RestController
@RequestMapping("/languages")
public class LanguagesController {

    @Autowired
    private LanguageService languageService;
    @Autowired
    private LocalizationService localizationService;

    /**
     * @return list with all localizations sorted by id
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Language>> getLanguages(){
        List<Language> langs = languageService.getAll().stream().
                sorted(Comparator.comparing(Language::getId)).collect(Collectors.toList());
        return new ResponseEntity<>(langs, HttpStatus.OK);
    }

    /**
     * This method returns number of localizations for each language
     * @return map where key is language abbreviation and value is number of localizations for this language
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Integer>> getLocalizationCount(){
        Map<String, Integer> map = new HashMap<>();
        languageService.getAll().forEach(language ->{
            map.put(language.getAbbreviation(), language.getLocalizations().size());
        });
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /**
     * This method removes target language
     * @param id the id of language to remove
     * @return ResponseEntity with status OK if language has been removed or status BAD_REQUEST if target language doesn't exist
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity deleteLanguage(@PathVariable("id") Integer id) {
        Language lang = languageService.getById(id);
        if (lang==null){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        languageService.delete(lang);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * This method save new language
     * @param language language, which should be created
     * @return ResponseEntity with status OK if language was saved or status BAD_REQUEST if target language abbreviation is empty
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity addLanguage(@RequestBody Language language) {
        if (!language.getAbbreviation().trim().equals("")) {
            languageService.save(language);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
