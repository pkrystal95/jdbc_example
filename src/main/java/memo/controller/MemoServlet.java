package memo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import memo.model.dao.MemoDAO;
import memo.model.dao.MemoJdbcDAO;
import memo.model.dto.MemoDTO;

import java.io.IOException;
import java.util.List;

/**
 * MemoServlet
 * - GET /memos        : 메모 목록 출력 (list.jsp forward)
 * - GET /memos/new    : 작성 폼 출력 (form.jsp forward)
 * - POST /memos       : 메모 저장 후 목록으로 redirect
 */
@WebServlet(urlPatterns = {"/memos", "/memos/new"})
public class MemoServlet extends HttpServlet {

    private MemoDAO memoDAO;

    @Override
    public void init() throws ServletException {
        // 실제 서비스에서는 DI 컨테이너/팩토리로 주입하겠지만, 여기선 간단히 직접 생성
        this.memoDAO = new MemoJdbcDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 폰트 안깨지게
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        String servletPath = req.getServletPath(); // 요청 주소
        if ("/memos/new".equals(servletPath)) { // == 하지 않기
            // 작성 폼 - create
            // forward : 넘겨준다
            req.getRequestDispatcher("/views/form.jsp").forward(req, resp);
            return;
        }

        // 목록
        Long userId = 1L; // 데모용: 로그인 없이 고정 사용자
        List<MemoDTO> memos = memoDAO.findByUserId(userId, 50, 0);
        req.setAttribute("memos", memos);
        req.getRequestDispatcher("/views/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // POST 본문을 파싱하기 전에 인코딩 지정
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String title = req.getParameter("title");
        String content = req.getParameter("content");

        Long userId = 1L; // 데모용 고정 사용자
        memoDAO.create(userId, title, content);

        // PRG 패턴으로 새로고침 중복 제출 방지
        // req.getContextPath()  -> localhost:port
        resp.sendRedirect(req.getContextPath() + "/memos");
    }
}