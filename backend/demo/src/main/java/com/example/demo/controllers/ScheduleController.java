package com.example.demo.controllers;

import com.example.demo.models.WorshipSchedule;
import com.example.demo.services.WorshipScheduleService;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Autowired
    private WorshipScheduleService service;

    // Под вопросом? Нужен ли вообще html, когда можно тянуть с базы.
    @PostMapping("/upload")
    public ResponseEntity<?> uploadSchedule(@RequestParam("file") MultipartFile file) {
        try {
            InputStream docStream = new ByteArrayInputStream(file.getBytes());
            XWPFDocument doc = new XWPFDocument(docStream);

            StringBuilder htmlContent = new StringBuilder();

            // Заголовок как во фронт-версии с Bootstrap-классами
            htmlContent.append("<h2 class=\"text-center fw-bold fs-2 mb-5 mt-4\">Расписание Богослужений</h2>");
            htmlContent.append("<div class=\"table-responsive\">");

            // Обработка только первой таблицы Word (если их несколько)
            if (!doc.getTables().isEmpty()) {
                var table = doc.getTables().get(0);
                htmlContent.append("<table class=\"table table-bordered\">");
                boolean theadDone = false;
                for (int i = 0; i < table.getNumberOfRows(); i++) {
                    var row = table.getRow(i);
                    // Шапка
                    if (i == 0 && !theadDone) {
                        htmlContent.append("<thead><tr>");
                        row.getTableCells()
                                .forEach(cell -> htmlContent.append("<th>").append(cell.getText()).append("</th>"));
                        htmlContent.append("</tr></thead><tbody>");
                        theadDone = true;
                    } else {
                        htmlContent.append("<tr>");
                        row.getTableCells()
                                .forEach(cell -> htmlContent.append("<td>").append(cell.getText()).append("</td>"));
                        htmlContent.append("</tr>");
                    }
                }
                htmlContent.append("</tbody></table>");
            } else {
                // если таблица отсутствует, можно что-то другое выводить или оставить пустым
                htmlContent.append("<div class=\"alert alert-warning\">Нет данных для отображения</div>");
            }
            htmlContent.append("</div>");

            // Сохраняем как обычно, htmlContent.toString() — ваш HTML для фронта
            WorshipSchedule schedule = service.saveSchedule("Расписание Богослужений", htmlContent.toString());
            return ResponseEntity.ok(schedule);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка загрузки Word-файла");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSchedule(@PathVariable Long id) {
        Optional<WorshipSchedule> schedule = service.getScheduleById(id);
        return schedule.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editSchedule(@PathVariable Long id, @RequestBody WorshipSchedule updated) {
        Optional<WorshipSchedule> schedule = service.updateSchedule(id, updated.getContent());
        return schedule.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSchedule(@PathVariable Long id) {
        service.deleteSchedule(id);
        return ResponseEntity.ok().build();
    }
}
