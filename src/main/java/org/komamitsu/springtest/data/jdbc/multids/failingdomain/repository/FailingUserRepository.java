package org.komamitsu.springtest.data.jdbc.multids.failingdomain.repository;

import org.komamitsu.springtest.data.jdbc.multids.domain.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface FailingUserRepository extends PagingAndSortingRepository<User, String> {
}
