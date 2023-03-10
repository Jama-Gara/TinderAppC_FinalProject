package com.example.registrationlogindemo.controller;
import org.springframework.http.ResponseEntity;
import com.example.registrationlogindemo.entity.Favorites;
import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.repository.FavoritesRepository;
import com.example.registrationlogindemo.repository.UserRepository;
import com.example.registrationlogindemo.service.FavoritesService;
import com.example.registrationlogindemo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class FavoritesController {
    @Autowired
    private final FavoritesService favoritesService;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final FavoritesRepository favoritesRepository;
    @Autowired
    private final UserService userService;

    @GetMapping("/favorites")
    public String getFavorites(Model model, HttpSession session) {
        Long currentUserId = (Long) session.getAttribute("userId");

        Set<User> users = userService.findAll();
        Set<Long> likedUserIds = favoritesService.findByStatusAndLikedBy("like", currentUserId).stream()
                .map(f -> f.getLikedUser().getId())
                .collect(Collectors.toSet());
        Set<Long> dislikedUserIds = favoritesService.findByStatusAndLikedBy("dislike", currentUserId).stream()
                .map(f -> f.getLikedUser().getId())
                .collect(Collectors.toSet());
        Set<User> likedUsers = favoritesService.findByStatusAndLikedBy("like", currentUserId).stream()
                .map(f -> f.getLikedUser())
                .collect(Collectors.toSet());
        Set<User> dislikedUsers = favoritesService.findByStatusAndLikedBy("dislike", currentUserId).stream()
                .map(f -> f.getLikedUser())
                .collect(Collectors.toSet());

        model.addAttribute("users", users);
        model.addAttribute("likedUserIds", likedUserIds);
        model.addAttribute("dislikedUserIds", dislikedUserIds);
        model.addAttribute("likedUsers", likedUsers);
        model.addAttribute("dislikedUsers", dislikedUsers);
        String username = (String) session.getAttribute("username"); // retrieve the username from the session
        model.addAttribute("username", username);

        return "favorites";
    }

    @PostMapping("/favorites/{status}")
    public String updateFavorites(@RequestParam("userIds[]") List<Long> userIds, @PathVariable String status, Model model, HttpSession session) {

        Long likedBy = (Long) session.getAttribute("userId");

        for (Long userId : userIds) {
            System.out.println(userId);
            Optional<User> likedUser = userService.findById(userId);
            System.out.println(userService.findById(userId));

            User whoLiked = userService.findById(likedBy).get();

            System.out.println(new Favorites(status,whoLiked, likedUser.get()));
            favoritesRepository.save(new Favorites(status, whoLiked, likedUser.get()));
            //favoritesService.saveFavorites(likedBy.getId(), likedUser.get().getId(), status);
            model.addAttribute("receiverId", likedUser.get().getId());
            model.addAttribute("receiver", likedUser.get());
        }
        model.addAttribute("success", "Favorites updated successfully.");
        return "redirect:/favorites";
    }


    @PostMapping("/favorites/remove")
    public String removeFavorite(@RequestParam("userId") Long userId,
                                                 @RequestParam("status") String status,
                                                 HttpSession session) {

        Long currentUserId = (Long) session.getAttribute("userId");

        System.out.println(currentUserId);

        Favorites favorite = favoritesService.findByLikedUserAndLikedBy(userId, currentUserId);
        System.out.println(userId);
        favoritesService.delete(favorite);
        return "redirect:/favorites";
        }
    }




