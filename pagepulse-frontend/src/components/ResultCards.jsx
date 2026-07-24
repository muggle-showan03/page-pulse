import '../styles/ResultCards.css';

function ResultCards({ data }) {
  if (!data) return null;

  const getStatusColor = (status) => {
    if (status >= 200 && status < 300) return 'success';
    if (status >= 300 && status < 400) return 'warning';
    return 'error';
  };

  const formatResponseTime = (ms) => {
    if (ms >= 1000) return `${(ms / 1000).toFixed(2)}s`;
    return `${ms}ms`;
  };

  return (
    <div className="results">
      <h2 className="results__title">Analysis Results</h2>
      <p className="results__url">{data.analyzedUrl}</p>

      {/* Highlight cards */}
      <div className="results__highlight">
        <div className="highlight-card">
          <div className={`highlight-card__value highlight-card__value--${getStatusColor(data.httpStatus)}`}>
            {data.httpStatus}
          </div>
          <div className="highlight-card__label">HTTP Status</div>
        </div>
        <div className="highlight-card">
          <div className="highlight-card__value highlight-card__value--accent">
            {formatResponseTime(data.responseTimeMs)}
          </div>
          <div className="highlight-card__label">Response Time</div>
        </div>
      </div>

      {/* Detail cards */}
      <div className="results__grid">
        <DetailCard
          icon="📄"
          label="Page Title"
          value={data.pageTitle}
          isText
        />
        <DetailCard
          icon="📝"
          label="Meta Description"
          value={data.metaDescription}
          isText
        />
        <DetailCard
          icon="🔤"
          label="H1 Tags"
          value={data.h1Count}
        />
        <DetailCard
          icon="🖼️"
          label="Images Missing Alt"
          value={data.imagesMissingAltCount}
          warnIfNonZero
        />
        <DetailCard
          icon="📊"
          label="Word Count"
          value={data.wordCount}
        />
      </div>
    </div>
  );
}

function DetailCard({ icon, label, value, isText = false, warnIfNonZero = false }) {
  const isEmpty = value === '' || value === null || value === undefined;
  const displayValue = isEmpty ? 'Not found' : value;

  const valueClass = isText
    ? `result-card__value${isEmpty ? ' result-card__value--empty' : ''}`
    : `result-card__value result-card__value--large`;

  return (
    <div className="result-card">
      <div className="result-card__header">
        <div className="result-card__icon">{icon}</div>
        <span className="result-card__label">{label}</span>
      </div>
      <div
        className={valueClass}
        style={warnIfNonZero && value > 0 ? { color: 'var(--warning)' } : {}}
      >
        {displayValue}
      </div>
    </div>
  );
}

export default ResultCards;
