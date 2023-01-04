package org.komamitsu.springtest.data.jdbc.multids.domain.repository.postgresql;

import org.komamitsu.springtest.data.jdbc.multids.domain.model.Player;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface PostgresqlPlayerRepository extends PagingAndSortingRepository<Player, String> {
}
