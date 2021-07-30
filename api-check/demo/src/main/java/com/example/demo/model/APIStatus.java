package com.example.demo.model;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "api_status_logs")
public class APIStatus {

    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "pk_id", nullable = false)
    @Id
    private UUID id;

    @Column(name = "class_name")
    private String className;

    @Column(name = "method_name")
    private String methodName;

    @Column(name = "request_mapping_api")
    private String apiCode;

    @Column(name = "request_mapping_http_code")
    private String methodTypeCode;

    @Column(name = "permission_api")
    private String apiDb;

    @Column(name = "permission_code")
    private String methodTypeDb;

    @Column(name = "status")
    private boolean status;

    @Column(name = "message")
    private String message;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "premissionpayload")
    private String premissionPayload;

    @Column(name = "requestmappayload")
    private String requestMapPayload;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getApiCode() {
        return apiCode;
    }

    public void setApiCode(String apiCode) {
        this.apiCode = apiCode;
    }

    public String getMethodTypeCode() {
        return methodTypeCode;
    }

    public void setMethodTypeCode(String methodTypeCode) {
        this.methodTypeCode = methodTypeCode;
    }

    public String getApiDb() {
        return apiDb;
    }

    public void setApiDb(String apiDb) {
        this.apiDb = apiDb;
    }

    public String getMethodTypeDb() {
        return methodTypeDb;
    }

    public void setMethodTypeDb(String methodTypeDb) {
        this.methodTypeDb = methodTypeDb;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getPremissionPayload() {
        return premissionPayload;
    }

    public void setPremissionPayload(String premissionPayload) {
        this.premissionPayload = premissionPayload;
    }

    public String getRequestMapPayload() {
        return requestMapPayload;
    }

    public void setRequestMapPayload(String requestMapPayload) {
        this.requestMapPayload = requestMapPayload;
    }
}
