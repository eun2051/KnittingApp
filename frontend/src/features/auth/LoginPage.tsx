import { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const LoginPage = ({ onLoggedIn }: { onLoggedIn: () => void }) => {
  const [form, setForm] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async () => {
    try {
      const payload = {
        email: form.email.trim(),
        password: form.password.trim()
      };
      console.log('로그인 요청 데이터:', JSON.stringify(payload, null, 2));
      const res = await axios.post('/api/auth/login', payload);
      const token = res.data.data.token;
      localStorage.setItem('jwt', token);
      onLoggedIn();
    } catch (e: any) {
      console.log('로그인 에러 응답:', JSON.stringify(e.response?.data, null, 2));
      const msg = e.response?.data?.message;
      const code = e.response?.data?.code;
      setError(msg ? `${msg}${code ? ` (${code})` : ''}` : '로그인 실패');
    }
  };

  return (
    <div className="relative min-h-screen flex flex-col justify-center items-center bg-gradient-to-br from-yarn-cream via-yarn-pink/30 to-yarn-peach font-cute">
      <div className="cloud-bg" />
      <div className="fluffy-card w-full max-w-xs mx-auto mt-20 flex flex-col gap-4 items-center shadow-cute">
        <h2 className="text-3xl font-bold text-yarn-pink mb-2 flex items-center gap-2">
          <span>로그인</span>
          <span className="text-3xl">🧶</span>
        </h2>
        <p className="text-yarn-pink/80 text-sm mb-2">포근한 뜨개 생활에 오신 것을 환영합니다!</p>
        <input className="input w-full rounded-xl border-2 border-yarn-pink/40 px-4 py-2 focus:outline-none focus:border-yarn-pink" placeholder="이메일" value={form.email} onChange={e => setForm(f => ({ ...f, email: e.target.value }))} />
        <input className="input w-full rounded-xl border-2 border-yarn-pink/40 px-4 py-2 focus:outline-none focus:border-yarn-pink" type="password" placeholder="비밀번호" value={form.password} onChange={e => setForm(f => ({ ...f, password: e.target.value }))} />
        {error && <div className="text-red-400 text-sm font-bold w-full text-center">{error}</div>}
        <button className="fluffy-btn w-full bg-yarn-pink text-white font-bold py-2 rounded-xl mt-2 shadow-cute" onClick={handleLogin}>로그인</button>
        <div className="w-full text-center mt-4">
          <span className="text-yarn-pink/80 text-sm">처음이신가요?</span>
          <button className="ml-2 text-yarn-pink font-bold underline hover:text-yarn-peach transition" onClick={() => navigate('/register')}>회원가입</button>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
