package com.example.homeworkagrpdev.Mappers;


import com.example.homeworkagrpdev.Entities.DocumentRecord;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DocumentRecordMapper implements RowMapper<DocumentRecord> {

    @Override
    public DocumentRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new DocumentRecord(
                rs.getInt("id"),
                UUID.fromString(rs.getString("document_uuid")),
                UUID.fromString(rs.getString("user_uuid")),
                rs.getTimestamp("opened_on")
        );
    }
}
