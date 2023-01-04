package org.komamitsu.springtest.data.jdbc.multids.domain.repository.mysql;

import org.komamitsu.springtest.data.jdbc.multids.domain.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface MysqlUserRepository extends PagingAndSortingRepository<User, String> {
}
