package fr.soat.festival.infra.spectator;

import fr.soat.festival.domain.spectator.AccountRepository;
import fr.soat.festival.domain.spectator.model.Account;
import fr.soat.festival.domain.spectator.model.SpectatorId;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class AccountDbRepository implements AccountRepository {

    private final JdbcTemplate jdbcTemplate;

    public AccountDbRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void create(Account account) {
        jdbcTemplate.update("INSERT INTO account " +
                            "(spectator_id, balance) " +
                            "VALUES  (?, ?)",
                account.getSpectatorId(),
                account.getBalance());
    }

    @Override
    public void update(Account account) {
        jdbcTemplate.update("UPDATE  account " +
                            "SET balance = ? " +
                            "WHERE spectator_id = ?",
                account.getBalance(),
                account.getSpectatorId()
        );
    }

    @Override
    public Account getOne(SpectatorId spectatorId) {
        return jdbcTemplate.queryForObject("SELECT spectator_id, balance " +
                                           "FROM account " +
                                           "WHERE spectator_id = ?",
                new Object[]{spectatorId.getIdValue()},
                (resultSet, i) -> new Account(
                        resultSet.getString("spectator_id"),
                        resultSet.getInt("balance")
                ));
    }
}
