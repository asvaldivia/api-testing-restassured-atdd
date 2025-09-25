package org.example.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ReportResponse {
    @JsonProperty
    private List<ReportItem> report;

    public List<ReportItem> getReport(){
        return report;
    }

}
