package pl.spring.demo.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.spring.demo.service.BookService;
import pl.spring.demo.to.BookTo;

import java.util.List;
import java.util.Map;

@Controller
public class BookController {
    @Autowired
    private BookService bookService;
    
    @RequestMapping(value = "/books", method = RequestMethod.GET)
    public String bookList(Map<String, Object> params) {
        final List<BookTo> allBooks = bookService.findAllBooks();
        params.put("books", allBooks);
        return "bookList";
    }
    
    @RequestMapping(value = "/deleteBook", method = RequestMethod.POST)
    public String deleteBook(@RequestParam("id") long id, Map<String, Object> params) {
    	BookTo bookToBeDeleted = bookService.getBookByID(id);
    	bookService.deleteBookByID(id);
    	params.put("bookDeleted", bookToBeDeleted);
    	return "bookDeletionConfirmation";
    }
}