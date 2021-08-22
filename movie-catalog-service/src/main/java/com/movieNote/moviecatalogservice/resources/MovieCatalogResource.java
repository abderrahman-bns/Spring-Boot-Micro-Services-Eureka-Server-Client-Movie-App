package com.movieNote.moviecatalogservice.resources;

import com.movieNote.moviecatalogservice.models.CatalogItem;
import com.movieNote.moviecatalogservice.models.Movie;
import com.movieNote.moviecatalogservice.models.Rating;
import com.movieNote.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

        UserRating ratings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" + userId, UserRating.class);

        return ratings.getUserRatings().stream().map( rating -> {
            // for each movie id , call movie info service and get details
            Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
            // put them all together
            return new CatalogItem(movie.getName(), "desc", rating.getRating());
        }).collect(Collectors.toList());

    }
}


//    @Autowired
//    private WebClient.Builder webClientBuilder;

//            Movie movie = webClientBuilder.build()
//                    .get()
//                    .uri("http://localhost:8082/movies/" + rating.getMovieId())
//                    .retrieve()
//                    .bodyToMono(Movie.class)
//                    .block();