package com.codecool.services;

import com.codecool.models.LineItem;
import com.codecool.models.Offer;
import com.codecool.models.enums.ItemTypeEnum;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

@Slf4j
@Service
public class PdfService {
    private final String PDF_URL = "http://18.194.36.195:1350/api/getofferpdf";

    public File getPdf(Offer offer) throws UnirestException, IOException {
        InputStream pdfInputStream = doPost(offer).getRawBody();

        return  fileFromInputStream(pdfInputStream, String.valueOf(offer.getId()), offer.getCompany().toString());
    }

    private File fileFromInputStream(InputStream inputstream, String offerId, String companyName) throws IOException{
        File offerFile = File.createTempFile(companyName + "_arajanlat_" + offerId + "@", ".pdf");
        offerFile.deleteOnExit();
        FileOutputStream out = new FileOutputStream(offerFile);
        IOUtils.copy(inputstream, out);
        return offerFile;
    }

    private HttpResponse<InputStream> doPost(Offer offer) throws UnirestException {
        return Unirest.post(PDF_URL)
                      .header("accept", "application/pdf")
                      .header("Content-type", "application/json")
                      .body(toJSONObject(offer))
                      .asBinary();
    }

    private JSONObject toJSONObject(Offer offer) {
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
        offerJsonObject.put("id", offer.getId());
        offerJsonObject.put("taxRate", offer.getCompany().getTaxRate());
        offerJsonObject.put("netTotal", offer.getNettoTotalPrice());
        offerJsonObject.put("serviceNetTotal", offer.getNettoServiceTotalPrice());
        offerJsonObject.put("serviceGrossTotal",offer.getNettoServiceTotalPrice().multiply(BigDecimal.valueOf(1.27)));
        offerJsonObject.put("itemGrossTotal", offer.getNettoTotalPrice().multiply(BigDecimal.valueOf(offer.getCompany().getTaxRate())));
        offerJsonObject.put("itemNetTotal",offer.getNettoTotalPrice());
        offerJsonObject.put("grossTotal", (offer.getNettoTotalPrice().multiply(BigDecimal.valueOf(offer.getCompany().getTaxRate()))));
        offerJsonObject.put("isNetworkUpgradeNeeded", offer.isNetworkUpgradeNeeded());
        offerJsonObject.put("company", offer.getCompany());
        offerJsonObject.put("nettFull", offer.getNettoServiceTotalPrice().add(offer.getNettoTotalPrice()));
        offerJsonObject.put("grossFull", (offer.getNettoServiceTotalPrice().multiply(BigDecimal.valueOf(1.27))).
                add(offer.getNettoTotalPrice().multiply(BigDecimal.valueOf(offer.getCompany().getTaxRate()))));
        System.out.println(offer.getNettoServiceTotalPrice().add(offer.getNettoTotalPrice()));
        System.out.println((offer.getNettoServiceTotalPrice().multiply(BigDecimal.valueOf(1.27))).
                add(offer.getNettoTotalPrice().multiply(BigDecimal.valueOf(offer.getCompany().getTaxRate()))));
        return offerJsonObject;
    }

}
