import '../styles/Header.css';

function Header() {
  return (
    <header className="header">
      <div className="header__icon">⚡</div>
      <h1 className="header__title">Page Pulse</h1>
      <p className="header__subtitle">
        Analyze any webpage instantly — get SEO insights, performance metrics, and accessibility checks.
      </p>
    </header>
  );
}

export default Header;
