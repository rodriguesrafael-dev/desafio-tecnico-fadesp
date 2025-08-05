package br.com.fadesp.test.pagamento.domain.dto;

import br.com.fadesp.test.pagamento.domain.enums.MetodoPagamento;
import br.com.fadesp.test.pagamento.domain.enums.Status;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public record AdicionarPagamentoDTO(
        Long idPagamento,

        @NotBlank(message = "CPF/CNPJ é obrigatório")
        @Pattern(regexp = "\\d{11}|\\d{14}", message = "CPF deve ter 11 dígitos ou CNPJ deve ter 14 dígitos")
        String cpfCnpj,

        @Pattern(regexp = "\\d{13,16}", message = "Número do cartão deve conter apenas dígitos")
        String numeroCartao,

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
        BigDecimal valor,

        boolean ativo,

        @NotNull(message = "Método de pagamento é obrigatório")
        MetodoPagamento metodoPagamento,

        Status status
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
