package com.ezbuy.ordermodel.dto;

import java.util.List;

public class ProfileForBusinessCustDTO {
    private String fileName;
    private String contents;
    private byte[] contentsByte;
    private List<CoordinatesDTO> lstCoordidates;
    private List<PartnerDTO> listPartner;
    private RecordTypeDTO recordTypeScanDTO;
    private PutFileServerLogDto putFileServerLog;

    public List<PartnerDTO> getListPartner() {
        return listPartner;
    }

    public void setListPartner(List<PartnerDTO> listPartner) {
        this.listPartner = listPartner;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public byte[] getContentsByte() {
        return contentsByte;
    }

    public void setContentsByte(byte[] contentsByte) {
        this.contentsByte = contentsByte;
    }

    public List<CoordinatesDTO> getLstCoordidates() {
        return lstCoordidates;
    }

    public void setLstCoordidates(List<CoordinatesDTO> lstCoordidates) {
        this.lstCoordidates = lstCoordidates;
    }

    public RecordTypeDTO getRecordTypeScanDTO() {
        return recordTypeScanDTO;
    }

    public void setRecordTypeScanDTO(RecordTypeDTO recordTypeScanDTO) {
        this.recordTypeScanDTO = recordTypeScanDTO;
    }

    public PutFileServerLogDto getPutFileServerLog() {
        return putFileServerLog;
    }

    public void setPutFileServerLog(PutFileServerLogDto putFileServerLog) {
        this.putFileServerLog = putFileServerLog;
    }
}
