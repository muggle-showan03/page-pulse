import '../styles/ErrorAlert.css';

function ErrorAlert({ message, onDismiss }) {
  if (!message) return null;

  return (
    <div className="error-alert" role="alert">
      <div className="error-alert__content">
        <span className="error-alert__icon">⚠️</span>
        <p className="error-alert__message">{message}</p>
        <button
          className="error-alert__close"
          onClick={onDismiss}
          aria-label="Dismiss error"
        >
          ✕
        </button>
      </div>
    </div>
  );
}

export default ErrorAlert;
