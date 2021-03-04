package by.nosevich.internship.task3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import by.nosevich.internship.task3.dto.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>{

}
