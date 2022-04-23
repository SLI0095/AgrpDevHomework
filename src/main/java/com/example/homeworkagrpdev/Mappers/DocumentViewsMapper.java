package com.example.homeworkagrpdev.Mappers;

import com.example.homeworkagrpdev.Entities.DocumentViews;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DocumentViewsMapper implements RowMapper<DocumentViews> {
    @Override
    public DocumentViews mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new DocumentViews(
                UUID.fromString(rs.getNString("document_uuid")),
                rs.getInt("number_of_views")
        );
    }
}
