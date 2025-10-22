package com.sendika.ocr;



import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/api/ocr")
public class OcrController {

    private final OcrService ocrService;

    public OcrController(OcrService ocrService) {
        this.ocrService = ocrService;
    }

    @PreAuthorize("isAuthenticated()") // veya hasRole('USER') / hasAuthority('SCOPE_read') vs.
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> ocr(
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "lang", required = false, defaultValue = "tur") String lang
    ) throws Exception {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Dosya yüklenmedi.");
        }

        // Basit MIME kontrolü: (PNG/JPG/PDF gibi)
        String contentType = file.getContentType();
        if (contentType == null ||
                !(contentType.equals("image/png") ||
                        contentType.equals("image/jpeg") ||
                        contentType.equals("image/webp") ||
                        contentType.equals("application/pdf") )) {
            return ResponseEntity.badRequest().body("Sadece PNG/JPG/WEBP/PDF desteklenir.");
        }

        // Geçici dosyaya kaydedip OCR çalıştırıyoruz
        File tmp = File.createTempFile("ocr_", "_" + StringUtils.cleanPath(file.getOriginalFilename()));
        file.transferTo(tmp);

        try {
            String text = ocrService.doOcr(tmp, lang);
            return ResponseEntity.ok(new OcrResponse(text, lang));
        } finally {
            tmp.delete();
        }
    }

    public record OcrResponse(String text, String lang) {}
}
