package controllers;

import api.ReceiptSuggestionResponse;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.Collections;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONObject;

import static java.lang.System.out;

@Path("/images")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.APPLICATION_JSON)
public class ReceiptImageController {
    private final AnnotateImageRequest.Builder requestBuilder;

    public ReceiptImageController() {
        // DOCUMENT_TEXT_DETECTION is not the best or only OCR method available
        Feature ocrFeature = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        this.requestBuilder = AnnotateImageRequest.newBuilder().addFeatures(ocrFeature);

    }

    /**
     * This borrows heavily from the Google Vision API Docs.  See:
     * https://cloud.google.com/vision/docs/detecting-fulltext
     *
     * YOU SHOULD MODIFY THIS METHOD TO RETURN A ReceiptSuggestionResponse:
     *
     * public class ReceiptSuggestionResponse {
     *     String merchantName;
     *     String amount;
     * }
     */
    @POST
    public ReceiptSuggestionResponse parseReceipt(@NotEmpty String base64EncodedImage) throws Exception {
        Image img = Image.newBuilder().setContent(ByteString.copyFrom(Base64.getDecoder().decode(base64EncodedImage))).build();
        AnnotateImageRequest request = this.requestBuilder.setImage(img).build();

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse responses = client.batchAnnotateImages(Collections.singletonList(request));
            AnnotateImageResponse res = responses.getResponses(0);

            String merchantName = null;
            BigDecimal amount = null;

            // Your Algo Here!!
            // Sort text annotations by bounding polygon.  Top-most non-decimal text is the merchant
            // bottom-most decimal text is the total amount
            out.println("==========================================================================");
            String receiptInfo = res.getTextAnnotationsList().get(0).getDescription();
            String[] receiptEle = receiptInfo.split("\n");
            out.println("Hello " + receiptEle[0] + " Bye");
            merchantName = receiptEle[0];
            out.println("Last ele " + receiptEle[receiptEle.length - 1]);
            try {
                amount = new BigDecimal(receiptEle[receiptEle.length - 1]);
            } catch(NumberFormatException e){
                amount = new BigDecimal(0.0);
            }
            out.println("==========================================================================");
            String boundingPoly = res.getTextAnnotationsList().get(0).getBoundingPoly().toString();
            String[] bp = boundingPoly.split("vertices ");
            out.println("==========================================================================");
            String cornerFirst = bp[1];
            String cornerLast = bp[3];

            String[] corner1 = cornerFirst.split("\n");
            String[] corner3 = cornerLast.split("\n");
            Integer xVal1 = Integer.parseInt(corner1[1].split(": ")[1]);
            Integer yVal1 = Integer.parseInt(corner1[2].split(": ")[1]);
            Integer xVal2 = Integer.parseInt(corner3[1].split(": ")[1]);
            Integer yVal2 = Integer.parseInt(corner3[2].split(": ")[1]);

//            JSONObject json = new JSONObject();
//            json.put("x1", xVal1);
//            json.put("y1", yVal1);
//            json.put("x2", xVal2);
//            json.put("y2", yVal2);
//            json.put("merchantName", merchantName);
//            json.put("amount", amount);
//            out.println(json);

//            for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
//                out.printf("Position : %s\n", annotation.getBoundingPoly());
//                out.printf("Text: %s\n", annotation.getDescription());
//                break;
//            }

            //TextAnnotation fullTextAnnotation = res.getFullTextAnnotation();
            return new ReceiptSuggestionResponse(merchantName, amount, xVal1, xVal2, yVal1, yVal2);
        }
    }
}
