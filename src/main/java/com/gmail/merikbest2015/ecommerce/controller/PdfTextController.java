package com.gmail.merikbest2015.ecommerce.controller;
import com.gmail.merikbest2015.ecommerce.dto.request.TextSearchRequest;
import com.gmail.merikbest2015.ecommerce.service.PdfTextExtractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class PdfTextController {

    @Autowired
    private PdfTextExtractorService pdfTextExtractorService;

    @GetMapping("/search")
    public boolean search(@RequestParam String keyword, @RequestParam String pdfUrl) {
        try {
            return pdfTextExtractorService.doesTextExistInPdf(pdfUrl, keyword);
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Handle error appropriately
        }
    }
}
