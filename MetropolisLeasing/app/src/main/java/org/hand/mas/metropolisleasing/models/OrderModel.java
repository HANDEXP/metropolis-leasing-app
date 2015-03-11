package org.hand.mas.metropolisleasing.models;

/**
 * Created by gonglixuan on 15/3/10.
 */
public class OrderModel {
    private String project_number;
    private String project_status_desc;

    private String bp_class;
    private String organization_code;
    private String bp_name;
    private String id_card_no;
    private String project_source;

    public OrderModel(String project_number,String project_status_desc,String bp_class,String organization_code,String bp_name,String id_card_no, String project_source){
        this.project_number = project_number;
        this.project_status_desc = project_status_desc;
        this.bp_class = bp_class;
        this.organization_code = organization_code;
        this.bp_name = bp_name;
        this.id_card_no = id_card_no;
        this.project_source = project_source;
    }

    public String getProject_number(){
        return this.project_number;
    }

    public String getProject_status_desc() {
        return project_status_desc;
    }

    public String getBp_class() {
        return bp_class;
    }

    public String getOrganization_code() {
        return organization_code;
    }

    public String getBp_name() {
        return bp_name;
    }

    public String getId_card_no() {
        return id_card_no;
    }

    public String getProject_source() {
        return project_source;
    }
}
