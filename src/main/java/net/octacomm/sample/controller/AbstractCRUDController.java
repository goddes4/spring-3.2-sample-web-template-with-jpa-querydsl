package net.octacomm.sample.controller;

import java.io.Serializable;

import net.octacomm.logger.Log;
import net.octacomm.sample.dao.DefaultRepository;
import net.octacomm.sample.domain.Domain;
import net.octacomm.sample.domain.DomainParam;

import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * CRUD (Create, Retrieve, Update, Delete) 기능을 수행하는 추상 클래스.
 * 도메인 클래스에 대해 CRUD 기능을 하려면, 본 추상 클래스를 상속받아 사용한다. 
 * 
 * @author taeyo
 *
 * @param <M> CRUDMapper 를 상속받은 SQL Mapper 인터페이스
 * @param <D> Domain 을 상속받은 클래스
 * @param <P> DomainParam 을 상속받은 클래스 (검색에 사용할 클래스)
 */
@SessionAttributes("domain")
public abstract class AbstractCRUDController<D extends Domain, P extends DomainParam, PK extends Serializable> {

	@Log
	private Logger logger;
	
	protected static final String URL_LIST = "/list";
	protected static final String URL_REGIST = "/regist";
	protected static final String URL_DETAIL = "/detail";
	protected static final String URL_UPDATE = "/update";
	protected static final String URL_DELETE = "/delete";

	protected DefaultRepository<D, PK> repository;

	/**
	 * 반드시 CRUDMapper 를 지정해야 한다.
	 * @param mapper
	 */
	public abstract void setCRUDMapper(DefaultRepository<D, PK> repository);

	@RequestMapping(value = URL_LIST)
	public void list(Model model, @ModelAttribute("domainParam") P param, BindingResult result) {
	
		logger.debug("Search Param : {}", param);
		
		int totalCount = (int) repository.count(param.getCondition());
		
		logger.debug("Total Count : {}", totalCount);
	
		Page<D> page = repository.findAll(param.getCondition(), new PageRequest(param.getCurrentPage(), param.getPageSize()));
	
		logger.debug("Domain List Size : {}", page.getSize());
		
		model.addAttribute("page", page);		
	}

	@RequestMapping(value = URL_REGIST, method = RequestMethod.GET)
	public void regist(Model model) throws InstantiationException, IllegalAccessException {
		model.addAttribute("domain", getDomainClass().newInstance());
	}

	protected abstract Class<D> getDomainClass();

	@RequestMapping(value = URL_REGIST, method = RequestMethod.POST)
	public String regist(@ModelAttribute("domain") D domain, SessionStatus sessionStatus) {
	
		logger.debug("domain : {}", domain);

		try {
			repository.save(domain);
			sessionStatus.setComplete();
			return getRedirectUrl();
		} catch (Exception e) {
			logger.error("", e);
			return URL_REGIST;
		}
	}

	protected abstract String getRedirectUrl();

	@RequestMapping(value = { URL_UPDATE, URL_DETAIL }, method = RequestMethod.GET)
	public void form(Model model, @RequestParam PK id) {
		model.addAttribute("domain", repository.findOne(id));
	}

	@RequestMapping(value = URL_UPDATE, method = RequestMethod.POST)
	public String update(@ModelAttribute("domain") D domain, RedirectAttributes redirectAttributes) {
	
		logger.debug("domain : {}", domain);

		try {
			repository.save(domain);
			return getRedirectUrl();
		} catch (Exception e) {
			logger.error("", e);
			return URL_UPDATE;
		}
	}

	@RequestMapping(value = URL_DELETE, method = RequestMethod.GET)
	public String delete(@RequestParam PK id, RedirectAttributes redirectAttributes) {
	
		try {
			repository.delete(id);
			return getRedirectUrl();
		} catch (Exception e) {
			logger.error("", e);
			throw new RuntimeException("삭제중 오류");
		}
	}

}