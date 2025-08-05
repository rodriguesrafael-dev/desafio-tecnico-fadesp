package br.com.fadesp.test.pagamento.domain.repository;

import br.com.fadesp.test.pagamento.core.entity.Pagamento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagamentoRepository extends MongoRepository<Pagamento, String> {
    Optional<Pagamento> findByIdPagamento(Long idPagamento);
}
