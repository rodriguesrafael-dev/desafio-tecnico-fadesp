package br.com.fadesp.test.pagamento.domain.service;

import br.com.fadesp.test.pagamento.core.component.GeradorIdPagamento;
import br.com.fadesp.test.pagamento.core.component.MetodoPagamentoComCartao;
import br.com.fadesp.test.pagamento.core.entity.Pagamento;
import br.com.fadesp.test.pagamento.domain.dto.*;
import br.com.fadesp.test.pagamento.domain.enums.MetodoPagamento;
import br.com.fadesp.test.pagamento.domain.enums.Status;
import br.com.fadesp.test.pagamento.domain.repository.PagamentoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PagamentoService {

    private static final Logger log = LoggerFactory.getLogger(PagamentoService.class);

    private final PagamentoRepository pagamentoRepository;
    private final GeradorIdPagamento geradorIdPagamento;
    private final MetodoPagamentoComCartao metodoPagamentoComCartao;
    private final MongoTemplate mongoTemplate;

    public PagamentoService(
            PagamentoRepository pagamentoRepository,
            GeradorIdPagamento geradorIdPagamento,
            MetodoPagamentoComCartao metodoPagamentoComCartao,
            MongoTemplate mongoTemplate
    ) {
        this.pagamentoRepository = pagamentoRepository;
        this.geradorIdPagamento = geradorIdPagamento;
        this.metodoPagamentoComCartao = metodoPagamentoComCartao;
        this.mongoTemplate = mongoTemplate;
    }

    public AdicionarPagamentoDTO adicionarPagamento(AdicionarPagamentoDTO pagamentoEnviado) {
        Pagamento pagamento;
        boolean existsPagamento = pagamentoEnviado.idPagamento() != null;

        if (existsPagamento) {
            pagamento = pagamentoRepository.findByIdPagamento(pagamentoEnviado.idPagamento())
                    .orElseThrow(() -> new RuntimeException("Pagamento não encontrado!"));
            log.info("Pagamento de numero {} já efetuado.", pagamentoEnviado.idPagamento());
        }

        pagamento = new Pagamento();
        Long novoIdPagamento = geradorIdPagamento.gerar();
        pagamento.setIdPagamento(novoIdPagamento);
        log.info("Criando novo pagamento de numero {}", novoIdPagamento);

        if (pagamentoEnviado.valor() == null || pagamentoEnviado.valor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser maior que zero");
        }

        metodoPagamentoComCartao.validar(pagamentoEnviado.metodoPagamento(), pagamentoEnviado.numeroCartao());

        pagamento.setCpfCnpj(pagamentoEnviado.cpfCnpj());
        pagamento.setValor(pagamentoEnviado.valor());
        pagamento.setAtivo(true);
        pagamento.setMetodoPagamento(pagamentoEnviado.metodoPagamento());

        if (pagamentoEnviado.metodoPagamento() == MetodoPagamento.CARTAO_CREDITO ||
                pagamentoEnviado.metodoPagamento() == MetodoPagamento.CARTAO_DEBITO) {
            pagamento.setNumeroCartao(pagamentoEnviado.numeroCartao());
        } else {
            pagamento.setNumeroCartao(null);
        }

        if (!existsPagamento) {
            Status statusInicial = pagamentoEnviado.status() != null ? pagamentoEnviado.status() : Status.PENDENTE_PROCESSAMENTO;
            pagamento.setStatus(statusInicial);
        }

        Pagamento salvo = pagamentoRepository.save(pagamento);

        log.info("Pagamento {} processado com sucesso. Status: {}, Método: {}", salvo.getIdPagamento(), salvo.getStatus(), salvo.getMetodoPagamento());

        return new AdicionarPagamentoDTO(
                salvo.getIdPagamento(),
                salvo.getCpfCnpj(),
                salvo.getNumeroCartao(),
                salvo.getValor(),
                salvo.isAtivo(),
                salvo.getMetodoPagamento(),
                salvo.getStatus()
        );
    }

    public PageDTO<ListarPagamentoDTO> listarPagamento(
            Optional<Long> idPagamento,
            Optional<String> cpfCnpj,
            Optional<Status> status,
            int page,
            int size,
            Optional<String> sortField,
            Optional<String> sortDirection
    ) {
        log.info("Listando pagamentos: filtros idPagamento={}, cpfCnpj={}, status={}, page={}, size={}, sortField={}, sortDirection={}",
                idPagamento.orElse(null), cpfCnpj.orElse(null), status.orElse(null),
                page, size, sortField.orElse(""), sortDirection.orElse(""));

        Query query = new Query();

        idPagamento.ifPresent(id  -> query.addCriteria(Criteria.where("idPagamento").is(id)));
        cpfCnpj    .ifPresent(cpf -> query.addCriteria(Criteria.where("cpfCnpj")    .is(cpf)));
        status     .ifPresent(st  -> query.addCriteria(Criteria.where("status")     .is(st)));

        sortField.ifPresent(field -> {
            Sort.Direction direction = sortDirection.filter(dir -> dir.equalsIgnoreCase("DESC"))
                    .map(dir -> Sort.Direction.DESC)
                    .orElse(Sort.Direction.ASC);
            query.with(Sort.by(direction, field));
        });

        long total = mongoTemplate.count(query, Pagamento.class);

        query.skip((long) page * size);
        query.limit(size);

        List<Pagamento> resultados = mongoTemplate.find(query, Pagamento.class);
        log.info("Consulta retornou {} registros de um total de {}", resultados.size(), total);

        List<ListarPagamentoDTO> lista = resultados.stream()
                .map(p -> new ListarPagamentoDTO(
                        p.getId(),
                        p.getIdPagamento(),
                        p.getCpfCnpj(),
                        p.getNumeroCartao(),
                        p.getValor(),
                        p.isAtivo(),
                        p.getMetodoPagamento(),
                        p.getStatus()))
                .toList();

        return new PageDTO<>(lista, total, page, size);
    }

    public InativarPagamentoDTO inativarPagamento(String id) {
        log.info("Solicitação para exclusão lógica do pagamento com IDPagamento: {}", id);

        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Pagamento com ID {} não encontrado para exclusão lógica", id);
                    return new RuntimeException("Pagamento não encontrado!");
                });

        if (pagamento.getStatus() != Status.PENDENTE_PROCESSAMENTO) {
            log.warn("Tentativa de excluir pagamento com status {}. Apenas PENDENTE_PROCESSAMENTO pode ser excluído.", pagamento.getStatus());
            throw new IllegalStateException("Somente pagamentos com status PENDENTE_PROCESSAMENTO podem ser desativados.");
        }

        pagamento.setAtivo(false);
        pagamentoRepository.save(pagamento);

        log.info("Pagamento com ID {} desativado com sucesso.", id);

        return new InativarPagamentoDTO(pagamento.isAtivo());
    }

}
