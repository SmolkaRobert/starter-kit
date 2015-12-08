package pl.spring.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.spring.demo.entity.BookEntity;
import pl.spring.demo.mapper.BookMapper;
import pl.spring.demo.to.AuthorTo;
import pl.spring.demo.to.BookTo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "CommonServiceTest-context.xml")
public class BookMapperTest {

	private long book1Id;
	private String book1Title;
	private long book2Id;
	private String book2Title;
	
	private String firstAuthorFirstName;
	private String firstAuthorLastName;
	private String secondAuthorFirstName;
	private String secondAuthorLastName;

	private List<AuthorTo> authors1;
	private BookEntity bookEntity1;
	private BookTo bookTo1;
	private List<AuthorTo> authors2;
	private BookEntity bookEntity2;
	private BookTo bookTo2;

	@Autowired
	private BookMapper bookMapper;

	@Before
	public void testInitialisation(){
		book1Id = 7L;
		book2Id = 8L;
		book1Title = "Oko czasu";
		book2Title = "Odyseja kosmiczna";
		firstAuthorFirstName = "Arthur";
		firstAuthorLastName = "Clarke";
		secondAuthorFirstName = "Stephen";
		secondAuthorLastName = "Baxter";

		authors1 = new ArrayList<AuthorTo>();
		authors1.add(new AuthorTo(1L, firstAuthorFirstName, firstAuthorLastName));
		authors1.add(new AuthorTo(2L, secondAuthorFirstName, secondAuthorLastName));

		authors2 = new ArrayList<AuthorTo>();
		authors2.add(new AuthorTo(1L, firstAuthorFirstName, firstAuthorLastName));

		bookEntity1 = new BookEntity(book1Id, book1Title, authors1);
		bookTo1 = new BookTo(book1Id, book1Title, firstAuthorFirstName+ " " +  firstAuthorLastName + " " 
				+ secondAuthorFirstName + " " + secondAuthorLastName + " ");

		bookEntity2 = new BookEntity(book2Id, book2Title, authors2);
		bookTo2 = new BookTo(book2Id, book2Title, firstAuthorFirstName+ " " +  firstAuthorLastName + " ");
	}

	@Test
	public void testShouldMapBookEntityIntoBookTo(){
		//given
		//when
		BookTo returnedBook = bookMapper.map(bookEntity1);
		//then
		Assertions.assertThat(returnedBook.getId()).isEqualTo(bookTo1.getId());
		Assertions.assertThat(returnedBook.getTitle()).isEqualTo(bookTo1.getTitle());
		Assertions.assertThat(returnedBook.getAuthors()).isEqualTo(bookTo1.getAuthors());
	}

	@Test
	public void testShouldMapBookToIntoBookEntity(){
		//given
		//when
		BookEntity returnedBook = bookMapper.map(bookTo1);
		//then
		Assertions.assertThat(returnedBook.getId()).isEqualTo(bookTo1.getId());
		Assertions.assertThat(returnedBook.getTitle()).isEqualTo(bookTo1.getTitle());
		bookEntityAuthorsAssertions(bookTo1.getAuthors(), returnedBook.getAuthors());
	}

	private void bookEntityAuthorsAssertions(String bookToAuthors, List<AuthorTo> authorsList) {
		for(AuthorTo singleAuthor : authorsList){
			String firstName = singleAuthor.getFirstName();
			String lastName = singleAuthor.getLastName();
			Assertions.assertThat(bookToAuthors).contains(firstName);
			Assertions.assertThat(bookToAuthors).contains(lastName);
		}
	}

	@Test
	public void testShouldMapBookEntityWithAuthorWithOneName(){
		//given
		authors1 = new ArrayList<AuthorTo>();
		authors1.add(new AuthorTo(1L, firstAuthorFirstName, ""));
		bookEntity1 = new BookEntity(book1Id, book1Title, authors1);
		String expectedAuthor = firstAuthorFirstName + " ";

		//when
		BookTo returnedBook = bookMapper.map(bookEntity1);
		//then
		Assertions.assertThat(returnedBook.getId()).isEqualTo(bookTo1.getId());
		Assertions.assertThat(returnedBook.getTitle()).isEqualTo(bookTo1.getTitle());
		Assertions.assertThat(returnedBook.getAuthors()).isEqualTo(expectedAuthor);
	}

	@Test
	public void testShouldMapListOfBookToIntoListOfBookEntity(){
		//given
		List<BookTo> initialBooks = new ArrayList<BookTo>();
		initialBooks.add(bookTo1);
		initialBooks.add(bookTo2);
		//when
		List<BookEntity> returnedBooks = bookMapper.mapListBookTo2BookEntity(initialBooks);
		//then
		BookEntity firstBook = returnedBooks.get(0);
		BookEntity secondBook = returnedBooks.get(1);
		Assertions.assertThat(firstBook.getId()).isEqualTo(bookTo1.getId());
		Assertions.assertThat(firstBook.getTitle()).isEqualTo(bookTo1.getTitle());
		bookEntityAuthorsAssertions(bookTo1.getAuthors(), firstBook.getAuthors());
		
		Assertions.assertThat(secondBook.getId()).isEqualTo(bookTo2.getId());
		Assertions.assertThat(secondBook.getTitle()).isEqualTo(bookTo2.getTitle());
		bookEntityAuthorsAssertions(bookTo2.getAuthors(), secondBook.getAuthors());
	}
	
	@Test
	public void testShouldMapListOfBookEntityIntoListOfBookTo(){
		//given
		List<BookEntity> initialBooks = new ArrayList<BookEntity>();
		initialBooks.add(bookEntity1);
		initialBooks.add(bookEntity2);
		//when
		List<BookTo> returnedBooks = bookMapper.mapListBookEntity2BookTo(initialBooks);
		//then
		BookTo firstBook = returnedBooks.get(0);
		BookTo secondBook = returnedBooks.get(1);
		Assertions.assertThat(firstBook.getId()).isEqualTo(bookEntity1.getId());
		Assertions.assertThat(firstBook.getTitle()).isEqualTo(bookEntity1.getTitle());
		Assertions.assertThat(firstBook.getAuthors()).isEqualTo(bookTo1.getAuthors());
		
		Assertions.assertThat(secondBook.getId()).isEqualTo(bookEntity2.getId());
		Assertions.assertThat(secondBook.getTitle()).isEqualTo(bookEntity2.getTitle());
		Assertions.assertThat(secondBook.getAuthors()).isEqualTo(bookTo2.getAuthors());
	}

}
