package org.example.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReportItem {
    @JsonProperty
    private String start;
    @JsonProperty
    private String end;
    @JsonProperty
    private String title;

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getTitle() {
        return title;
    }
}
