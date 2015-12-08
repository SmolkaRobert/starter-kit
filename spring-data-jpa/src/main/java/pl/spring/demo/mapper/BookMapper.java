package pl.spring.demo.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import pl.spring.demo.entity.BookEntity;
import pl.spring.demo.to.AuthorTo;
import pl.spring.demo.to.BookTo;

@Component
public class BookMapper {

	public BookTo map(BookEntity bookEntity){
		String authors = "";

		for(AuthorTo singleAuthor: bookEntity.getAuthors()){
			authors += singleAuthor.getFirstName() + " ";
			if(!singleAuthor.getLastName().isEmpty()){
				authors += singleAuthor.getLastName() + " ";
			}
		}

		return new BookTo(bookEntity.getId(), bookEntity.getTitle(), authors);
	}

	public BookEntity map(BookTo bookTo){
		List<AuthorTo> authorsList = new ArrayList<AuthorTo>();
		Long authorId = 1L;

		String allAuthors = bookTo.getAuthors();

		for(String singleAuthor : Arrays.asList(allAuthors.split("(?<!\\G\\S+)\\s"))){
			String firstName = "";
			String lastName = "";
			String[] authorNames = singleAuthor.split("\\s");
			firstName = authorNames[0];

			if(authorNames.length > 1){
				lastName = authorNames[1];
			}
			authorsList.add(new AuthorTo(authorId, firstName, lastName));
			authorId++;
		}

		return new BookEntity(bookTo.getId(), bookTo.getTitle(), authorsList);
	}

	public List<BookEntity> mapListBookTo2BookEntity(List<BookTo> bookTo){
		List<BookEntity> bookEntity = new ArrayList<BookEntity>();

		for(BookTo singleBookTo : bookTo){
			bookEntity.add(map(singleBookTo));
		}
		return bookEntity;
	}

	public List<BookTo> mapListBookEntity2BookTo(List<BookEntity> bookEntity){
		List<BookTo> bookTo = new ArrayList<BookTo>();

		for(BookEntity singleBookEntity : bookEntity){
			bookTo.add(map(singleBookEntity));
		}
		return bookTo;
	}
}
