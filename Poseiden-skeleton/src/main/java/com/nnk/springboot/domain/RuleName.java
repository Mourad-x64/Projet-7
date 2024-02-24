package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name = "rulename")
public class RuleName {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int ruleNameId;

    @NotBlank(message = "name is mandatory")
    private String name;
    @NotBlank(message = "description is mandatory")
    private String description;
    @NotBlank(message = "json is mandatory")
    private String json;
    @NotBlank(message = "template is mandatory")
    private String template;
    @NotBlank(message = "sqlStr is mandatory")
    private String sqlStr;
    @NotBlank(message = "sqlPart is mandatory")
    private String sqlPart;

    public int getRuleNameId() {
        return ruleNameId;
    }

    public void setRuleNameId(int id) {
        this.ruleNameId = id;
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

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getSqlStr() {
        return sqlStr;
    }

    public void setSqlStr(String sqlStr) {
        this.sqlStr = sqlStr;
    }

    public String getSqlPart() {
        return sqlPart;
    }

    public void setSqlPart(String sqlPart) {
        this.sqlPart = sqlPart;
    }
}

