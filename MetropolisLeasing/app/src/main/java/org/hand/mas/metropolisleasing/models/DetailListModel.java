package org.hand.mas.metropolisleasing.models;

/**
 * Created by gonglixuan on 15/3/11.
 */
public class DetailListModel {
    private String project_id;
    private String project_number;
    private String bp_name;
    private String check_id;
    private String description;
    private String cdd_count;
    private String cdd_item_id;

    public DetailListModel(String project_id,String project_number, String bp_name, String check_id ,String description, String cdd_count, String cdd_item_id) {
        this.project_id = project_id;
        this.project_number = project_number;
        this.bp_name = bp_name;
        this.check_id = check_id;
        this.description = description;
        this.cdd_count = cdd_count;
        this.cdd_item_id = cdd_item_id;
    }

    public String getProjectId() {
        return project_id;
    }

    public String getProjectNumber() {
        return project_number;
    }

    public String getBpName() {
        return bp_name;
    }

    public String getCheckId() {
        return check_id;
    }

    public String getDescription() {
        return description;
    }


    public String getCddCount() {
        return cdd_count;
    }

    public String getCddItemId(){
        return cdd_item_id;
    }
}
