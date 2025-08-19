
package memo.model.dao;

import memo.model.dto.UserDTO;
import memo.model.util.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserJdbcDAO implements UserDAO {
    @Override
    public Long create(String username, String displayName) {
        final String sql = """
        INSERT INTO user_account (username, display_name)
        VALUES (?, ?)
        """;
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql,
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, username);
            ps.setString(2, displayName);
            int updated = ps.executeUpdate(); // 반영된게 있는지 체크
            if (updated == 0) throw new SQLException("삽입 실패, user_account");

            try (ResultSet rs = ps.getGeneratedKeys()) { // PK
                if (rs.next()) return rs.getLong(1);
            }
            throw new SQLException("삽입 실패, user_account");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("UserJdbcDAO.create error", e);
        }
    }

    private UserDTO mapUser(ResultSet rs) throws SQLException {
        return new UserDTO(
                rs.getLong("user_id"),
                rs.getString("username"),
                rs.getString("display_name"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }

    @Override
    public Optional<UserDTO> findById(Long userId) {
        final String sql = """
        SELECT *
        FROM user_account
        WHERE user_id = ?
        """;
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) { // PK
//                if (!rs.next()) return Optional.empty();
//                return Optional.of(mapUser(rs));
                return rs.next() ? Optional.of(mapUser(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("UserJdbcDAO.findById error", e);
        }
    }

    @Override
    public Optional<UserDTO> findByUsername(String username) {
        final String sql = """
        SELECT *
        FROM user_account
        WHERE username = ?
        """;
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) { // PK
//                if (!rs.next()) return Optional.empty();
//                return Optional.of(mapUser(rs));
                return rs.next() ? Optional.of(mapUser(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("UserJdbcDAO.findByUsername error", e);
        }
    }

    @Override
    public List<UserDTO> findAll(int limit, int offset) {
        final String sql = """
        SELECT *
        FROM user_account
        LIMIT ? OFFSET ?
        """;
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                List<UserDTO> list = new ArrayList<>();
                while (rs.next()) list.add(mapUser(rs));
                return list;
            }
        } catch (SQLException e) {
            throw new RuntimeException("UserJdbcDAO.findAll error", e);
        }    }
}
