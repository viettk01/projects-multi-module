package com.nongviet201.cinema.admin.sdk.service;

import com.nongviet201.cinema.admin.sdk.request.UpsertMetaDataRequest;
import com.nongviet201.cinema.admin.sdk.request.UpsertMovieRequest;
import com.nongviet201.cinema.core.entity.movie.*;
import com.nongviet201.cinema.core.exception.BadRequestException;
import com.nongviet201.cinema.core.model.enums.movie.AgeRequirement;
import com.nongviet201.cinema.core.model.enums.movie.GraphicsType;
import com.nongviet201.cinema.core.model.enums.movie.TranslationType;
import com.nongviet201.cinema.core.repository.MovieRepository;
import com.nongviet201.cinema.core.service.*;
import com.nongviet201.cinema.core.utils.EntityService;
import com.nongviet201.cinema.core.utils.EnumService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminMovieService {

    private final MovieService movieService;
    private final EnumService enumService;
    private final CountryService countryService;
    private final GenreService genreService;
    private final ActorService actorService;
    private final DirectorService directorService;
    private final MovieRepository movieRepository;
    private final EntityService entityService;

    public void updateDeletedMovie(int id, boolean deleted) {
        Movie movie = movieService.getMovieById(id);
        movie.setDeleted(deleted);
        movieRepository.save(movie);
    }

    public void createMovie(UpsertMovieRequest request) {
        Movie movie = buildMovieFromRequest(request);
        movieRepository.save(movie);
    }

    public void updateMovie(int id, UpsertMovieRequest request) {
        Movie movie = movieService.getMovieById(id);
        updateMovieFromRequest(movie, request);
        movieRepository.save(movie);
    }

    public void createMetaData(
        UpsertMetaDataRequest request
    ) {
        if (request.getType().isEmpty()) {
            throw new BadRequestException("Loại dữ liệu không được để trống");
        }

        switch (request.getType()) {
            case "genre":
                genreService.createGenre(request.getName());
                break;
            case "actor":
                actorService.createActor(request.getName());
                break;
            case "director":
                directorService.createDirector(request.getName());
                break;
            case "country":
                countryService.createCountry(request.getName());
                break;
            default:
                throw new BadRequestException("Loại dữ liệu '" + request.getType() + "' không hợp lệ");
        }
    }

    public void deleteMetaData(
        int id,
        String type
    ) {
        switch (type) {
            case "genre":
                genreService.deleteById(id);
                break;
            case "actor":
                actorService.deleteById(id);
                break;
            case "director":
                directorService.deleteById(id);
                break;
            case "country":
                countryService.deleteById(id);
                break;
            default:
                throw new BadRequestException("Loại dữ liệu '" + type + "' không hợp lệ");
        }
    }

    public void editMetaData(
        int id,
        UpsertMetaDataRequest request
    ) {
        switch (request.getType()) {
            case "genre":
                Genre genre = genreService.getGenreById(id);
                genre.setName(request.getName());
                genreService.save(genre);
                break;
            case "actor":
                Actor actor = actorService.getActorById(id);
                actor.setName(request.getName());
                actorService.save(actor);
                break;
            case "director":
                Director director = directorService.getDirectorById(id);
                director.setName(request.getName());
                directorService.save(director);
                break;
            case "country":
                Country country = countryService.getCountryById(id);
                country.setName(request.getName());
                countryService.save(country);
                break;
            default:
                throw new BadRequestException("Loại dữ liệu '" + request.getType() + "' không hợp lệ");
        }
    }


    private Movie buildMovieFromRequest(
        UpsertMovieRequest request
    ) {
        return Movie.builder()
            .name(request.getName())
            .slug(request.getSlug())
            .description(request.getDescription())
            .poster(request.getPoster())
            .bannerImg(request.getBannerImg())
            .trailer(request.getTrailer())
            .ageRequirement(enumService.getEnumValueByName(AgeRequirement::valueOf, request.getAgeRequirement(), "AgeRequirement"))
            .duration(request.getDuration())
            .producer(request.getProducer())
            .rating(request.getRating())
            .ratingCount(request.getRatingCount())
            .releaseDate(request.getReleaseDate())
            .status(request.isStatus())
            .graphicsTypes(enumService.getEnumListByNames(GraphicsType::valueOf, request.getGraphicTypes(), "GraphicsType"))
            .translationTypes(enumService.getEnumListByNames(TranslationType::valueOf, request.getTranslationTypes(), "TranslationType"))
            .country(entityService.getEntitiesByIds(countryService::getCountryById, request.getCountryIds()))
            .genres(entityService.getEntitiesByIds(genreService::getGenreById, request.getGenreIds()))
            .actors(entityService.getEntitiesByIds(actorService::getActorById, request.getActorIds()))
            .directors(entityService.getEntitiesByIds(directorService::getDirectorById, request.getDirectorIds()))
            .build();
    }

    private void updateMovieFromRequest(
        Movie movie, UpsertMovieRequest request
    ) {
        movie.setName(request.getName());
        movie.setSlug(request.getSlug());
        movie.setDescription(request.getDescription());
        movie.setPoster(request.getPoster());
        movie.setBannerImg(request.getBannerImg());
        movie.setTrailer(request.getTrailer());
        movie.setAgeRequirement(enumService.getEnumValueByName(AgeRequirement::valueOf, request.getAgeRequirement(), "AgeRequirement"));
        movie.setDuration(request.getDuration());
        movie.setProducer(request.getProducer());
        movie.setRating(request.getRating());
        movie.setRatingCount(request.getRatingCount());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setStatus(request.isStatus());
        movie.setGraphicsTypes(enumService.getEnumListByNames(GraphicsType::valueOf, request.getGraphicTypes(), "GraphicsType"));
        movie.setTranslationTypes(enumService.getEnumListByNames(TranslationType::valueOf, request.getTranslationTypes(), "TranslationType"));
        movie.setCountry(entityService.getEntitiesByIds(countryService::getCountryById, request.getCountryIds()));
        movie.setGenres(entityService.getEntitiesByIds(genreService::getGenreById, request.getGenreIds()));
        movie.setActors(entityService.getEntitiesByIds(actorService::getActorById, request.getActorIds()));
        movie.setDirectors(entityService.getEntitiesByIds(directorService::getDirectorById, request.getDirectorIds()));
    }

}
