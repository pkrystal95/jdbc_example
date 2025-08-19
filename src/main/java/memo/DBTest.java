package memo;

import memo.model.dao.MemoDAO;
import memo.model.dao.MemoJdbcDAO;
import memo.model.dao.UserDAO;
import memo.model.dao.UserJdbcDAO;

public class DBTest {
    public static void main(String[] args) {
        UserDAO userDAO = new UserJdbcDAO();
        MemoDAO memoDAO = new MemoJdbcDAO();
        // 사용자 만들기
        System.out.println("😀 사용자 만들기 시작");
        try {
            userDAO.create("charlie", "Charlie Park");
            // 3
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("😀 사용자 만들기 완료");
        // 메모 작성
        System.out.println("😀 메모 작성 시작");
        Long memoId1 = null;
        Long memoId2 = null; // scope
        try {
            memoId1 = memoDAO.create(3L, "안녕하세요", "찰리에요");
            memoId2 = memoDAO.create(3L, "반갑습니다", "채플린에요");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("😀 메모 작성 완료");
        // 단건 조회
        System.out.println("😀 단건 조회 시작");
        System.out.println(
                memoDAO.findById(memoId1)
        );
        System.out.println("😀 단건 조회 완료");
        // 유저 기준 목록 조회
        System.out.println("😀 유저 기준 목록 조회 시작");
        System.out.println(
                memoDAO.findByUserId(3L, 10, 0)
        );
        System.out.println("😀 유저 기준 목록 조회 완료");
        // 조인 조회
        System.out.println("😀 조인 조회 시작");
        System.out.println(
                memoDAO.findAllWithAuthor(10, 0)
        );
        System.out.println("😀 조인 조회 완료");
        // 메모 삭제
        System.out.println("😀 메모 삭제 시작");
        memoDAO.deleteById(memoId1);
        memoDAO.deleteById(memoId2);
        System.out.println("😀 메모 삭제 완료");
    }
}