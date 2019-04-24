package com.crossover.techtrial.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.repositories.BookRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

	  MockMvc mockMvc;
	  
	  @Mock
	  private BookController bookController;
	
	  @Autowired
	  private TestRestTemplate template;
	  
	  @Autowired
	  BookRepository bookRepository;
	  
	  @Before
	  public void setup() throws Exception {
	    mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
	  }
	  
	  @Test
	  public void testBookRegistrationsuccessful() throws Exception {
	    HttpEntity<Object> book = getHttpEntity(
	        "{\"title\": \"book1\"}");
	    
	    ResponseEntity<Book> response = template.postForEntity(
	        "/api/book",book,Book.class); 
	    
	    Assert.assertEquals("book1", response.getBody().getTitle());
	    Assert.assertEquals(200,response.getStatusCode().value());
	    
	    //cleanup the user
	    bookRepository.deleteById(response.getBody().getId());
	  }
	  
	  private HttpEntity<Object> getHttpEntity(Object body) {
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    return new HttpEntity<Object>(body, headers);
		  }
}
