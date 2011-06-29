package com.dasolute.universe.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dasolute.universe.domain.Document;

@Controller
@RequestMapping("/domains")
public class DomainController {

	@RequestMapping(value = "/{domainName}/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("domainName") String domainName, @PathVariable("id") Long id, ModelMap modelMap) {
		if (id == null)
			throw new IllegalArgumentException("An Identifier is required");

		modelMap.addAttribute(domainName, Document.findDocument(id));

		return domainName + "s/show";
	}

}