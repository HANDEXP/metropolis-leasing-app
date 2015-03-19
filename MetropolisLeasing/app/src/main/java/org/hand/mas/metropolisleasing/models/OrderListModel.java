package org.hand.mas.metropolisleasing.models;

/**
 * Created by gonglixuan on 15/3/10.
 */
public class OrderListModel {
    private String project_number;
    private String project_status_desc;

    private String bp_class;
    private String organization_code;
    private String bp_name;
    private String id_card_no;
    private String project_source;

    public OrderListModel(String project_number, String project_status_desc, String bp_class, String organization_code, String bp_name, String id_card_no, String project_source){
        this.project_number = project_number;
        this.project_status_desc = project_status_desc;
        this.bp_class = bp_class;
        this.organization_code = organization_code;
        this.bp_name = bp_name;
        this.id_card_no = id_card_no;
        this.project_source = project_source;
    }

    public String getProjectNumber(){
        return this.project_number;
    }

    public String getProjectStatusDesc() {
        return project_status_desc;
    }

    public String getBpClass() {
        return bp_class;
    }

    public String getOrganizationCode() {
        return organization_code;
    }

    public String getBpName() {
        return bp_name;
    }

    public String getIdCardNo() {
        return id_card_no;
    }

    public String getProjectSource() {
        return project_source;
    }
}
