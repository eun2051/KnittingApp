import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import ProjectList from './features/project/ProjectList';
import ProjectDetailPage from './features/project/ProjectDetailPage';
import RegisterPage from './features/auth/RegisterPage';
import LoginPage from './features/auth/LoginPage';
import './index.css';

// 인증 상태를 확인하는 컴포넌트
const RequireAuth = ({ children }: { children: React.ReactElement }) => {
  const token = localStorage.getItem('jwt');
  return token ? children : <Navigate to="/login" replace />;
};

function App() {
  // 인증 성공 시 목록으로 이동
  const handleAuthSuccess = () => {
    window.location.href = '/';
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-yarn-cream via-yarn-pink/30 to-yarn-peach p-0 flex justify-center items-start font-cute">
      <BrowserRouter>
        <Routes>
          {/* 회원가입 화면 */}
          <Route path="/register" element={<RegisterPage onRegistered={handleAuthSuccess} />} />
          {/* 로그인 화면 */}
          <Route path="/login" element={<LoginPage onLoggedIn={handleAuthSuccess} />} />
          {/* 메인 화면 (목록) - 인증 필요 */}
          <Route path="/" element={
            <RequireAuth>
              <ProjectList />
            </RequireAuth>
          } />
          {/* 상세 화면 (주소 뒤에 id가 붙음) - 인증 필요 */}
          <Route path="/project/:id" element={
            <RequireAuth>
              <ProjectDetailPage />
            </RequireAuth>
          } />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;