package com.ezbuy.ordermodel.dto;

import lombok.Data;

@Data
public class PutFileServerLogDto {
    private String accountServer;
    private String fileName;
    private String fileSize;
    private String hostAdd;
    private String liveTimeDay;
    private String logId;
    private String remoteDir;
    private String startDate;
    private String passServer;
    private String systemName;
}
