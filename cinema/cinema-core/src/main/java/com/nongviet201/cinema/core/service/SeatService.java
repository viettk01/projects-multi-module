package com.nongviet201.cinema.core.service;

import com.nongviet201.cinema.core.entity.cinema.Seat;

import java.util.List;

public interface SeatService {
    List<Seat> getAllSeatsByAuditoriumIdOrderBySeatRowAsc(int auditoriumId);

    List<Seat> getAllSeatDeletedByAuditoriumIdOrderBySeatRowAsc(int audId);

    Seat getSeatById(int seatId);

    void save(Seat seat);

    void delete(int id);
}
