package br.com.fadesp.test.pagamento.domain.dto;

import br.com.fadesp.test.pagamento.domain.enums.Status;

import java.io.Serial;
import java.io.Serializable;

public record ProcessarPagamentoDTO(
        Long idPagamento,
        Status status
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
