package br.com.fadesp.test.pagamento.domain.dto;

import java.io.Serial;
import java.io.Serializable;

public record InativarPagamentoDTO(
        boolean ativo
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
