import { useState } from 'react';
import '../styles/UrlInput.css';

function UrlInput({ onAnalyze, isLoading }) {
  const [url, setUrl] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    const trimmed = url.trim();
    if (trimmed) {
      onAnalyze(trimmed);
    }
  };

  return (
    <div className="url-input">
      <form className="url-input__form" onSubmit={handleSubmit}>
        <input
          id="url-input-field"
          type="text"
          className="url-input__field"
          placeholder="Enter a URL to analyze (e.g., https://example.com)"
          value={url}
          onChange={(e) => setUrl(e.target.value)}
          disabled={isLoading}
          autoComplete="url"
          spellCheck="false"
        />
        <button
          id="analyze-button"
          type="submit"
          className="url-input__button"
          disabled={isLoading || !url.trim()}
        >
          <span className="url-input__button-icon">🔍</span>
          {isLoading ? 'Analyzing...' : 'Analyze'}
        </button>
      </form>
    </div>
  );
}

export default UrlInput;
