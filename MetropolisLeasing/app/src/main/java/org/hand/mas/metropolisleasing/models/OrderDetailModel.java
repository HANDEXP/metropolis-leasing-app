package org.hand.mas.metropolisleasing.models;

/**
 * Created by gonglixuan on 15/3/11.
 */
public class OrderDetailModel {
    private String project_number;
    private String project_source;
    private String description;
    private String cdd_list_id;
    private String cdd_count;

    public OrderDetailModel(String project_number, String project_source, String cdd_list_id, String description, String cdd_count) {
        this.project_number = project_number;
        this.project_source = project_source;
        this.cdd_list_id = cdd_list_id;
        this.description = description;
        this.cdd_count = cdd_count;
    }

    public String getProject_number() {
        return project_number;
    }

    public String getProject_source() {
        return project_source;
    }

    public String getDescription() {
        return description;
    }

    public String getCdd_list_id() {
        return cdd_list_id;
    }

    public String getCdd_count() {
        return cdd_count;
    }
}
