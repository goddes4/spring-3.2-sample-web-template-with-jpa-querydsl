package net.octacomm.sample.domain;

import com.mysema.query.types.Predicate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class DomainParam {
	protected int currentPage = 1;
	protected int pageSize;
	protected int pageGroupSize;
	
	public abstract Predicate getCondition(); 
}
