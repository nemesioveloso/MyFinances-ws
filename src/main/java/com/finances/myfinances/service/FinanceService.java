package com.finances.myfinances.service;

import com.finances.myfinances.model.Finance;
import com.finances.myfinances.model.User;
import com.finances.myfinances.model.UserType;
import com.finances.myfinances.repository.FinanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinanceService {

    @Autowired
    private FinanceRepository financeRepository;

    public List<String> getAllCategoriesByUser(User user) {
        return financeRepository.findAllByUser(user)
                .stream()
                .map(Finance::getCategoria)
                .distinct()
                .collect(Collectors.toList());
    }

    public Finance createFinance(Finance finance) throws Exception {
        User user = finance.getUser();

        // Obtém todas as categorias distintas do usuário
        List<String> distinctCategories = financeRepository.findDistinctCategoriaByUser(user);

        // Verifica se o usuário é FREE e se já possui 4 categorias diferentes
        if (user.getUserType() == UserType.FREE && distinctCategories.size() >= 5) {
            throw new Exception("Usuário FREE não pode ter mais de 5 categorias diferentes.");
        }
        return financeRepository.save(finance);
    }

    public List<Finance> getAllFinancesByUser(User user) {
        return financeRepository.findAllByUser(user);
    }

    public Finance getFinanceByIdAndUser(Long id, User user) {
        return financeRepository.findByIdAndUser(id, user).orElse(null);
    }

    public Finance updateFinance(Finance finance) {
        return financeRepository.save(finance);
    }

    public void deleteFinance(Finance finance) {
        financeRepository.delete(finance);
    }
}
