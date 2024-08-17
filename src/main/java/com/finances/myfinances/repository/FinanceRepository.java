package com.finances.myfinances.repository;

import com.finances.myfinances.model.Finance;
import com.finances.myfinances.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinanceRepository extends JpaRepository<Finance, Long> {

    // Conta o número de categorias distintas que o usuário possui
    int countDistinctByUserAndCategoria(User user, String categoria);
    List<Finance> findAllByUser(User user);
    Optional<Finance> findByIdAndUser(Long id, User user);
    @Query("SELECT DISTINCT f.categoria FROM Finance f WHERE f.user = :user")
    List<String> findDistinctCategoriaByUser(User user);
}
