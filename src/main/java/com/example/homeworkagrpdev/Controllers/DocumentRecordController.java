package com.example.homeworkagrpdev.Controllers;

import com.example.homeworkagrpdev.Entities.DocumentViews;
import com.example.homeworkagrpdev.Entities.DocumentRecord;
import com.example.homeworkagrpdev.Entities.DocumentTrend;
import com.example.homeworkagrpdev.Servicies.DocumentRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
public class DocumentRecordController {

    @Autowired
    DocumentRecordService documentRecordService;

    @PostMapping("/addRecord")
    void addRecord(@RequestParam String bookUuid, @RequestParam String userUuid){
        documentRecordService.addRecord(new DocumentRecord(UUID.fromString(bookUuid), UUID.fromString(userUuid)));
    }

    @GetMapping("/topOverallLastWeek")
    List<DocumentViews> getMostOpenedLastWeekOverall(){
        return documentRecordService.mostOpenedInLastWeekOverall();
    }

    //For testing purposes
    @GetMapping("/topOverallLastWeekTest")
    List<DocumentViews> getMostOpenedLastWeekOverall(@RequestParam LocalDateTime endOfTheWeek){
        return documentRecordService.mostOpenedInLastWeekOverall(endOfTheWeek);
    }

    @GetMapping("/topUniqueLastWeek")
    List<DocumentViews> getMostOpenedLastWeekUniqueUsers(){
        return documentRecordService.mostOpenedInLastWeekUniqueUsers();
    }

    //For testing purposes
    @GetMapping("/topUniqueLastWeekTest")
    List<DocumentViews> getMostOpenedLastWeekUniqueUsers(@RequestParam LocalDateTime endOfTheWeek){
        return documentRecordService.mostOpenedInLastWeekUniqueUsers(endOfTheWeek);
    }

    @GetMapping("/mostTrendingLastWeek")
    List<DocumentTrend> getMostTrendingLastWeek(){
        return documentRecordService.mostTrendingLastWeek();
    }

    //For testing purposes
    @GetMapping("/mostTrendingLastWeekTest")
    List<DocumentTrend> getMostTrendingLastWeek(@RequestParam LocalDateTime endOfTheWeek){
        return documentRecordService.mostTrendingLastWeek(endOfTheWeek);
    }

    @GetMapping("/mostTrendingFrom")
    List<DocumentTrend> getMostTrendingFromDate(@RequestParam LocalDateTime from){
        return documentRecordService.mostTrendingHistory(from);
    }

    //For testing purposes
    @GetMapping("/mostTrendingFromTest")
    List<DocumentTrend> getMostTrendingFromDate(@RequestParam LocalDateTime from, @RequestParam LocalDateTime to){
        return documentRecordService.mostTrendingHistory(from, to);
    }

}
