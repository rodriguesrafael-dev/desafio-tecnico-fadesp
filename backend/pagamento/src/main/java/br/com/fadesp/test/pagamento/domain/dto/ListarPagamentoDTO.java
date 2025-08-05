package br.com.fadesp.test.pagamento.domain.dto;

import br.com.fadesp.test.pagamento.domain.enums.MetodoPagamento;
import br.com.fadesp.test.pagamento.domain.enums.Status;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public record ListarPagamentoDTO(
        String id,
        Long idPagamento,
        String cpfCnpj,
        String numeroCartao,
        BigDecimal valor,
        boolean ativo,
        MetodoPagamento metodoPagamento,
        Status status
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
