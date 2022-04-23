package com.example.homeworkagrpdev.Entities;

import java.util.*;

public class DocumentTrend implements Comparable<DocumentTrend>{

    private UUID documentUuid;
    private List<Integer> history;
    private int current;
    private float score;
    private final float degradation = 0.9f;
    private float sqrAvg;
    private float average;

    public DocumentTrend(UUID documentUuid, int current) {
        this.documentUuid = documentUuid;
        this.current = current;
        this.history = new ArrayList<>();
        this.score = 0;
        this.sqrAvg = 0;
        this.average = 0;
    }

    public UUID getDocumentUuid() {
        return documentUuid;
    }

    public void setDocumentUuid(UUID documentUuid) {
        this.documentUuid = documentUuid;
    }

    public List<Integer> getHistory() {
        return history;
    }

    public void setHistory(List<Integer> history) {
        this.history = history;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    //calculating trend using standard score
    public void calculateScore(){
        int sizeOfData = this.history.size();
        int sum = history.stream().reduce(0, Integer::sum);
        float average = (float)sum / (float) sizeOfData;
        float sum2 = 0;
        for(int i : history){
            sum2 += Math.pow(i-average, 2);
        }
        float std = (float) Math.sqrt(sum2 / sizeOfData);
        this.score = (this.current - average) / std;
    }

    //calculating trend using standard score that take in count the age of data
    public void calculateScoreWithDegradation(){
        for(int value : history){
            if(this.average == 0 && this.sqrAvg == 0){
                this.average = (float) value;
                this.sqrAvg = (float) Math.pow(value, 2);
            } else {
                this.average = this.average * this.degradation + value * (1 - this.degradation);
                this.sqrAvg = (float) (this.sqrAvg * this.degradation + (Math.pow(value, 2) * (1 - this.degradation)));
            }
        }
        float std = this.std();
        if(std == 0){
            this.score = (current - this.average);
        } else {
            this.score = (current - this.average) / std;
        }
    }

    private float std(){
        return (float) Math.sqrt(this.sqrAvg - (Math.pow(this.average, 2)));
    }

    public void addHistory(int data){
        this.history.add(data);
    }

    @Override
    public int compareTo(DocumentTrend o) {
        return Float.compare(o.getScore(), this.getScore());
    }
}
