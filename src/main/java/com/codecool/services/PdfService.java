package com.codecool.services;
import com.codecool.models.PdfServerException;
import com.codecool.models.Offer;

import java.io.File;

public class PdfService {

    public File getPdf(Offer offer) throws PdfServerException {
        if (offer == null) throw new PdfServerException();
        return new File("asd");
    }
}
