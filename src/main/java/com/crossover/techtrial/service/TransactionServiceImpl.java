package com.crossover.techtrial.service;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.repositories.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	  TransactionRepository transactionRepository;
	
	  public Transaction findTransaction(Long id_book) {
		    Optional<Transaction> optionalTransaction = transactionRepository.findTransaction(id_book);
		    if (optionalTransaction.isPresent()) {
		      return optionalTransaction.get();
		    }else return null;
		  }
	
}
