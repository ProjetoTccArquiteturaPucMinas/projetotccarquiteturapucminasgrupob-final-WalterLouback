package com.seuprojeto.marketplace.application.usecase;

import com.seuprojeto.marketplace.application.dto.SelecaoCarrinho;
import com.seuprojeto.marketplace.domain.model.CategoriaProduto;
import com.seuprojeto.marketplace.domain.model.Produto;
import com.seuprojeto.marketplace.domain.model.ResumoCarrinho;
import com.seuprojeto.marketplace.domain.repository.ProdutoRepositorio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CalcularCarrinhoUseCase {

    private static final BigDecimal CEM = new BigDecimal("100");
    private static final BigDecimal DESCONTO_MAXIMO_PERCENTUAL = new BigDecimal("25");

    private final ProdutoRepositorio produtoRepositorio;

    public CalcularCarrinhoUseCase(ProdutoRepositorio produtoRepositorio) {
        this.produtoRepositorio = produtoRepositorio;
    }

    public ResumoCarrinho executar(List<SelecaoCarrinho> selecaoCarrinhos) {
        if (selecaoCarrinhos == null || selecaoCarrinhos.isEmpty()) {
            return new ResumoCarrinho(BigDecimal.ZERO, BigDecimal.ZERO);
        }

        BigDecimal subtotal = BigDecimal.ZERO;
        int totalItens = 0;
        BigDecimal descontoCategoriaPercentual = BigDecimal.ZERO;

        for (SelecaoCarrinho selecao : selecaoCarrinhos) {
            validarSelecao(selecao);

            Produto produto = produtoRepositorio.findById(selecao.getIdProduto());

            int quantidade = selecao.getQuantidade();

            subtotal = subtotal.add(produto.getPreco().multiply(BigDecimal.valueOf(quantidade)));
            totalItens += quantidade;

            BigDecimal descontoCategoriaDoItem = obterDescontoPorCategoria(produto.getCategoriaProduto())
                    .multiply(BigDecimal.valueOf(quantidade));
            descontoCategoriaPercentual = descontoCategoriaPercentual.add(descontoCategoriaDoItem);
        }

        BigDecimal descontoQuantidadePercentual = obterDescontoPorQuantidade(totalItens);
        BigDecimal descontoTotalPercentual = descontoQuantidadePercentual.add(descontoCategoriaPercentual);

        if (descontoTotalPercentual.compareTo(DESCONTO_MAXIMO_PERCENTUAL) > 0) {
            descontoTotalPercentual = DESCONTO_MAXIMO_PERCENTUAL;
        }
        BigDecimal valorDesconto = subtotal
                .multiply(descontoTotalPercentual)
                .divide(CEM, 2, RoundingMode.HALF_UP);

        return new ResumoCarrinho(subtotal, valorDesconto);
    }

    private void validarSelecao(SelecaoCarrinho selecao) {
        if (selecao == null || selecao.getIdProduto() == null || selecao.getQuantidade() == null || selecao.getQuantidade() <= 0) {
            throw new IllegalArgumentException("Seleção de carrinho inválida");
        }
    }

    private BigDecimal obterDescontoPorQuantidade(int totalItens) {
        if (totalItens <= 1) {
            return BigDecimal.ZERO;
        }
        if (totalItens == 2) {
            return new BigDecimal("5");
        }
        if (totalItens == 3) {
            return new BigDecimal("7");
        }
        return new BigDecimal("10");
    }

    private BigDecimal obterDescontoPorCategoria(CategoriaProduto categoriaProduto) {
        return switch (categoriaProduto) {
            case CAPINHA, FONE -> new BigDecimal("3");
            case CARREGADOR -> new BigDecimal("5");
            case PELICULA, SUPORTE -> new BigDecimal("2");
        };
    }
}