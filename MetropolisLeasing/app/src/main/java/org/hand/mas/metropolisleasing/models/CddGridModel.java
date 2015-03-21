package org.hand.mas.metropolisleasing.models;

import java.io.Serializable;

/**
 * Created by gonglixuan on 15/3/16.
 */
public class CddGridModel implements Serializable{

    private String attachment_id;
    private String cdd_item_id;
    private String file_path;
    private String description;
    private String file_name;
    private String file_suffix;
    private boolean remote;

    public CddGridModel(String attachment_id, String cdd_item_id, String file_path, String file_name, String description, String file_suffix, boolean remote) {
        this.attachment_id = attachment_id;
        this.cdd_item_id = cdd_item_id;
        this.file_path = file_path;
        this.file_name = file_name;
        this.description = description;
        this.file_suffix = file_suffix;
        this.remote = remote;
    }

    public String getAttachment_id() { return attachment_id; }

    public String getCddItemId() {
        return cdd_item_id;
    }

    public String getFilePath() {
        return file_path;
    }

    public String getDescription() {
        return description;
    }

    public String getFileName() {
        return file_name;
    }

    public String getFileSuffix() {
        return file_suffix;
    }

    public boolean getRemote() {return remote;};
}
