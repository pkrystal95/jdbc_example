package memo.model.dao;

import memo.model.dto.MemoDTO;
import memo.model.dto.MemoWithAuthorDTO;
import memo.model.util.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemoJdbcDAO implements MemoDAO {
    @Override
    public Long create(Long userId, String title, String content) {
        final String sql = """
                INSERT INTO memo (user_id, title, content)
                VALUES (?, ?, ?)
                """;
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql,
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, userId);
            ps.setString(2, title);
            ps.setString(3, content);
            int updated = ps.executeUpdate(); // 반영된게 있는지 체크
            if (updated == 0) throw new SQLException("삽입 실패, memo");

            try (ResultSet rs = ps.getGeneratedKeys()) { // PK
                if (rs.next()) return rs.getLong(1);
            }
            throw new SQLException("삽입 실패, memo");
        } catch (SQLException e) {
            throw new RuntimeException("MemoJdbcDAO.create error", e);
        }
    }

    private MemoDTO mapMemo(ResultSet rs) throws SQLException {
        return new MemoDTO(
                rs.getLong("memo_id"),
                rs.getLong("user_id"),
                rs.getString("title"),
                rs.getString("content"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }

    @Override
    public Optional<MemoDTO> findById(Long memoId) {
        final String sql = """
                SELECT *
                FROM memo
                WHERE memo_id = ?
                """;
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, memoId);
            try (ResultSet rs = ps.executeQuery()) { // PK
                return rs.next() ? Optional.of(mapMemo(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("MemoJdbcDAO.findById error", e);
        }
    }

    @Override
    public List<MemoDTO> findByUserId(Long userId, int limit, int offset) {
        final String sql = """
                SELECT *
                FROM memo
                WHERE user_id = ?
                LIMIT ? OFFSET ?
                """;
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            try (ResultSet rs = ps.executeQuery()) {
                List<MemoDTO> list = new ArrayList<>();
                while (rs.next()) list.add(mapMemo(rs));
                return list;
            }
        } catch (SQLException e) {
            throw new RuntimeException("MemoJdbcDAO.findByUserId error", e);
        }
    }

    @Override
    public List<MemoWithAuthorDTO> findAllWithAuthor(int limit, int offset) {
        final String sql = """
                SELECT m.memo_id, m.title, m.content, m.created_at,
                       u.user_id, u.username, u.display_name
                FROM memo m
                JOIN user_account u ON u.user_id = m.user_id
                LIMIT ? OFFSET ?
                """;
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                List<MemoWithAuthorDTO> list = new ArrayList<>();
                while (rs.next()) list.add(mapMemoWithAuthor(rs));
                return list;
            }
        } catch (SQLException e) {
            throw new RuntimeException("MemoJdbcDAO.findAllWithAuthor error", e);
        }
    }

    private MemoWithAuthorDTO mapMemoWithAuthor(ResultSet rs) throws SQLException {
        return new MemoWithAuthorDTO(
                rs.getLong("memo_id"),
                rs.getString("title"),
                rs.getString("content"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getLong("user_id"),
                rs.getString("username"),
                rs.getString("display_name")
        );
    }

    @Override
    public int deleteById(Long memoId) {
        final String sql = "DELETE FROM memo WHERE memo_id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, memoId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("MemoJdbcDAO.deleteById error", e);
        }
    }
}