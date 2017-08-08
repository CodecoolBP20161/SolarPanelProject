package com.codecool.services;

import com.codecool.models.LineItem;
import com.codecool.models.Offer;
import com.codecool.models.enums.ItemTypeEnum;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;


@Service
public class PdfService {
    private final String PDF_URL = "http://52.15.84.238:1350/api/getofferpdf";

    public File getPdf(Offer offer) throws UnirestException, IOException {
        InputStream pdfInputStream = doPost(offer).getRawBody();
        System.out.println("InputStream: " + pdfInputStream);
        String offerId = "123456";

        return  fileFromInputStream(pdfInputStream, offerId);
    }

    private File fileFromInputStream(InputStream inputstream, String offerId) throws IOException{
        File tempFile = File.createTempFile("A_Napos_Oldal_Arajanlat_" + offerId, ".pdf");
        tempFile.deleteOnExit();
        FileOutputStream out = new FileOutputStream(tempFile);
        IOUtils.copy(inputstream, out);
        return tempFile;
    }

    private HttpResponse<InputStream> doPost(Offer offer) throws UnirestException {
        JSONObject offerJsonObject = new JSONObject();
        JSONArray lineItemItems = new JSONArray();
        JSONArray lineItemsService = new JSONArray();
        for (LineItem item : offer.getLineItems()) {
            if (item.getType() == ItemTypeEnum.Item) {
                lineItemItems.put(item.toJson());
            } else {
                lineItemsService.put(item.toJson());
            }
        }
        offerJsonObject.put("items", lineItemItems);
        offerJsonObject.put("services", lineItemsService);
        offerJsonObject.put("id", 100);
        offerJsonObject.put("taxRate", offer.getTaxRate());
        offerJsonObject.put("netTotal", offer.getNettoTotalPrice());
        offerJsonObject.put("grossTotal", (offer.getNettoTotalPrice().multiply(BigDecimal.valueOf(offer.getTaxRate()))));
        offerJsonObject.put("isNetworkUpgradeNeeded", offer.isNetworkUpgradeNeeded());

        return Unirest.post(PDF_URL)
                      .header("accept", "application/pdf")
                      .header("Content-type", "application/json")
                      .body(offerJsonObject)
                      .asBinary();
    }


}
