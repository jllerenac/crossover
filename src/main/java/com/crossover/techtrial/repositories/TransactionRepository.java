/**
 * 
 */
package com.crossover.techtrial.repositories;

import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.stream.*;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.jpa.repository.*;

/**
 * @author crossover
 *
 */
@RestResource(exported = false)
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    @Query("select id from transaction t")
	Optional<Transaction> findTransaction(Long id);
 
}
