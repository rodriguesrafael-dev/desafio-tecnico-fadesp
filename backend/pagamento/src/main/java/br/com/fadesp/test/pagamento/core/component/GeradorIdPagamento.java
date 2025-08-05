package br.com.fadesp.test.pagamento.core.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class GeradorIdPagamento {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Long gerar() {
        String hoje = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String sequenceName = "pagamento_seq_" + hoje;

        Query query = new Query(Criteria.where("_id").is(sequenceName));
        Update update = new Update().inc("seq", 1);
        FindAndModifyOptions options = new FindAndModifyOptions()
                .returnNew(true)
                .upsert(true);

        SequenceDocument sequence = mongoTemplate.findAndModify(
                query, update, options, SequenceDocument.class, "sequences"
        );

        String id = hoje + String.format("%06d", sequence.getSeq());
        return Long.parseLong(id);
    }

}

@Document(collection = "sequence")
class SequenceDocument {
    @Id
    private String id;
    private int seq;

    public SequenceDocument() {}

    public SequenceDocument(String id, int seq) {
        this.id = id;
        this.seq = seq;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

}
