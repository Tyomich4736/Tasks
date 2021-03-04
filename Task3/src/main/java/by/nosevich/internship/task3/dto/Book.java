package by.nosevich.internship.task3.dto;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "books")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;
	
	private String name;
	
	@OneToMany(mappedBy = "book")
	private List<Localization> localizations;

	public boolean equals(Book obj) {
		if (this.getId().equals(obj.getId())
				&& this.getName().equals(obj.getName())) {
			return true;
		}
		return false;
	}
}
