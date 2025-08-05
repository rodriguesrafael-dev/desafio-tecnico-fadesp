package br.com.fadesp.test.pagamento.core.component;

import br.com.fadesp.test.pagamento.domain.enums.MetodoPagamento;
import org.springframework.stereotype.Component;

@Component
public class MetodoPagamentoComCartao {

    public void validar(MetodoPagamento metodoPagamento, String numeroCartao) {
        boolean isMetodoCartao = metodoPagamento == MetodoPagamento.CARTAO_CREDITO ||
                                 metodoPagamento == MetodoPagamento.CARTAO_DEBITO;

        boolean temNumeroCartao = numeroCartao != null && !numeroCartao.trim().isEmpty();

        if (isMetodoCartao && !temNumeroCartao) {
            throw new IllegalArgumentException(
                    "Número do cartão é obrigatório para pagamentos com " + metodoPagamento
            );
        }

        if (!isMetodoCartao && temNumeroCartao) {
            throw new IllegalArgumentException(
                    "Número do cartão não deve ser informado para pagamentos com " + metodoPagamento
            );
        }

    }

}
