package com.seuprojeto.marketplace.application.usecase;

import com.seuprojeto.marketplace.application.dto.SelecaoCarrinho;
import com.seuprojeto.marketplace.domain.model.CategoriaProduto;
import com.seuprojeto.marketplace.domain.model.Produto;
import com.seuprojeto.marketplace.domain.repository.ProdutoRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CalcularCarrinhoUseCaseTest {

    private CalcularCarrinhoUseCase useCase;

    @BeforeEach
    void setup() {
        useCase = new CalcularCarrinhoUseCase(new FakeProdutoRepositorio());
    }

    @Test
    void shouldApplyCategoryDiscountWhenSingleItem() {
        List<SelecaoCarrinho> input = List.of(
                new SelecaoCarrinho(1L, 1)
        );

        var result = useCase.executar(input);
        assertEquals(0, result.getSubtotal().compareTo(new BigDecimal("50")));
        assertEquals(0, result.getDesconto().compareTo(new BigDecimal("1.50")));
        assertEquals(0, result.getTotal().compareTo(new BigDecimal("48.50")));
    }

    @Test
    void shouldApplyQuantityDiscount() {

        List<SelecaoCarrinho> input = List.of(
                new SelecaoCarrinho(1L, 2)
        );

        var result = useCase.executar(input);

        assertTrue(result.getDesconto().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void shouldApplyCategoryDiscount() {

        List<SelecaoCarrinho> input = List.of(
                new SelecaoCarrinho(2L, 1)
        );

        var result = useCase.executar(input);

        assertTrue(result.getDesconto().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void shouldApplyCumulativeDiscounts() {

        List<SelecaoCarrinho> input = List.of(
                new SelecaoCarrinho(1L, 2),
                new SelecaoCarrinho(2L, 2)
        );

        var result = useCase.executar(input);

        assertTrue(result.getDesconto().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void shouldNotExceedMaxDiscountLimit() {

        List<SelecaoCarrinho> input = List.of(
                new SelecaoCarrinho(2L, 10)
        );

        var result = useCase.executar(input);

        BigDecimal maxAllowed = result.getSubtotal().multiply(new BigDecimal("0.25"));

        assertTrue(result.getDesconto().compareTo(maxAllowed) <= 0);
    }

    @Test
    void shouldCalculateTotalCorrectly() {

        List<SelecaoCarrinho> input = List.of(
                new SelecaoCarrinho(3L, 1)
        );

        var result = useCase.executar(input);

        BigDecimal expectedTotal = result.getSubtotal().subtract(result.getDesconto());

        assertEquals(0, expectedTotal.compareTo(result.getTotal()));
    }

    static class FakeProdutoRepositorio implements ProdutoRepositorio {

        private final List<Produto> produtos = List.of(
                new Produto(1L, "Capinha", CategoriaProduto.CAPINHA, new BigDecimal("50")),
                new Produto(2L, "Carregador", CategoriaProduto.CARREGADOR, new BigDecimal("100")),
                new Produto(3L, "Fone", CategoriaProduto.FONE, new BigDecimal("200")),
                new Produto(4L, "Película", CategoriaProduto.PELICULA, new BigDecimal("30")),
                new Produto(5L, "Suporte", CategoriaProduto.SUPORTE, new BigDecimal("80"))
        );

        @Override
        public List<Produto> findAll() {
            return produtos;
        }

        @Override
        public Optional<Produto> findById(Long id) {
            return produtos.stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst();
        }
    }
}