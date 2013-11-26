package net.octacomm.sample.domain;

import com.mysema.query.types.Predicate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class UserParam extends DomainParam {
	private String searchWord;

	@Override
	public Predicate getCondition() {
		return null;
	} 
}
