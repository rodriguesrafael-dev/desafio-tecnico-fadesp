package br.com.fadesp.test.pagamento.rest;

import br.com.fadesp.test.pagamento.domain.dto.*;
import br.com.fadesp.test.pagamento.domain.enums.Status;
import br.com.fadesp.test.pagamento.domain.service.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/pagamentos")
@CrossOrigin(origins = "http://localhost:4200")
public class PagamentoController {

    private final PagamentoService pagamentoService;
    private final KafkaTemplate<Long, ProcessarPagamentoDTO> kafkaTemplate;

    public PagamentoController(
            PagamentoService pagamentoService,
            KafkaTemplate<Long, ProcessarPagamentoDTO> kafkaTemplate
    ) {
        this.pagamentoService = pagamentoService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public ResponseEntity<AdicionarPagamentoDTO> adicionarPagamento(
            @Valid @RequestBody AdicionarPagamentoDTO pagamento,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        pagamento = pagamentoService.adicionarPagamento(pagamento);
        var uri = uriComponentsBuilder.path("/{idPagamento}").buildAndExpand(pagamento.idPagamento()).toUri();
        return ResponseEntity.created(uri).body(pagamento);
    }

    @GetMapping("/lista-pagamentos")
    public ResponseEntity<PageDTO<ListarPagamentoDTO>> listarPagamento(
            @RequestParam(required      = false) Long idPagamento,
            @RequestParam(required      = false) String cpfCnpj,
            @RequestParam(required      = false) Status status,
            @RequestParam(defaultValue  = "0")   int page,
            @RequestParam(defaultValue  = "6")   int size,
            @RequestParam(required      = false) String sortField,
            @RequestParam(defaultValue  = "ASC") String sortDirection
    ) {
        PageDTO<ListarPagamentoDTO> pagamentos = pagamentoService.listarPagamento(
                Optional.ofNullable(idPagamento),
                Optional.ofNullable(cpfCnpj),
                Optional.ofNullable(status),
                page,
                size,
                Optional.ofNullable(sortField),
                Optional.ofNullable(sortDirection)
        );

        return ResponseEntity.ok(pagamentos);
    }

    @PutMapping("/status")
    public ResponseEntity<Map<String, String>> processarPagamentoViaKafka(@RequestBody ProcessarPagamentoDTO pagamento) {
        kafkaTemplate.send("pagamento-status", pagamento.idPagamento(), pagamento);
        return ResponseEntity.accepted()
                .body(Map.of("mensagem", "Mensagem publicada para o tópico pagamento-status no Kafka."));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<InativarPagamentoDTO> inativarPagamento(@PathVariable String id) {
        InativarPagamentoDTO pagamento = pagamentoService.inativarPagamento(id);
        return ResponseEntity.ok(pagamento);
    }

}
