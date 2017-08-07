package com.codecool.services;

import com.codecool.models.Offer;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;


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
        String offerJson ="{\"id\": 123321, \"items\" : [ {\"name\": \"270W-os Amerisolar polikristályos napelem\",\"price\": 10000,\"quantity\": 10,\"subtotal\": 100000,\"description\": \"Legjobb termék\"}, {\"name\": \"270W-os Amerisolar polikristályos napelem\",\"price\": 10000,\"quantity\": 10,\"subtotal\": 100000,\"description\": \"Legjobb termék\"}, {\"name\": \"270W-os Amerisolar polikristályos napelem\",\"price\": 10000,\"quantity\": 10,\"subtotal\": 100000,\"description\": \"Legjobb termék\"}, {\"name\": \"270W-os Amerisolar polikristályos napelem\",\"price\": 10000,\"quantity\": 10,\"subtotal\": 100000,\"description\": \"Legjobb termék\"}], \"services\" : [ {\"name\": \"Service1\", \"price\": 10000, \"quantity\": 10, \"subtotal\": 100000, \"description\": \"Legjobb termék\"}, {\"name\": \"Service1\", \"price\": 10000, \"quantity\": 10, \"subtotal\": 100000, \"description\": \"Legjobb termék\"}, {\"name\": \"Service1\", \"price\": 10000, \"quantity\": 10, \"subtotal\": 100000, \"description\": \"Legjobb termék\"}, {\"name\": \"Service1\", \"price\": 10000, \"quantity\": 10, \"subtotal\": 100000, \"description\": \"Legjobb termék\"}, {\"name\": \"Service1\", \"price\": 10000, \"quantity\": 10, \"subtotal\": 100000, \"description\": \"Legjobb termék\"},],\"isNetworkUpgradeNeeded\": true,\"taxRate\": 27,\"netTotal\": 9999999,\"grossTotal\": 124343}";
        JSONObject offerJsonObject = new JSONObject(offerJson);
        return Unirest.post(PDF_URL)
                      .header("accept", "application/pdf")
                      .header("Content-type", "application/json")
                      .body(offerJsonObject)
                      .asBinary();
    }


}
