package com.nam.ShoppingApp.services.jwt;

import com.nam.ShoppingApp.entity.User;
import com.nam.ShoppingApp.repository.UserRepository;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByEmail(username);

    if (user.isEmpty())
      throw new UsernameNotFoundException("User not found with username: " + username);
    return new org.springframework.security.core.userdetails.User(
        user.get().getEmail(), user.get().getPassword(), new ArrayList<>());
  }
}
