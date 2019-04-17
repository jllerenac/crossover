/**
 * 
 */
package com.crossover.techtrial.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.repositories.BookRepository;
import com.crossover.techtrial.repositories.MemberRepository;
import com.crossover.techtrial.repositories.TransactionRepository;
import com.crossover.techtrial.service.MemberService;
import com.crossover.techtrial.service.TransactionService;
/**
 * @author kshah
 *
 */
@RestController
public class TransactionController {
  
  @Autowired TransactionService transactionService;
	
  @Autowired TransactionRepository transactionRepository;
  
  @Autowired BookRepository bookRepository;
  
  @Autowired MemberRepository memberRepository;
  /*
   * PLEASE DO NOT CHANGE SIGNATURE OR METHOD TYPE OF END POINTS
   * Example Post Request :  { "bookId":1,"memberId":33 }
   */
@PostMapping(path = "/api/transaction")
  public ResponseEntity<Transaction> issueBookToMember(@RequestBody Map<String, Long> params){
    Long bookId = params.get("bookId");
    Long memberId = params.get("memberId");
    Optional<Book> checkBook = bookRepository.findById(bookId);
    if (!checkBook.isPresent()){
    	System.out.println("---------- CROSSOVER MESSAGE: Book does not exist ----------"); 
    	return ResponseEntity.status(404).build();
    }
    Optional<Member> checkMember = memberRepository.findById(memberId);
    if (!checkMember.isPresent()){
    	System.out.println("---------- CROSSOVER MESSAGE: Member does not exist ----------"); 
    	return ResponseEntity.status(404).build();
    }
    Transaction existingt = transactionService.findTransaction(bookId);
    if (existingt != null) { 
    	System.out.println("---------- CROSSOVER MESSAGE: Book is already issued to another customer ----------"); 
    	return ResponseEntity.status(403).build();
    	}
    if (transactionRepository.findMoreThanFiveTransactions(memberId) >= 5) {
    	System.out.println("---------- CROSSOVER MESSAGE: Member has more than 5 books issued ----------"); 
    	return ResponseEntity.status(403).build();
    }
    Transaction transaction = new Transaction();
    // this is to check if the same book has been issued before to the same member
    // if so, we update date_of_issue, so we wont create extra records in table
 //   Optional<Transaction> existingtr = transactionRepository.findExistingTransaction(bookId, memberId);
 //   if (existingtr.isPresent()) { 
 //   		transaction.setId(existingtr.get().getId());
 //   	}
 // if the 4 preceding lines are commented is because we want to create new record when issuing
 // a book that has already been issued and returned with the same member
    transaction.setBook(bookRepository.findById(bookId).orElse(null));
    transaction.setMember(memberRepository.findById(memberId).get());
    transaction.setDateOfIssue(LocalDateTime.now());    
    return ResponseEntity.ok().body(transactionRepository.save(transaction));
  }
  /*
   * PLEASE DO NOT CHANGE SIGNATURE OR METHOD TYPE OF END POINTS
   */
  @PatchMapping(path= "/api/transaction/{transaction-id}/return")
  public ResponseEntity<Transaction> returnBookTransaction(@PathVariable(name="transaction-id") Long transactionId){
    Optional<Transaction> transaction = transactionRepository.findById(transactionId);
    if (!transaction.isPresent()) {
    	System.out.println("---------- CROSSOVER MESSAGE: Transaction does not exist ----------"); 
    	return ResponseEntity.status(404).build();
    	 }
    Transaction updtransaction = transaction.get() ; 
    if (updtransaction.getDateOfReturn() != null ) {
    	System.out.println("---------- CROSSOVER MESSAGE: Book already returned ----------"); 
    	return ResponseEntity.status(403).build();
    }
    updtransaction.setDateOfReturn(LocalDateTime.now());
    return ResponseEntity.ok().body(transactionRepository.save(updtransaction));
  }

}
