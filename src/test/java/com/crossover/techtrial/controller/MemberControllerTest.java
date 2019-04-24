/**
 * 
 */
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
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.repositories.MemberRepository;

/**
 * @author kshah
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MemberControllerTest {
  
  MockMvc mockMvc;
  
  @Mock
  private MemberController memberController;
  
  @Autowired
  private TestRestTemplate template;
  
  @Autowired
  MemberRepository memberRepository;
  
  @Before
  public void setup() throws Exception {
    mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
  }
  
  @Test
  public void testMemberRegistrationSuccessful() throws Exception {
    HttpEntity<Object> member = getHttpEntity(
        "{\"name\": \"test\", \"email\": \"test01@gmail.com\"," 
            + " \"membershipStatus\": \"ACTIVE\",\"membershipStartDate\":\"2018-08-08T12:12:12\" }");
    
    ResponseEntity<Member> response = template.postForEntity(
        "/api/member",member,Member.class); 
    
    Assert.assertEquals("test", response.getBody().getName());
    Assert.assertEquals(200,response.getStatusCode().value());
    
    //cleanup the user
    memberRepository.deleteById(response.getBody().getId());
  }

  @Test
  public void testMemberWrongFirstCharacterValidation() throws Exception {
	  HttpEntity<Object> member = getHttpEntity(
		        "{\"name\": \"1Jose Llerena\", \"email\": \"test01@gmail.com\"," 
		            + " \"membershipStatus\": \"ACTIVE\",\"membershipStartDate\":\"2018-08-08T12:12:12\" }");
    
    ResponseEntity<Member> response = template.postForEntity(
        "/api/member/",member,Member.class); 
    
    Assert.assertEquals(400,response.getStatusCode().value());
    
  }
  
  @Test
  public void testMemberShortNameValidation() throws Exception {
	  HttpEntity<Object> member = getHttpEntity(
		        "{\"name\": \"J\", \"email\": \"test01@gmail.com\"," 
		            + " \"membershipStatus\": \"ACTIVE\",\"membershipStartDate\":\"2018-08-08T12:12:12\" }");
    
    ResponseEntity<Member> response = template.postForEntity(
        "/api/member/",member,Member.class); 
    
    Assert.assertEquals(400,response.getStatusCode().value());
    
  }
  
  @Test
  public void testMemberDuplicatedEmail() throws Exception {
      HttpEntity<Object> memberOne = getHttpEntity(
              "{\"name\": \"Jose Llerena\", \"email\": \"josellerenacarpio@gmail.com\","
                      + " \"membershipStatus\": \"ACTIVE\",\"membershipStartDate\":\"2019-01-15T15:18:45\" }");

      HttpEntity<Object> memberTwo = getHttpEntity(
              "{\"name\": \"Jose Alfredo Llerena\", \"email\": \"josellerenacarpio@gmail.com\","
                      + " \"membershipStatus\": \"ACTIVE\",\"membershipStartDate\":\"2019-01-15T15:19:45\" }");

      ResponseEntity<Member> responseOne= template.postForEntity("/api/member", memberOne, Member.class);
      Assert.assertEquals("Jose Llerena", responseOne.getBody().getName());
      Assert.assertEquals(200, responseOne.getStatusCode().value());

      ResponseEntity<Member> responseTwo = template.postForEntity("/api/member", memberTwo, Member.class);
      Assert.assertEquals(200, responseTwo.getStatusCode().value());

      //cleanup the user
      memberRepository.deleteById(responseOne.getBody().getId());
  }
  
  @Test
  public void testDateIssuedLaterThanReturnedDate() throws Exception {
    
    ResponseEntity<Member[]> response = template.getForEntity(
        "/api/member/top-member?startTime=2019-01-21T09:25:12&endTime=2019-01-11T09:25:12",Member[].class); 
    
    Assert.assertEquals(400,response.getStatusCode().value());
    
  }
  
  private HttpEntity<Object> getHttpEntity(Object body) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new HttpEntity<Object>(body, headers);
  }

}
