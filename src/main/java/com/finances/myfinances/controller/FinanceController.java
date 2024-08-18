package com.finances.myfinances.controller;

import com.finances.myfinances.model.Finance;
import com.finances.myfinances.model.User;
import com.finances.myfinances.service.FinanceService;
import com.finances.myfinances.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/finances")
public class FinanceController {

    @Autowired
    private FinanceService financeService;

    @Autowired
    private UserService userService;

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        // Recupera o usuário autenticado
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());

        // Obtém todas as categorias associadas ao usuário
        List<String> categories = financeService.getAllCategoriesByUser(user);

        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<?> createFinance(@RequestBody Finance finance) {
        try {
            // Recupera o usuário autenticado
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userService.getUserByUsername(userDetails.getUsername());

            // Associa a finança ao usuário autenticado
            finance.setUser(user);

            // Cria a nova finança
            Finance createdFinance = financeService.createFinance(finance);
            return ResponseEntity.ok("Finança adicionada com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            // Aqui retornamos a mensagem de erro específica que foi lançada
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllFinances(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // Recupera o usuário autenticado
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());

        // Define a paginação e ordenação padrão
        Pageable pageable = PageRequest.of(page, size);

        // Obtém as finanças associadas ao usuário com paginação
        Page<Finance> financePage = financeService.getAllFinancesByUser(user, pageable);

        // Prepara a resposta
        Map<String, Object> response = new HashMap<>();
        response.put("totalElements", financePage.getTotalElements());
        response.put("totalPages", financePage.getTotalPages());
        response.put("page", financePage.getNumber());
        response.put("size", financePage.getSize());
        response.put("list", financePage.getContent());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFinance(@PathVariable Long id, @RequestBody Finance financeDetails) {
        try {
            // Recupera o usuário autenticado
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userService.getUserByUsername(userDetails.getUsername());

            // Verifica se a finança existe e pertence ao usuário autenticado
            Finance existingFinance = financeService.getFinanceByIdAndUser(id, user);
            if (existingFinance == null) {
                return ResponseEntity.status(404).body("Finança não encontrada ou você não tem permissão para editá-la.");
            }

            // Atualiza os detalhes da finança
            existingFinance.setData(financeDetails.getData());
            existingFinance.setCategoria(financeDetails.getCategoria());
            existingFinance.setDescricao(financeDetails.getDescricao());
            existingFinance.setTipo(financeDetails.getTipo());
            existingFinance.setValor(financeDetails.getValor());
            existingFinance.setFixoVariavel(financeDetails.getFixoVariavel());
            existingFinance.setParcelas(financeDetails.getParcelas());
            existingFinance.setObservacoes(financeDetails.getObservacoes());

            // Salva as alterações
            Finance updatedFinance = financeService.updateFinance(existingFinance);
            return ResponseEntity.ok(updatedFinance);

        } catch (Exception e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

        @DeleteMapping("/{id}")
        public ResponseEntity<?> deleteFinance(@PathVariable Long id) {
            try {
                // Recupera o usuário autenticado
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                User user = userService.getUserByUsername(userDetails.getUsername());

                // Verifica se a finança existe e pertence ao usuário autenticado
                Finance existingFinance = financeService.getFinanceByIdAndUser(id, user);
                if (existingFinance == null) {
                    return ResponseEntity.status(404).body("Finança não encontrada ou você não tem permissão para excluí-la.");
                }

                // Exclui a finança
                financeService.deleteFinance(existingFinance);
                return ResponseEntity.noContent().build();

            } catch (Exception e) {
                return ResponseEntity.status(403).body(e.getMessage());
            }
        }

    }


