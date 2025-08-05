package br.com.fadesp.test.pagamento.core.entity;

import br.com.fadesp.test.pagamento.domain.enums.MetodoPagamento;
import br.com.fadesp.test.pagamento.domain.enums.Status;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Document(collection = "pagamentos")
public class Pagamento implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Indexed(unique = true)
    private Long idPagamento;

    private String cpfCnpj;
    private String numeroCartao;
    private BigDecimal valor;
    private boolean ativo = true;
    private MetodoPagamento metodoPagamento;
    private Status status;

    public Pagamento() {
    }

    public Pagamento(
            String id,
            Long idPagamento,
            String cpfCnpj,
            String numeroCartao,
            BigDecimal valor,
            boolean ativo,
            MetodoPagamento metodoPagamento,
            Status status
    ) {
        this.id = id;
        this.idPagamento = idPagamento;
        this.cpfCnpj = cpfCnpj;
        this.numeroCartao = numeroCartao;
        this.valor = valor;
        this.ativo = ativo;
        this.metodoPagamento = metodoPagamento;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(Long idPagamento) {
        this.idPagamento = idPagamento;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
