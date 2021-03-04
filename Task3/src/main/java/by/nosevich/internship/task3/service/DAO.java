package by.nosevich.internship.task3.service;

import java.util.List;

public interface DAO<T> {
	List<T> getAll();
	void save(T entity);
	void delete(T entity);
	T getById(int id);
}
