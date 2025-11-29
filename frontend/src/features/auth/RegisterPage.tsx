import { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const RegisterPage = ({ onRegistered }: { onRegistered: () => void }) => {
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: '', password: '', passwordConfirm: '' });
  const [error, setError] = useState('');

  const handleRegister = async () => {
    if (!form.email.match(/^\S+@\S+\.\S+$/)) {
      setError('이메일 형식이 올바르지 않습니다.');
      return;
    }
    if (form.password !== form.passwordConfirm) {
      setError('비밀번호가 일치하지 않습니다.');
      return;
    }
    try {
      await axios.post('auth/register', {
        email: form.email,
        password: form.password,
      });
      onRegistered();
    } catch (e: any) {
      const msg = e.response?.data?.message;
      const code = e.response?.data?.code;
      setError(msg ? `${msg}${code ? ` (${code})` : ''}` : '회원가입 실패');
    }
  };

  return (
    <div className="relative min-h-screen flex flex-col justify-center items-center bg-gradient-to-br from-yarn-cream via-yarn-pink/30 to-yarn-peach font-cute">
      <div className="cloud-bg" />

      <div className="fluffy-card w-full max-w-xs mx-auto mt-20 flex flex-col gap-4 items-center shadow-cute">
        <div className="flex items-center gap-3 mb-2">
          {/* 뒤로가기 버튼 */}
          <button
            onClick={() => navigate('/login')}
            className="w-7 h-7 flex items-center justify-center bg-yarn-pink text-white rounded-full shadow-md hover:bg-yarn-pink/80 transition font-bold text-sm"
            aria-label="뒤로가기"
          >
            &lt;
          </button>
          
          <h2 className="text-3xl font-bold text-yarn-pink flex items-center gap-2">
            <span>회원가입</span>
            <span className="text-3xl">🧶</span>
          </h2>
        </div>
        <p className="text-yarn-pink/80 text-sm mb-2">포근한 뜨개 생활을 시작해보세요!</p>
        <input className="input w-full rounded-xl border-2 border-yarn-pink/40 px-4 py-2 focus:outline-none focus:border-yarn-pink" type="email" placeholder="이메일" value={form.email} onChange={e => setForm(f => ({ ...f, email: e.target.value }))} />
        <input className="input w-full rounded-xl border-2 border-yarn-pink/40 px-4 py-2 focus:outline-none focus:border-yarn-pink" type="password" placeholder="비밀번호" value={form.password} onChange={e => setForm(f => ({ ...f, password: e.target.value }))} />
        <input className="input w-full rounded-xl border-2 border-yarn-pink/40 px-4 py-2 focus:outline-none focus:border-yarn-pink" type="password" placeholder="비밀번호 확인" value={form.passwordConfirm} onChange={e => setForm(f => ({ ...f, passwordConfirm: e.target.value }))} />
        {error && <div className="text-red-400 text-sm font-bold w-full text-center">{error}</div>}
        <button className="fluffy-btn w-full bg-yarn-pink text-white font-bold py-2 rounded-xl mt-2 shadow-cute" onClick={handleRegister}>회원가입</button>
      </div>
    </div>
  );
};

export default RegisterPage;
