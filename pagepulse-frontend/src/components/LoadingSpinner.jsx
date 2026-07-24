import '../styles/LoadingSpinner.css';

function LoadingSpinner() {
  return (
    <div className="spinner-overlay">
      <div className="spinner" />
      <p className="spinner-text">Analyzing page…</p>
    </div>
  );
}

export default LoadingSpinner;
