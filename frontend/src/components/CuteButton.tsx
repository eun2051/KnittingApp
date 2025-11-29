interface CuteButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  children: React.ReactNode;
}

export default function CuteButton({ children, ...props }: CuteButtonProps) {
  return (
    <>
      <button
        className="bg-yarn-pink text-white rounded-full px-6 py-2 shadow-lg hover:bg-pink-300 transition-all font-bold text-lg"
        {...props}
      >
        {children}
      </button>
      {/* Tailwind 커스텀 색상 적용 테스트용 박스 */}
      <div className="w-32 h-32 bg-yarn-pink rounded-3xl shadow-cute m-10 flex items-center justify-center font-cute text-pink-700">
        핑크 박스
      </div>
    </>
  );
}
