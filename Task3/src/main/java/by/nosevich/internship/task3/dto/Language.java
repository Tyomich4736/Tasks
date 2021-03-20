package by.nosevich.internship.task3.dto;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "languages")
public class Language {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;
	
	private String abbreviation;

	@JsonIgnore
	@OneToMany(mappedBy = "language")
	private List<Localization> localizations;

	public boolean equals(Language obj) {
		if (this.getId().equals(obj.getId())
				&& this.getAbbreviation().equals(obj.getAbbreviation())) {
			return true;
		}
		return false;
	}
}
