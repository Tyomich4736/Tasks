package by.nosevich.internship.task3.controllers;

import by.nosevich.internship.task3.dto.Book;
import by.nosevich.internship.task3.dto.Language;
import by.nosevich.internship.task3.dto.Localization;
import by.nosevich.internship.task3.service.BookService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Artyom Nosevich
 * This controller is responsible for manage localizations
 */
@RestController
@RequestMapping("/localizations")
public class LocalizationsController {
    @Autowired
    private BookService bookService;
    @Autowired
    private LanguageService languageService;
    @Autowired
    private LocalizationService localizationService;

    /**
     * @return list with all localizations sorted by language and value
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Localization>> getLocalizations(){
        List<Localization> locals = localizationService.getAll();
        List<Localization> sortedLocals = locals.stream().
                sorted(Comparator.comparing(o -> o.getValue().toLowerCase())).
                sorted(Comparator.comparing(o -> o.getLanguage().getAbbreviation().toLowerCase())).
                collect(Collectors.toList());
        return new ResponseEntity<>(sortedLocals, HttpStatus.OK);
    }

    /**
     * This method removes target localization
     * @param id the id of localization to remove
     * @return ResponseEntity with status OK if localization has been removed or status BAD_REQUEST if target localization doesn't exist
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteLocalization(@PathVariable("id") Integer id) {
        Localization local = localizationService.getById(id);
        if (local==null){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        localizationService.delete(local);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * This method add new localization
     * @param bookId the id of localized book
     * @param languageId the id of localization language
     * @param value localization value
     * @return ResponseEntity with status OK if localization was created or status CONFLICT if the same localization exist
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity addLocalization(@RequestParam("bookId") Integer bookId,
                                          @RequestParam("langId") Integer languageId,
                                          @RequestParam("value") String value) {
        Localization local = new Localization();
        Book book = bookService.getById(bookId);
        Language lang = languageService.getById(languageId);
        if (lang!=null && book!=null && value.trim()!="") {
            local.setBook(book);
            local.setLanguage(lang);
            local.setValue(value);
            if (!hasSameLocalization(local)) {
                localizationService.save(local);
                return new ResponseEntity(HttpStatus.OK);
            }
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    private boolean hasSameLocalization(Localization local) {
        return (localizationService.getByBookAndLanguage(local.getBook(),local.getLanguage())!=null);
    }
}
