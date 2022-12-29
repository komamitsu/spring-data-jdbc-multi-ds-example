package org.komamitsu.springtest.data.jdbc.multids.domain.repository;

import org.komamitsu.springtest.data.jdbc.multids.domain.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(transactionManager = "failingTransactionManager")
@Repository
public interface FailingUserRepository extends PagingAndSortingRepository<User, String> {
}
