package fr.soat.festival.domain;

import fr.soat.festival.domain.concert.ConcertRepository;
import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.concert.model.Concert;
import fr.soat.festival.domain.place.PlaceRepository;
import fr.soat.festival.domain.place.model.Place;
import fr.soat.festival.domain.place.model.PlaceId;
import fr.soat.festival.domain.spectator.AccountRepository;
import fr.soat.festival.domain.spectator.SpectatorRepository;
import fr.soat.festival.domain.spectator.model.Account;
import fr.soat.festival.domain.spectator.model.Booking;
import fr.soat.festival.domain.spectator.model.Spectator;
import fr.soat.festival.domain.spectator.model.SpectatorId;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class BookingService {

    private final ConcertRepository concertRepository;
    private final PlaceRepository placeRepository;
    private final SpectatorRepository spectatorRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public Booking book(Artist artist, SpectatorId spectatorId) {
        // lookup an available place
        Concert concert = concertRepository.load(artist);
        Spectator spectator = spectatorRepository.load(spectatorId);
        if (concert.isFull()) {
            spectator = spectator.rejectBooking(artist);
            spectatorRepository.save(spectator);
        } else {
            PlaceId placeId = concert.getAnAvailablePlaceId();
            concert = concert.book(placeId);
            concertRepository.save(concert);

            Place place = placeRepository.load(placeId);
            place = place.assignTo(spectatorId);
            placeRepository.save(place);

            spectator = spectator.registerBooking(place.getId(), place.getArtist());
            spectatorRepository.save(spectator);
            Account account = accountRepository.getOne(spectatorId);
            account.setBalance(account.getBalance() - place.getPrice());
            accountRepository.update(account);
        }
        return spectator.getBooking(artist);
    }

    @Transactional
    public void cancelBooking(PlaceId placeId) {
        // 1. update the place status (make AVAILABLE), remove assigne
        Place place = placeRepository.load(placeId);
        SpectatorId assignee = place.getAssignee();
        place = place.cancelAssignment();
        placeRepository.save(place);

        // 2. update the spectator bookings
        Spectator spectator = spectatorRepository.load(assignee);
        spectator = spectator.cancelBooking(place.getArtist());
        spectatorRepository.save(spectator);

        // update the spectator account
        Account account = accountRepository.getOne(spectator.getId());
        account.setBalance(account.getBalance() + place.getPrice());
        accountRepository.update(account);

        // 3. update the concert (make the place available back) + update status
        Concert concert = concertRepository.load(place.getArtist());
        concert = concert.cancelBooking(placeId);
        concertRepository.save(concert);
    }

}
