package br.com.fadesp.test.pagamento.core.component;

import br.com.fadesp.test.pagamento.core.entity.Pagamento;
import br.com.fadesp.test.pagamento.domain.dto.ProcessarPagamentoDTO;
import br.com.fadesp.test.pagamento.domain.enums.Status;
import br.com.fadesp.test.pagamento.domain.repository.PagamentoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProcessarPagamentoKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(ProcessarPagamentoKafkaConsumer.class);
    private final PagamentoRepository pagamentoRepository;

    public ProcessarPagamentoKafkaConsumer(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    @KafkaListener(topics = "pagamento-status", groupId = "payments-group")
    public void processarPagamento(ProcessarPagamentoDTO pagamentoDTO) {
        log.info("Recebido pedido de processamento via Kafka para idPagamento: {} e status: {}",
                pagamentoDTO.idPagamento(), pagamentoDTO.status());

        Optional<Pagamento> pagamentoEnviado = pagamentoRepository.findByIdPagamento(pagamentoDTO.idPagamento());
        if (pagamentoEnviado.isEmpty()) {
            log.warn("Pagamento de número {} não encontrado.", pagamentoDTO.idPagamento());
            return;
        }

        Pagamento pagamento = pagamentoEnviado.get();
        Status statusAtual = pagamento.getStatus();

        Status statusRetornado = ProcessarPagamento.atualizarStatus(pagamento);

        pagamento.setStatus(statusRetornado);

        if (statusRetornado.equals(statusAtual)) {
            log.warn("Transição de status inválida: {} -> {} para pagamento numero {}", statusAtual, statusRetornado, pagamentoDTO.idPagamento());
            return;
        }

        pagamentoRepository.save(pagamento);

        log.info("Pagamento {} atualizado de {} para {}",
                pagamentoDTO.idPagamento(), statusAtual, statusRetornado);
    }

}
