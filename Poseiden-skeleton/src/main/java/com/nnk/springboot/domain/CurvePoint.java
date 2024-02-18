package com.nnk.springboot.domain;

import jakarta.persistence.*;

import java.security.Timestamp;


@Entity
@Table(name = "curvepoint")
public class CurvePoint {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int curvePointId;
    private Timestamp asOfDate;
    private Double term;
    private Double value;
    private Timestamp creationDate;


    public int getCurvePointId() {
        return curvePointId;
    }

    public void setCurvePointId(int id) {
        this.curvePointId = id;
    }

    public Timestamp getAsOfDate() {
        return asOfDate;
    }

    public void setAsOfDate(Timestamp asOfDate) {
        this.asOfDate = asOfDate;
    }

    public Double getTerm() {
        return term;
    }

    public void setTerm(Double term) {
        this.term = term;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }
}
