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

        // Verifica se a categoria já existe para o usuário
        List<String> distinctCategories = financeRepository.findDistinctCategoriaByUser(user);
        boolean categoriaExiste = distinctCategories.contains(finance.getCategoria());

        if (!categoriaExiste) {
            // Se a categoria não existe e o usuário é FREE com 5 categorias distintas, não permite criar nova categoria
            if (user.getUserType() == UserType.FREE && distinctCategories.size() >= 5) {
                throw new Exception("Usuário FREE não pode ter mais de 5 categorias diferentes.");
            }
        }

        // Caso a categoria já exista ou o limite não tenha sido atingido, permite a criação da finança
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
