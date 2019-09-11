package fr.soat.conference.infra.statistics;

import fr.soat.conference.domain.booking.ConferenceName;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static java.util.Collections.synchronizedMap;

@Repository
public class StatisticsRepository {

    private final Map<ConferenceName, Integer> conferenceToBookings = synchronizedMap(new HashMap<>());
    private final Map<ConferenceName, Integer> conferenceToIncomes = synchronizedMap(new HashMap<>());

    public void increaseBookingNumber(ConferenceName conferenceName) {
        Integer currentBookings = getBookingNumber(conferenceName);
        conferenceToBookings.put(conferenceName, currentBookings + 1);
    }

    public void decreaseBookingNumber(ConferenceName conferenceName) {
        Integer currentBookings = getBookingNumber(conferenceName);
        conferenceToBookings.put(conferenceName, currentBookings - 1);
    }

    public Integer getBookingNumber(ConferenceName conferenceName) {
        return conferenceToBookings.getOrDefault(conferenceName, 0);
    }

    public void increaseIncomes(ConferenceName conferenceName, int income) {
        Integer currentIncomes = getIncomes(conferenceName);
        conferenceToIncomes.put(conferenceName, currentIncomes + income);
    }

    public Integer getIncomes(ConferenceName conferenceName) {
        return conferenceToIncomes.getOrDefault(conferenceName, 0);
    }

    public Collection<ConferenceName> getConferences() {
        HashSet<ConferenceName> conferenceNames = new HashSet<>(conferenceToBookings.keySet());
        conferenceNames.addAll(conferenceToIncomes.keySet());
        return conferenceNames;
    }

    public void clear() {
        conferenceToIncomes.clear();;
        conferenceToBookings.clear();;
    }
}
