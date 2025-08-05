package br.com.fadesp.test.pagamento.core.component;

import br.com.fadesp.test.pagamento.core.entity.Pagamento;
import br.com.fadesp.test.pagamento.domain.enums.Status;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class ProcessarPagamento {

    private static final Random random = new Random();

    public static Status simular() {
        if (random.nextDouble() < 0.6) {
            return Status.PROCESSADO_SUCESSO;
        } else {
            return Status.PROCESSADO_FALHA;
        }
    }

    public static Status atualizarStatus(Pagamento pagamento) {
        Status statusAtual = pagamento.getStatus();

        return switch (statusAtual) {
            case PENDENTE_PROCESSAMENTO -> {
                statusAtual = simular();
                yield statusAtual;
            }
            case PROCESSADO_FALHA -> {
                statusAtual = Status.PENDENTE_PROCESSAMENTO;
                yield statusAtual;
            }
            case PROCESSADO_SUCESSO -> {
                yield statusAtual;
            }
        };
    }

}
