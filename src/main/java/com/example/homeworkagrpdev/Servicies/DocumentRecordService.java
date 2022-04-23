package com.example.homeworkagrpdev.Servicies;

import com.example.homeworkagrpdev.Entities.DocumentViews;
import com.example.homeworkagrpdev.Entities.DocumentRecord;
import com.example.homeworkagrpdev.Entities.DocumentTrend;
import com.example.homeworkagrpdev.Repositories.DocumentRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DocumentRecordService {

    @Autowired
    DocumentRecordRepository documentRecordRepository;

    public void addRecord(DocumentRecord documentRecord){
        documentRecordRepository.addRecord(documentRecord);
    }

    public List<DocumentViews> mostOpenedInLastWeekOverall(){
        return this.mostOpenedInLastWeekOverall(LocalDateTime.now());
    }

    //For testing purposes
    public List<DocumentViews> mostOpenedInLastWeekOverall(LocalDateTime endOfTheWeek){
        return documentRecordRepository.mostOpenedInLastWeekOverall(endOfTheWeek);
    }

    public List<DocumentViews> mostOpenedInLastWeekUniqueUsers(){
        return this.mostOpenedInLastWeekUniqueUsers(LocalDateTime.now());
    }

    //For testing purposes
    public List<DocumentViews> mostOpenedInLastWeekUniqueUsers(LocalDateTime endOfTheWeek){
        return documentRecordRepository.mostOpenedInLastWeekUniqueUsers(endOfTheWeek);
    }

    public List<DocumentTrend> mostTrendingLastWeek(){

        return this.mostTrendingLastWeek(LocalDateTime.now());
    }

    public List<DocumentTrend> mostTrendingLastWeek(LocalDateTime endOfTheWeek){
        float average = this.getAverageViewsBetweenDates(endOfTheWeek.minus(1, ChronoUnit.DAYS), endOfTheWeek);
        List<DocumentTrend> documentTrends = new ArrayList<>();
        for(DocumentViews d : this.getAllViewsBetweenDates(endOfTheWeek.minus(1,ChronoUnit.DAYS), endOfTheWeek)){
            if(d.getViews() >= (int)average){
                documentTrends.add(
                        new DocumentTrend(d.getDocumentUuid(),d.getViews())
                );
            }
        }
        for(DocumentTrend trend : documentTrends){
            for(int i = 1; i < 7; i++){
                int displays = this.numberOfViews(trend.getDocumentUuid(), endOfTheWeek.minus(i+1, ChronoUnit.DAYS), endOfTheWeek.minus(i, ChronoUnit.DAYS));
                trend.addHistory(displays);
            }
            trend.calculateScoreWithDegradation();
        }

        Collections.sort(documentTrends);
        return getFirst10(documentTrends);
    }

    //For testing purposes
    public List<DocumentTrend> mostTrendingHistory(LocalDateTime from, LocalDateTime to){
        return getDocumentTrends(from, to);
    }

    public List<DocumentTrend> mostTrendingHistory(LocalDateTime from){
        LocalDateTime to = LocalDateTime.now();
        return getDocumentTrends(from, to);
    }

    private List<DocumentTrend> getDocumentTrends(LocalDateTime from, LocalDateTime to) throws IllegalArgumentException{
        Duration duration = Duration.between(from, to);
        if(duration.toHours() < 0){
            throw new IllegalArgumentException("Date from must be lower than date to.");
        }
        List<DocumentTrend> documentTrends = new ArrayList<>();

        if(duration.toDays() < 3) {
            calculate(documentTrends, (int) duration.toHours(), ChronoUnit.HOURS, to);
        } else if(duration.toDays() < 15) {
            calculate(documentTrends, (int) duration.toDays() * 2, ChronoUnit.HALF_DAYS, to);
        } else if(duration.toDays() < 32) {
            calculate(documentTrends, (int) duration.toDays(), ChronoUnit.DAYS, to);
        } else if (duration.toDays() < 367){
            calculate(documentTrends, (int) duration.toDays() / 7, ChronoUnit.WEEKS, to);
        } else {
            calculate(documentTrends, (int) duration.toDays() / 30, ChronoUnit.MONTHS, to);
        }

        Collections.sort(documentTrends);
        return getFirst10(documentTrends);
    }

    private void calculate(List<DocumentTrend> documentTrends, int numberOfPeriods, ChronoUnit unit, LocalDateTime to){
        float average = this.getAverageViewsBetweenDates(to.minus(1, unit), to);
        for(DocumentViews d : this.getAllViewsBetweenDates(to.minus(1, unit), to)){
            if(d.getViews() >= (int)average){
                documentTrends.add(
                        new DocumentTrend(d.getDocumentUuid(),d.getViews())
                );
            }
        }

        for(DocumentTrend trend : documentTrends){
            for(int i = 1; i < numberOfPeriods; i++){
                int displays = this.numberOfViews(trend.getDocumentUuid(),to.minus(i+1, unit), to.minus(i, unit));
                trend.addHistory(displays);
            }
            trend.calculateScoreWithDegradation();
        }
    }

    private int numberOfViews(UUID documentUuid, LocalDateTime from, LocalDateTime to){
        return documentRecordRepository.numberOfViews(documentUuid, from, to);
    }

    private float getAverageViewsBetweenDates(LocalDateTime from, LocalDateTime to){
        return documentRecordRepository.averageNumberOfViews(from, to);
    }

    private List<DocumentViews> getAllViewsBetweenDates(LocalDateTime from, LocalDateTime to){
        return documentRecordRepository.numberOfViews(from, to);
    }

    private List<DocumentTrend> getFirst10(List<DocumentTrend> documentTrends){
        return documentTrends
                .stream()
                .limit(10)
                .collect(Collectors.toList());
    }
}
