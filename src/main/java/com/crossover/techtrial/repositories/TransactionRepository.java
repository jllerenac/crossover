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
    @Query(
    value="select id from crosslibrary.transaction t where book_id = ?1 and date_of_return is null;", 
    nativeQuery = true)
	Optional<Transaction> findTransaction(Long id_book);
 
}
