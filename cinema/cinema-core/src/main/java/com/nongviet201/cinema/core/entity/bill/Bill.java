package com.nongviet201.cinema.core.entity.bill;

import com.nongviet201.cinema.core.entity.cinema.Cinema;
import com.nongviet201.cinema.core.entity.user.User;
import com.nongviet201.cinema.core.entity.cinema.Showtime;
import com.nongviet201.cinema.core.model.enums.bill.BillStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    private BillStatus status;
    private long discount;
    private long totalPrice;
    private String barcode;
    private int points;

    private LocalDateTime paymentAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name= "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "showtime_id")
    private Showtime showtime;

    @ManyToOne
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;

}
