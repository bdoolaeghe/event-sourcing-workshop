package fr.soat.festival.domain.spectator.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class Account {
    String spectatorId;
    int balance;
}
