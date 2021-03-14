package by.nosevich.internship.task3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import by.nosevich.internship.task3.dto.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>{
}
