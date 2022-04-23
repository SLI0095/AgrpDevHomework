package com.example.homeworkagrpdev.Repositories;

import com.example.homeworkagrpdev.Entities.DocumentViews;
import com.example.homeworkagrpdev.Entities.DocumentRecord;
import com.example.homeworkagrpdev.Mappers.DocumentViewsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Repository
public class DocumentRecordRepository {

    final String mostOpenedOverallQuery = "SELECT a.document_uuid, IFNULL(number_of_views, 0) AS number_of_views FROM " +
            "(SELECT DISTINCT document_record.document_uuid FROM document_record) a LEFT JOIN " +
            "(SELECT document_record.document_uuid, " +
            "COUNT(*) AS number_of_views " +
            "FROM document_record WHERE document_record.opened_on " +
            "BETWEEN :from AND :to " +
            "GROUP BY document_record.document_uuid) b " +
            "ON a.document_uuid = b.document_uuid " +
            "ORDER BY number_of_views DESC LIMIT 10;";

    final String mostOpenedUniqueUsersQuery = "SELECT a.document_uuid, IFNULL(number_of_views, 0) AS number_of_views FROM " +
            "(SELECT DISTINCT document_record.document_uuid FROM document_record) a LEFT JOIN " +
            "(SELECT document_record.document_uuid, " +
            "COUNT(DISTINCT document_record.document_uuid, document_record.user_uuid) AS number_of_views " +
            "FROM document_record WHERE document_record.opened_on " +
            "BETWEEN :from AND :to " +
            "GROUP BY document_record.document_uuid ) b " +
            "ON a.document_uuid = b.document_uuid " +
            "ORDER BY number_of_views DESC LIMIT 10;";

    final String numberOfViewsAllQuery = "SELECT a.document_uuid, IFNULL(number_of_views, 0) AS number_of_views FROM " +
            "(SELECT DISTINCT document_record.document_uuid FROM document_record) a LEFT JOIN " +
            "(SELECT document_record.document_uuid, " +
            "COUNT(DISTINCT document_record.document_uuid, document_record.user_uuid) " +
            "AS number_of_views " +
            "FROM document_record WHERE document_record.opened_on " +
            "BETWEEN :from AND :to " +
            "GROUP BY document_record.document_uuid) b " +
            "ON a.document_uuid = b.document_uuid;";

    final String numberOfViewsForDocumentQuery = "SELECT COUNT(DISTINCT document_record.document_uuid, document_record.user_uuid) " +
            "AS number_of_views " +
            "FROM document_record WHERE document_record.opened_on " +
            "BETWEEN :from AND :to " +
            "AND document_record.document_uuid = :document;";

    final String averageViewsQuery = "SELECT AVG(c.number_of_views) FROM " +
            "(SELECT a.document_uuid, IFNULL(number_of_views, 0) AS number_of_views FROM " +
            "(SELECT DISTINCT document_record.document_uuid FROM document_record) a LEFT JOIN " +
            "(SELECT document_record.document_uuid, COUNT(DISTINCT document_record.document_uuid, document_record.user_uuid) " +
            "AS number_of_views FROM document_record WHERE " +
            "document_record.opened_on BETWEEN :from AND :to " +
            "GROUP BY document_record.document_uuid ) b ON a.document_uuid = b.document_uuid) c;";


    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    public void addRecord(DocumentRecord documentRecord){
        jdbcTemplate.update("INSERT INTO document_record (document_uuid, user_uuid) values (:document, :user)",
                new MapSqlParameterSource()
                        .addValue("document", documentRecord.getDocumentUuid().toString())
                        .addValue("user", documentRecord.getUserUuid().toString())
                );
    }

    public List<DocumentViews> mostOpenedInLastWeekOverall(LocalDateTime now) {
        return jdbcTemplate.query(this.mostOpenedOverallQuery,
               new MapSqlParameterSource()
                       .addValue("from", Timestamp.valueOf(now.minus(7, ChronoUnit.DAYS)))
                       .addValue("to", Timestamp.valueOf(now)),
               new DocumentViewsMapper());
    }

    public List<DocumentViews> mostOpenedInLastWeekUniqueUsers(LocalDateTime now) {
        return jdbcTemplate.query(this.mostOpenedUniqueUsersQuery,
                new MapSqlParameterSource()
                        .addValue("from", Timestamp.valueOf(now.minus(7, ChronoUnit.DAYS)))
                        .addValue("to", Timestamp.valueOf(now)),
                new DocumentViewsMapper());
    }

    public int numberOfViews(UUID documentUuid, LocalDateTime from, LocalDateTime to){
        return jdbcTemplate.queryForObject(this.numberOfViewsForDocumentQuery,
                new MapSqlParameterSource()
                        .addValue("from", Timestamp.valueOf(from))
                        .addValue("to", Timestamp.valueOf(to))
                        .addValue("document", documentUuid.toString()),
                Integer.TYPE
        );
    }

    public float averageNumberOfViews(LocalDateTime from, LocalDateTime to){
        return jdbcTemplate.queryForObject(this.averageViewsQuery,
                new MapSqlParameterSource()
                        .addValue("from", Timestamp.valueOf(from))
                        .addValue("to", Timestamp.valueOf(to)),
                Float.TYPE
        );
    }

    public List<DocumentViews> numberOfViews(LocalDateTime from, LocalDateTime to) {
        return jdbcTemplate.query(this.numberOfViewsAllQuery,
                new MapSqlParameterSource()
                        .addValue("from", Timestamp.valueOf(from))
                        .addValue("to", Timestamp.valueOf(to)),
                new DocumentViewsMapper());
    }

    public List<DocumentViews> numberOfViews(String query) {
        return jdbcTemplate.query(query,
                new DocumentViewsMapper());
    }

}
