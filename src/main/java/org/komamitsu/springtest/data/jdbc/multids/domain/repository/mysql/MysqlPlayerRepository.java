package org.komamitsu.springtest.data.jdbc.multids.domain.repository.mysql;

import org.komamitsu.springtest.data.jdbc.multids.domain.model.Player;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface MysqlPlayerRepository extends PagingAndSortingRepository<Player, String> {
}
