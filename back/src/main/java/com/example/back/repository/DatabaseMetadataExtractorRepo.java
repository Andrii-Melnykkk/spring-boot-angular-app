package com.example.back.repository;

import com.example.back.dto.ColumnMetaDataDTO;
import com.example.back.dto.TableMetaDataDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class DatabaseMetadataExtractorRepo {

    private static final String TABLE_SQL = "SELECT table_name FROM information_schema.tables " +
            "WHERE table_schema = 'public'";
    private static final String COLUMN_SQL = "SELECT \n" +
            "c.column_name,\n" +
            "c.data_type,\n" +
            " STRING_AGG(DISTINCT tc.CONSTRAINT_NAME, ', ') constraint_name,\n" +
            " STRING_AGG(DISTINCT tc.CONSTRAINT_type, ', ') constraint_type\n" +
            "FROM\n" +
            "information_schema.columns AS c\n" +
            "LEFT JOIN INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE AS ccu\n" +
            "          ON ccu.COLUMN_NAME = c.COLUMN_NAME\n" +
            "LEFT JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS tc\n" +
            "          ON ccu.CONSTRAINT_NAME = tc.CONSTRAINT_NAME\n" +
            "\t\t  WHERE c.TABLE_NAME = :tableName\n" +
            "\t\t  GROUP BY\n" +
            "    c.column_name,\n" +
            "\tc.data_type";

    private final NamedParameterJdbcTemplate jdbcTemplate;


    /**
     * Method, used 2 get list of tables with public schema and meta data to these tables
     * from database
     *
     * @return list of tables with meta data
     */
    public List<TableMetaDataDTO> extractTableMetaData() {

        List<TableMetaDataDTO> tableMetaDataDTOS;

        tableMetaDataDTOS = jdbcTemplate.query(
                TABLE_SQL,
                (rs, rowNum) ->
                        new TableMetaDataDTO(
                                rs.getString("table_name")
                        )
        );

        tableMetaDataDTOS.forEach(tableMetaDataDTO -> tableMetaDataDTO.setColumnMetaData(
                extractColumnMetaData(tableMetaDataDTO.getTableName())));

        return tableMetaDataDTOS;
    }

    /**
     * Method, used 2 get list of columns and meta data to these columns from given table
     *
     * @param tableName - name of table, which meta data need to be find out
     * @return list of columns with meta data
     */
    private List<ColumnMetaDataDTO> extractColumnMetaData(String tableName) {

        return jdbcTemplate.query(
                COLUMN_SQL,
                new MapSqlParameterSource()
                        .addValue("tableName", tableName),
                (rs, rowNum) -> new ColumnMetaDataDTO(
                        rs.getString("column_name"),
                        rs.getString("data_type"),
                        Arrays.asList(ArrayUtils.nullToEmpty(StringUtils.splitByWholeSeparator(
                                rs.getString("constraint_name"), ", "))),
                        Arrays.asList(ArrayUtils.nullToEmpty(StringUtils.splitByWholeSeparator(
                                rs.getString("constraint_type"), ", "))))

        );
    }
}



