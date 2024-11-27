package com.stripe.stripe_payments.repositories;

import com.stripe.stripe_payments.common.entities.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByCustomerId(String customerId);
}
