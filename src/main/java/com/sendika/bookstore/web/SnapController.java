package com.sendika.bookstore.web;
import com.sendika.bookstore.model.Snap;
import com.sendika.bookstore.repo.SnapRepository;
import com.sendika.bookstore.service.SnapStorageService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/snaps")
public class SnapController {

    private final SnapRepository repo;
    private final SnapStorageService storage;

    public SnapController(SnapRepository repo, SnapStorageService storage) {
        this.repo = repo;
        this.storage = storage;
    }

    // PNG + başlık/alt başlık kaydet
    @PreAuthorize("isAuthenticated()")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(
            @RequestPart("image") MultipartFile imagePng,
            @RequestPart("title") String title,
            @RequestPart(value = "subtitle", required = false) String subtitle
    ) throws Exception {
        if (imagePng == null || imagePng.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error","Görsel yok"));
        }
        if (!"image/png".equalsIgnoreCase(imagePng.getContentType())) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body(Map.of("error","Sadece PNG kabul edilir"));
        }
        String filename = storage.savePng(imagePng);
        Snap snap = repo.save(new Snap(title, subtitle, filename));
        return ResponseEntity.ok(Map.of("id", snap.getId(), "filename", snap.getFilename()));
    }

    // Liste
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<Snap> list() {
        return repo.findAll();
    }

    // Görseli döndür (Content-Type: image/png)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> image(@PathVariable Long id) throws Exception {
        Snap s = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
        var path = storage.resolve(s.getFilename());
        if (!Files.exists(path)) return ResponseEntity.notFound().build();
        FileSystemResource res = new FileSystemResource(path);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(res);
    }
}
