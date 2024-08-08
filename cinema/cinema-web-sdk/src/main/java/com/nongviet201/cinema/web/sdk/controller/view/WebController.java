package com.nongviet201.cinema.web.sdk.controller.view;

import com.nongviet201.cinema.core.service.AuthService;
import com.nongviet201.cinema.core.service.MovieService;
import com.nongviet201.cinema.core.service.PostService;
import com.nongviet201.cinema.core.service.ShowtimeService;
import com.nongviet201.cinema.web.sdk.controller.service.WebBillControllerService;
import com.nongviet201.cinema.web.sdk.controller.service.WebUserControllerService;
import com.nongviet201.cinema.web.sdk.controller.service.WebVerifyService;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.stream.Collectors;

@AllArgsConstructor
public abstract class WebController {
    private final MovieService movieService;
    private final ShowtimeService showtimeService;
    private final WebBillControllerService billControllerService;
    private final WebVerifyService verifyService;
    private final PostService postService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute(
            "movies",
            movieService.getAllPublishMoviesOrderByReleaseDate()
        );
        model.addAttribute(
            "posts",
            postService.getAllPublishPostsOrderByUpdatedAt()
                .stream()
                .limit(4)
                .collect(Collectors.toList())
        );
        return "index";
    }

    @GetMapping("/movie/{id}")
    public String infoMoviePage(
        @PathVariable("id") int id,
        Model model
    ) {
        model.addAttribute(
            "movie",
            movieService.getPublishMovieById(id)
        );
        model.addAttribute(
            "showtimes",
            showtimeService.getAllShowtimesByMovieIdAnDate(id, LocalDate.now())
        );
        return "movie/detail";
    }

    @GetMapping("/booking")
    public String getBookingPage(
            @RequestParam(value = "showtime", required = false) Integer showtimeId,
            @RequestParam(value = "vnp_TxnRef", required = false) Integer bill,
            @RequestParam(value = "vnp_ResponseCode", required = false) String code,
            Model model
    ) {
        if (showtimeId!= null) {
            model.addAttribute(
                "showtime",
                showtimeService.getShowtimeById(showtimeId)
            );
        }
        if (bill!= null) {
            if (code.equals("00")) {
                model.addAttribute(
                    "code",
                    code
                );
                model.addAttribute(
                    "bill",
                    billControllerService.updateBill(bill)
                );
            } else {
                model.addAttribute(
                    "code",
                    code
                );
                model.addAttribute(
                    "bill",
                    billControllerService.getBillById(bill)
                );
            }
        }
        return "booking/booking";
    }

    @GetMapping("/posts")
    public String getBlogsPage(
        @RequestParam(value = "id", required = false) Integer id,
        Model model
    ) {
        model.addAttribute(
            "posts",
            postService.getAllPublishPostsOrderByUpdatedAt()
        );
        if (id!= null) {
            model.addAttribute(
                "post",
                postService.getPostById(id)
            );
            return "post/detail";
        }
        return "post/index";
    }


    @GetMapping("/verification")
    public String getVerificationPage(
        @RequestParam(value = "token", required = false) String token,
        @RequestParam(value = "tokenForgotPassword", required = false) String tokenForgotPassword,
        Model model
    ) {
        if (token!= null) {
            model.addAttribute(
                "verifyResponse",
                verifyService.verifyRegister(token)
            );
        }
        if (tokenForgotPassword!= null) {
            if (verifyService.verifyForgotPassword(tokenForgotPassword)) {
                model.addAttribute(
                    "tokenForgotPassword",
                    tokenForgotPassword
                );
                return "pages/verify";
            }
        }
        return "pages/verify";
    }

    @GetMapping("/user")
    public String getUserPage(
        @RequestParam(value = "stage", defaultValue = "2") Integer stage,
        Model model
    ) {
        model.addAttribute(
            "stage",
            stage
        );
        return "user/user";
    }
}
