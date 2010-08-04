package com.dasolute.universe.web;

import javax.validation.Valid;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
// import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import com.dasolute.universe.domain.Document;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;

// @RooWebScaffold(path = "documents", formBackingObject = Document.class)
@Controller
@RequestMapping("/documents")
public class DocumentController {

	@RequestMapping(method = RequestMethod.POST)
    public String create(@Valid Document document, BindingResult result, ModelMap modelMap) {
        if (document == null) throw new IllegalArgumentException("A document is required");
        if (result.hasErrors()) {
            modelMap.addAttribute("document", document);
            return "documents/create";
        }
        document.persist();
        return "redirect:/documents/" + document.getId();
    }

	@RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(ModelMap modelMap) {
        modelMap.addAttribute("document", new Document());
        return "documents/create";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, ModelMap modelMap) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        modelMap.addAttribute("document", Document.findDocument(id));
        return "documents/show";
    }

	@RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, ModelMap modelMap) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            modelMap.addAttribute("documents", Document.findDocumentEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) Document.countDocuments() / sizeNo;
            modelMap.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            modelMap.addAttribute("documents", Document.findAllDocuments());
        }
        return "documents/list";
    }

	@RequestMapping(method = RequestMethod.PUT)
    public String update(@Valid Document document, BindingResult result, ModelMap modelMap) {
        if (document == null) throw new IllegalArgumentException("A document is required");
        if (result.hasErrors()) {
            modelMap.addAttribute("document", document);
            return "documents/update";
        }
        document.merge();
        return "redirect:/documents/" + document.getId();
    }

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        modelMap.addAttribute("document", Document.findDocument(id));
        return "documents/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        Document.findDocument(id).remove();
        return "redirect:/documents?page=" + ((page == null) ? "1" : page.toString()) + "&size=" + ((size == null) ? "10" : size.toString());
    }

	Converter<Document, String> getDocumentConverter() {
        return new Converter<Document, String>() {
            public String convert(Document document) {
                return new StringBuilder().append(document.getTitle()).append(" ").append(document.getContent()).toString();
            }
        };
    }

	@InitBinder
    void registerConverters(WebDataBinder binder) {
        if (binder.getConversionService() instanceof GenericConversionService) {
            GenericConversionService conversionService = (GenericConversionService) binder.getConversionService();
            conversionService.addConverter(getDocumentConverter());
        }
    }
}
