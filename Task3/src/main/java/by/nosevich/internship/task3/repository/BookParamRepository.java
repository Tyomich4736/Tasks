package by.nosevich.internship.task3.repository;

import by.nosevich.internship.task3.dto.BookParam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookParamRepository extends JpaRepository<BookParam, Integer> {
}
