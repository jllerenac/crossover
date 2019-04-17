
package com.crossover.techtrial.service;

import java.util.List;
import com.crossover.techtrial.model.Transaction;

public interface TransactionService {
  
  public Transaction findTransaction(Long id_book);
  
 // public Transaction findTransactionByBookMember(Long id_book, Long id_member);
 
}