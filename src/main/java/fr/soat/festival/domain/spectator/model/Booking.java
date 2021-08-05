package fr.soat.festival.domain.spectator.model;

import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.place.model.PlaceId;
import lombok.Value;

public interface Booking {

    Artist getArtist();
    PlaceId getPlaceId();

    @Value
    class RegisteredBooking implements Booking {
        Artist artist;
        PlaceId placeId;
    }

    @Value
    class RejectedBooking implements Booking {
        Artist artist;

        @Override
        public PlaceId getPlaceId() {
            throw new UnsupportedOperationException("This booking attempt has been rejected, there is no placeId");
        }
    }

}
