package com.epam.esm.domain.payload;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class CertificateRequest {
    private Long tagId;

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer duration;

    private LocalDateTime createDate;

    private LocalDateTime lastUpdateDate;

    public CertificateRequest() {
    }

    public CertificateRequest(Long tagId,
                              Long id,
                              String name,
                              String description,
                              BigDecimal price,
                              Integer duration,
                              LocalDateTime createDate,
                              LocalDateTime lastUpdateDate) {
        this.tagId = tagId;
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
    }

    public CertificateRequest(Long id,
                              String name,
                              String description,
                              BigDecimal price,
                              Integer duration,
                              LocalDateTime createDate,
                              LocalDateTime lastUpdateDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CertificateRequest that = (CertificateRequest) o;
        return tagId.equals(that.tagId) &&
                id.equals(that.id) &&
                description.equals(that.description) &&
                price.equals(that.price) &&
                duration.equals(that.duration) &&
                createDate.equals(that.createDate) &&
                lastUpdateDate.equals(that.lastUpdateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagId, id, description, price, duration, createDate, lastUpdateDate);
    }

}
