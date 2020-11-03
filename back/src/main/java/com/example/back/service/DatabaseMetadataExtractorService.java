package com.example.back.service;


import com.example.back.dto.TableMetaDataDTO;
import com.example.back.repository.DatabaseMetadataExtractorRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@AllArgsConstructor
public class DatabaseMetadataExtractorService {


    private final DatabaseMetadataExtractorRepo databaseMetadataExtractorRepo;

    /**
     * Service method, used 2 get list of tables with public schema and meta data to these tables
     * from database
     *
     * @return list of tables with meta data
     */
    public List<TableMetaDataDTO> extractTableMetaDataService() {
        return databaseMetadataExtractorRepo.extractTableMetaData();
    }
}
