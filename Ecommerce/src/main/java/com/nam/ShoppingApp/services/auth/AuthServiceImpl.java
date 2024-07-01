package com.nam.ShoppingApp.services.auth;

import com.nam.ShoppingApp.dto.SignupRequest;
import com.nam.ShoppingApp.dto.UserDao;
import com.nam.ShoppingApp.entity.Order;
import com.nam.ShoppingApp.entity.User;
import com.nam.ShoppingApp.enums.OrderStatus;
import com.nam.ShoppingApp.enums.UserRole;
import com.nam.ShoppingApp.exception.AppException;
import com.nam.ShoppingApp.exception.ErrorCode;
import com.nam.ShoppingApp.repository.OrderRepository;
import com.nam.ShoppingApp.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  @Autowired private UserRepository userRepository;

  @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired private OrderRepository orderRepository;

  @Override
  public UserDao createUser(SignupRequest signupRequest) {
    User user = new User();

    user.setEmail(signupRequest.getEmail());
    user.setPassword(bCryptPasswordEncoder.encode(signupRequest.getPassword()));
    user.setName(signupRequest.getName());
    user.setRole(UserRole.CUSTOMER);

    User savedUser;

    try {
      savedUser = userRepository.save(user);
    } catch (DataIntegrityViolationException e) {
      throw new AppException(ErrorCode.USER_EXISTED);
    }

    Order order = new Order();
    order.setUser(savedUser);
    order.setAmount(0L);
    order.setTotalAmount(0L);
    order.setDiscount(0L);
    order.setOrderStatus(OrderStatus.PENDING);
    orderRepository.save(order);

    UserDao userDao = new UserDao();
    userDao.setId(savedUser.getId());
    userDao.setEmail(savedUser.getEmail());
    userDao.setName(savedUser.getName());
    return userDao;
  }

  @Override
  public boolean hasUserWithEmail(String email) {
    return userRepository.findByEmail(email).isPresent();
  }

  @PostConstruct
  public void createAdminAccount() {
    User adminAccount = userRepository.findByRole(UserRole.ADMIN).orElse(null);
    if (adminAccount == null) {
      User admin = new User();
      admin.setEmail("admin@gmail.com");
      admin.setPassword(bCryptPasswordEncoder.encode("admin"));
      admin.setName("admin");
      admin.setRole(UserRole.ADMIN);
      userRepository.save(admin);
    }
  }
}
