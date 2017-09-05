package controllers;

import api.CreateReceiptRequest;
import api.ReceiptResponse;
import dao.ReceiptDao;
import dao.TagDao;
import generated.tables.records.ReceiptsRecord;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/tags/{tag}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TagController {
    final TagDao tags;
    final ReceiptDao receipts;

    public TagController(TagDao tags, ReceiptDao receipts) {
        this.tags = tags;
        this.receipts = receipts;
    }

    @PUT
    public void toggleTag(@PathParam("tag") String tagName, int id) {
        tags.toggleTag(tagName, id);
    }
    @GET
    public List<ReceiptResponse> getReceiptsWithTag(@PathParam("tag") String tagName) {
        List<ReceiptsRecord> receiptRecords = tags.getAllReceiptsWithTag(tagName);
        return receiptRecords.stream().map(ReceiptResponse::new).collect(toList());
    }
}