package com.crossover.techtrial.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

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

import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.repositories.TransactionRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TransactionControllerTest {

	  MockMvc mockMvc;
	
	  @Mock
	  private TransactionController transactionController;
	  
	  @Autowired
	  private TestRestTemplate template;
	  
	  @Autowired
	  TransactionRepository transactionRepository;
	  
	  @Before
	  public void setup() throws Exception {
	    mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
	  }
	  
	  @Test
	  public void testTransactionRegistrationSuccessful() throws Exception {
	    HttpEntity<Object> transaction = getHttpEntity(
	        "{\"bookId\": \"6\", \"memberId\": \"2\" }");
	    
	    ResponseEntity<Transaction> response = template.postForEntity(
	        "/api/transaction/",transaction,Transaction.class); 
	    
	    Assert.assertEquals("6", Long.toString(response.getBody().getBook().getId()));
	    Assert.assertEquals("2", Long.toString(response.getBody().getMember().getId()));
	    Assert.assertEquals(200,response.getStatusCode().value());
	    
	    //cleanup the user
	    transactionRepository.deleteById(response.getBody().getId());
	  }
	  
	  @Test
	  public void testTransactionBookNotFound() throws Exception {
	    HttpEntity<Object> transaction = getHttpEntity(
	        "{\"bookId\": \"6000\", \"memberId\": \"1\" }");
	    
	    ResponseEntity<Transaction> response = template.postForEntity(
	        "/api/transaction/",transaction,Transaction.class); 

	    Assert.assertEquals(404,response.getStatusCode().value());

	  }
	  
	  @Test
	  public void testTransactionMemberNotFound() throws Exception {
	    HttpEntity<Object> transaction = getHttpEntity(
	        "{\"bookId\": \"1\", \"memberId\": \"2000\" }");
	    
	    ResponseEntity<Transaction> response = template.postForEntity(
	        "/api/transaction/",transaction,Transaction.class); 

	    Assert.assertEquals(404,response.getStatusCode().value());

	  }
	  
	  @Test
	  public void testTransactionExisting() throws Exception {
	    HttpEntity<Object> transaction = getHttpEntity(
	        "{\"bookId\": \"1\", \"memberId\": \"1\" }");
	    
	    ResponseEntity<Transaction> response = template.postForEntity(
	        "/api/transaction/",transaction,Transaction.class); 

	    Assert.assertEquals(403,response.getStatusCode().value());

	  }
	  
	  
	  private HttpEntity<Object> getHttpEntity(Object body) {
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    return new HttpEntity<Object>(body, headers);
		  }
}
