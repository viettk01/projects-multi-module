package com.nongviet201.cinema.web.sdk.controller.decorator;

import com.nongviet201.cinema.core.entity.cinema.Showtime;
import com.nongviet201.cinema.core.utils.WebFormatter;
import com.nongviet201.cinema.web.sdk.converter.WebShowtimeToResponseConvert;
import com.nongviet201.cinema.web.sdk.response.WebShowtimeResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WebShowtimeDecorator {

    private final WebShowtimeToResponseConvert showtimeConvert;
    private final WebFormatter dateTimeFormatter;

    public WebShowtimeResponse decorate (
            Showtime showtime
    ) {
        return showtimeConvert.convert(
                showtime.getId(),
                dateTimeFormatter.formatDateToEEEEddMM(showtime.getScreeningDate()),
                dateTimeFormatter.formatTimeToHHmm(showtime.getStartTime()),
                showtime.getMovieSchedule().getMovie().getName(),
                showtime.getMovieSchedule().getMovie().getPoster(),
                showtime.getMovieSchedule().getMovie().getAgeRequirement().toString(),
                showtime.getAuditorium().getId(),
                showtime.getAuditorium().getName(),
                showtime.getAuditorium().getAuditoriumType().toString(),
                showtime.getAuditorium().getCinema().getName()
        );
    }
}
