package com.example.back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ColumnMetaDataDTO {
    private String columnName;
    private String type;
    private List<String> constraintsNames;
    private List<String> constraintsTypes;
}
