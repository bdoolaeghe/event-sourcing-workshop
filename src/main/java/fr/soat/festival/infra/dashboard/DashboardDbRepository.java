package fr.soat.festival.infra.dashboard;

import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.dashboard.DashboardRepository;
import fr.soat.festival.domain.dashboard.model.ConcertDashboard;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class DashboardDbRepository implements DashboardRepository {

    private final JdbcTemplate jdbcTemplate;

    public DashboardDbRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void createConcertDashboard(Artist artist) {
        jdbcTemplate.update("INSERT INTO concert_dashboard " +
                            "(artist) " +
                            "VALUES  (?) " +
                            "ON CONFLICT DO NOTHING ",
                artist.getName()
        );
    }

    @Override
    public void updateBookingRatio(Artist artist, int newBookingRatio) {
        jdbcTemplate.update("UPDATE concert_dashboard " +
                            " SET booking_ratio = ? " +
                            "WHERE artist = ?",
                newBookingRatio,
                artist.getName()
        );
    }

    @Override
    public void addIncomes(Artist artist, int incomesToAdd) {
        jdbcTemplate.update("UPDATE concert_dashboard " +
                            "SET booking_incomes_total = booking_incomes_total + ? " +
                            "WHERE artist = ?",
                incomesToAdd,
                artist.getName()
        );
    }

    @Override
    public void removeIncomes(Artist artist, int incomesToRemove) {
        jdbcTemplate.update("UPDATE concert_dashboard " +
                            "SET booking_incomes_total = booking_incomes_total - ? " +
                            "WHERE artist = ?",
                incomesToRemove,
                artist.getName()
        );
    }

    @Override
    public ConcertDashboard getConcertDashboard(Artist artist) {
        return jdbcTemplate.queryForObject("SELECT artist," +
                                           "           booking_ratio, " +
                                           "           booking_incomes_total " +
                                           "FROM concert_dashboard " +
                                           "WHERE artist = ?",
                new Object[]{artist.getIdValue()},
                (resultSet, i) -> new ConcertDashboard(
                        Artist.named(resultSet.getString("artist")),
                        resultSet.getInt("booking_ratio") + "%",
                        resultSet.getInt("booking_incomes_total") + " euro"
                ));
    }
}
