package dao;

import api.ReceiptResponse;
import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;
import org.h2.jdbc.JdbcSQLException;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;
import static generated.Tables.TAGS;

public class TagDao {
    DSLContext dsl;

    public TagDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public void toggleTag(String tagName, Integer id) {
        boolean exists = false;
        List<TagsRecord> trlist = dsl.selectFrom(TAGS).fetch();
        for(TagsRecord ele:trlist){
            if(ele.getTag().equals(tagName) && ele.getReceiptid().equals(id)){
                exists = true;
                break;
            }
        }

        if(exists){
            System.out.println("Exists");
            System.out.println(dsl.deleteFrom(TAGS).where(String.valueOf(TAGS.TAG.equals(tagName)), TAGS.RECEIPTID.equals(id)).execute());
        }

        else{
            TagsRecord tagsRecord = dsl
                    .insertInto(TAGS, TAGS.TAG, TAGS.RECEIPTID)
                    .values(tagName, id)
                    .returning(TAGS.ID)
                    .fetchOne();

            checkState(tagsRecord != null && tagsRecord.getId() != null, "Insert failed");
        }
    }

    public List<ReceiptsRecord> getAllReceiptsWithTag(String tag) {
        List<TagsRecord> tags = dsl.selectFrom(TAGS).fetch();
        ArrayList<ReceiptsRecord> receipts = new ArrayList<ReceiptsRecord>();
        for(TagsRecord tr: tags){
            if(tr.getTag().equals(tag))
                receipts.add(dsl.selectFrom(RECEIPTS).where("id=" + tr.getReceiptid()).fetchOne());
        }
        return receipts;
    }

    public List<TagsRecord> getTags(){
        List<TagsRecord> tags = dsl.selectFrom(TAGS).fetch();
        return tags;
    }
}
