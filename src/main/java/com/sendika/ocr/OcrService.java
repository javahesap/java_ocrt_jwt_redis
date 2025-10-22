package com.sendika.ocr;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class OcrService {

    private final ITesseract tesseract = new Tesseract();

    public OcrService(
            @Value("${ocr.tessdata-path:}") String tessdataProp
    ) {
        // 1) application.properties
        String path = (tessdataProp != null && !tessdataProp.isBlank()) ? tessdataProp : null;

        // 2) Ortam değişkeni
        if (path == null) {
            String env = System.getenv("TESSDATA_PREFIX");
            if (env != null && !env.isBlank()) path = env;
        }

        if (path == null) {
            throw new IllegalStateException(
                    "tessdata yolu bulunamadı. 'ocr.tessdata-path' ya da TESSDATA_PREFIX ayarlayın."
            );
        }

        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new IllegalStateException("Geçersiz tessdata dizini: " + dir.getAbsolutePath());
        }

        tesseract.setDatapath(dir.getAbsolutePath());
        tesseract.setLanguage("eng"); // istekte override edilecek
        // İsteğe bağlı:
        // tesseract.setOcrEngineMode(1);
        // tesseract.setPageSegMode(3);
    }

    public String doOcr(File imageOrPdf, String lang) {
        if (lang != null && !lang.isBlank()) tesseract.setLanguage(lang); // "tur", "eng", "tur+eng"
        try {
            return tesseract.doOCR(imageOrPdf);
        } catch (TesseractException e) {
            throw new RuntimeException("OCR sırasında hata: " + e.getMessage(), e);
        }
    }
}
