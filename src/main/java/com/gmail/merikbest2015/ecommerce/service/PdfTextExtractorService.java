package com.gmail.merikbest2015.ecommerce.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

@Service
public class PdfTextExtractorService {

    public boolean doesTextExistInPdf(String pdfUrl, String searchText) throws IOException {
        URL url = new URL(pdfUrl);
        try (PDDocument document = PDDocument.load(url.openStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            return text.contains(searchText);
        }
    }
}
