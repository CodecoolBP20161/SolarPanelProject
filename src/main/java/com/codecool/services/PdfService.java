package com.codecool.services;

import com.codecool.models.Offer;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.pdfbox.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.*;


@Service
public class PdfService {
    private final String PDF_URL = "http://52.15.84.238:1350/api/getofferpdf";

    public File getPdf(Offer offer) throws UnirestException, IOException {
        InputStream pdfInputStream = doPost(offer).getBody();
        String offerId = String.valueOf(offer.getId());

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
        return Unirest.post(PDF_URL)
                      .header("accept", "application/pdf")
                      .header("Content-type", "application/json")
                      .body(offer.toJson())
                      .asBinary();
    }


}
